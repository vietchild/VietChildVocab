package vn.vietchild.vietchildvocab;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.vietchild.vietchildvocab.Adapters.NewCourseAdapter;
import vn.vietchild.vietchildvocab.Adapters.RandomAdapter;
import vn.vietchild.vietchildvocab.DownloadManager.VCDownloader;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.Model.Item;
import vn.vietchild.vietchildvocab.Model.Module;
import vn.vietchild.vietchildvocab.SQLite.DatabaseHelper;


/*
Sử dụng recycle View
 Bước 1: Xem các course nào đã registerd
 Bước 2: Add các course chưa registered vào adapter
 */
public class AddCourseActivity extends BaseActivity {

    private static final String TAG = "Add_Course_Activity";
    private static final String fileCourseThumb = "coursethumb.json";
    private DatabaseReference mDatabases;
    private FirebaseStorage mStorages;
    private FirebaseAuth mAuths;

    private    String uuid = getUid();
    RecyclerView rvAddCourse;
    NewCourseAdapter courseAdapter;
    List<Course> unregisteredCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        unregisteredCourses = new ArrayList<Course>();
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mStorages = FirebaseStorage.getInstance();
        mAuths = FirebaseAuth.getInstance();
        rvAddCourse = (RecyclerView)findViewById(R.id.rvAddCourse);
        vc_db = DatabaseHelper.getInstance(getApplicationContext());

        List<Course> allCourseThumb = new ArrayList<>();
        allCourseThumb = vc_db.dbGetAllCourseThumb();

        for (Course course: allCourseThumb) {
            if (course.getCoursestatus()!=1 ) {
                unregisteredCourses.add(course);
            }
        }

        if (allCourseThumb.size()!=0 && unregisteredCourses.isEmpty()) {
            Toast.makeText(this, "NO NEW COURSE", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(intent);
            finish();
        }


        //Recycle View
        courseAdapter = new NewCourseAdapter(this,  unregisteredCourses);
        rvAddCourse.setAdapter(courseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddCourse.setLayoutManager(linearLayoutManager);
        courseAdapter.setOnItemClickListener(new NewCourseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                showProgressDialog();
             //   Toast.makeText(AddCourseActivity.this, "position: " + position + " it is: " + unregisteredCourses.get(position).getCourseid() , Toast.LENGTH_SHORT).show();
                writeUserNewCourse(unregisteredCourses.get(position));
                VCDownloader newDownloader = new VCDownloader(getApplicationContext(),unregisteredCourses.get(position).getCourseid().toString());
                newDownloader.downloadModulesImages();
                unregisteredCourses.remove(position);
                courseAdapter.notifyDataSetChanged();
            }
        });
    }


    private void writeUserNewCourse(Course course) {


        DatabaseReference mDatabases = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage mStorages = FirebaseStorage.getInstance();
        FirebaseAuth mAuths = FirebaseAuth.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Ghi vào course vào Users/UserID/Courses/ với giá trị true
        final String courseID = course.getCourseid().toString();
        long mTIMESTAMP = new Date().getTime();
        List<Module> downloadModules = new ArrayList<Module>();
        // UPDATE SQLITE DATABASE

        vc_db = DatabaseHelper.getInstance(getApplicationContext());
        vc_db.dbSetCourseStatus(courseID,1,mTIMESTAMP);
        vc_db = DatabaseHelper.getInstance(getApplicationContext());

        // GET ALL Items from Firebase Database to SQLite
        mDatabases.child("courses").child(courseID).child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot moduleSnapshot: dataSnapshot.getChildren()) {
                // Lay module
                Module module =  moduleSnapshot.getValue(Module.class);
                // Lay va luu item vao SQLite
                List<Item> itemList = module.getItems();
                if (itemList!=null) {
                    int itemListSize = itemList.size();
                    // set Item position randomly
                    RandomAdapter randomPosition = new RandomAdapter();
                    ArrayList<Integer> randomPositionArray = randomPosition.getRandomArray(itemListSize, itemListSize);

                    for (int i = 0; i < itemListSize; i++) {
                        itemList.get(i).setItemposition(randomPositionArray.get(i).intValue());
                        vc_db.dbAddOrUpdateItems(itemList.get(i), courseID, module.getModulealias());
                    }
                }
                // Load anh va audio cua actived Module
                if (module.getModuleactive()==1) {
                    VCDownloader itemdownload = new VCDownloader(getApplicationContext(),courseID);
                    itemdownload.downloadItemsImages(module.getModulealias());
                }
            }
            hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Khai bao dinh dang ngay thang
        SimpleDateFormat dinhDangThoiGian = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");
        //parse ngay thang sang dinh dang va chuyen thanh string.
        String hienThiThoiGian = dinhDangThoiGian.format(mTIMESTAMP);




//UPDATE FIREBASE DATABASE
        mDatabases.child("Stats").child("courses").child(course.getCourseid().toString())
                .child("Users").child(userID).setValue(true);
        Map<String, Object> newCourse = course.toCourseMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Users/" + userID +"/courses/" + course.getCourseid().toString(), true);
        childUpdates.put("/Users/" + userID +"/userprogress/courses/" + course.getCourseid().toString(), newCourse);
        mDatabases.updateChildren(childUpdates);
        mDatabases.child("Users").child(userID).child("userprogress").child("courses").child(course.getCourseid().toString())
                .child("courselearneditems").setValue(Long.valueOf(0));
        mDatabases.child("Users").child(userID).child("userprogress").child("courses").child(course.getCourseid().toString())
                .child("courseregisterdate").setValue(mTIMESTAMP);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();

    }

}
