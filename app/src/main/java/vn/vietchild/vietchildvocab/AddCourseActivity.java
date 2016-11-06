package vn.vietchild.vietchildvocab;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import vn.vietchild.vietchildvocab.Adapters.NewCourseAdapter;
import vn.vietchild.vietchildvocab.FileManager.JsonManager;
import vn.vietchild.vietchildvocab.Model.AllCoursesThumb;
import vn.vietchild.vietchildvocab.Model.CourseWithoutModule;


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
    ArrayList<String> registeredCourse;
    ArrayList<CourseWithoutModule> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mCourses = new ArrayList<>();
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mStorages = FirebaseStorage.getInstance();
        mAuths = FirebaseAuth.getInstance();
        mCourses = new ArrayList<CourseWithoutModule>();
        registeredCourse = new ArrayList<>();
        rvAddCourse = (RecyclerView)findViewById(R.id.rvAddCourse);

        //Recycle View
        courseAdapter = new NewCourseAdapter(this,  mCourses);
        rvAddCourse.setAdapter(courseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddCourse.setLayoutManager(linearLayoutManager);
        showProgressDialog();

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




    }




}
