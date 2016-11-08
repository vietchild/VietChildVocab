package vn.vietchild.vietchildvocab;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.vietchild.vietchildvocab.Adapters.NewCourseAdapter;
import vn.vietchild.vietchildvocab.DownloadManager.VCDownloader;
import vn.vietchild.vietchildvocab.Model.Course;
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
        unregisteredCourses = vc_db.dbGetUnregisteredCourses(getApplicationContext());
        if (unregisteredCourses.isEmpty()) {
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
                Toast.makeText(AddCourseActivity.this, "position: " + position , Toast.LENGTH_SHORT).show();
                Toast.makeText(AddCourseActivity.this, unregisteredCourses.get(position).getCoursename(), Toast.LENGTH_SHORT).show();
                writeUserNewCourse(unregisteredCourses.get(position));
                VCDownloader newDownloader = new VCDownloader(getApplicationContext(),unregisteredCourses.get(position).getCourseid().toString());
                newDownloader.downloadModulesImages();
            }
        });
    }
    private void writeUserNewCourse(Course course) {
        String TABLE_COURSES = "courses";

        // Courses Table Columns
        final String KEY_COURSE_ID = "courseid";
        final String KEY_COURSE_NAME = "coursename";
        final String KEY_COURSE_DESC = "coursedescription";
        final String KEY_COURSE_PRICE = "courseprice";
        final String KEY_COURSE_TOTAL_ITEMS = "coursetotalitems";
        final String KEY_COURSE_LEARNED_ITEMS = "courselearneditems";
        final String KEY_COURSE_REGISTER_DATE = "courseregisterdate";
        final String KEY_COURSE_LAST_LEARN = "courselastlearn";
        final String KEY_COURSE_STATUS = "coursestatus";
        final String KEY_COURSE_NOTIFY_TIME = "coursenotifytime";
        final String KEY_COURSE_ITEM_PER_DAY = "courseitemperday";
        final String KEY_COURSE_SCORE = "coursescore";

        DatabaseReference mDatabases = FirebaseDatabase.getInstance().getReference();
        FirebaseStorage mStorages = FirebaseStorage.getInstance();
        FirebaseAuth mAuths = FirebaseAuth.getInstance();
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        // Ghi vào course vào Users/UserID/Courses/ với giá trị true
        String courseID = course.getCourseid();


        // UPDATE SQLITE DATABASE

        vc_db = DatabaseHelper.getInstance(getApplicationContext());
        SQLiteDatabase db = vc_db.getWritableDatabase();
        db.beginTransaction();
        long mTIMESTAMP = new Date().getTime();
        //Khai bao dinh dang ngay thang
        SimpleDateFormat dinhDangThoiGian = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy ");


        //parse ngay thang sang dinh dang va chuyen thanh string.
        String hienThiThoiGian = dinhDangThoiGian.format(mTIMESTAMP);


        try {
            ContentValues values = new ContentValues();
            values.put(KEY_COURSE_STATUS, 1);
            values.put(KEY_COURSE_REGISTER_DATE,mTIMESTAMP);
            int rowEffect = db.update(TABLE_COURSES, values, KEY_COURSE_ID + "= ?", new String[]{String.valueOf(courseID)});
            if (rowEffect == 1) {

                Log.i(TAG, "Update Course " + courseID + " = " + 1 );
            }
            db.setTransactionSuccessful();
            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
            startActivity(intent);
            finish();


        } catch (Exception e) {
            Log.d(TAG, "Error while trying Update Course Status: " + courseID);
        } finally {
            db.endTransaction();
        }
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

}
        /*
        mDatabases.child("Users").child(uuid).child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy ma trận các course đã đăng ký trên Firebase;
                for (DataSnapshot courseRegistered : dataSnapshot.getChildren()) {

                    if (courseRegistered.getValue().equals(true)) {
                    registeredCourse.add(courseRegistered.getKey());
                    }
                    }
                // Đọc  "coursethumb.json"; và lấy ra ma trận tất cả các course chưa đăng ký
                JsonManager allcoursethumb = new JsonManager(getApplicationContext());
                AllCoursesThumb allCourses = allcoursethumb.readJSONtoCourseThumb(fileCourseThumb);
                for (String keyCourse : allCourses.getCourses().keySet()){
                    Boolean registeredStatus = false;

                    if (registeredCourse.isEmpty()) {
                        registeredStatus = false;}
                    else {

                        for (int i =0; i<registeredCourse.size();i++) {
                            if (keyCourse.equals(registeredCourse.get(i).toString())){
                                registeredStatus = true;
                            }
                        }
                    }

                    if (!registeredStatus) {
                        mCourses.add(allCourses.getCourses().get(keyCourse));
                    }

                    courseAdapter.notifyDataSetChanged();
                    hideProgressDialog();
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            Log.e(TAG, "Không lấy dược các course mới");
            }
        });


*/