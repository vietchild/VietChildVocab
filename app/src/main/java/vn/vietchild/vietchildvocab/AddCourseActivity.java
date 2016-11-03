package vn.vietchild.vietchildvocab;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import vn.vietchild.vietchildvocab.Adapters.NewCourseAdapter;
import vn.vietchild.vietchildvocab.Model.Course;

public class AddCourseActivity extends BaseActivity {

    private static final String TAG = "Add_Course_Activity";
    private DatabaseReference mDatabases;
    private FirebaseStorage mStorages;
    private FirebaseAuth mAuths;

    private    String uuid = getUid();
    RecyclerView rvAddCourse;
    NewCourseAdapter courseAdapter;
    ArrayList<String> registeredCourse;
    ArrayList<Course> mCourses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        mCourses = new ArrayList<>();
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mStorages = FirebaseStorage.getInstance();
        mAuths = FirebaseAuth.getInstance();
        mCourses = new ArrayList<Course>();
        registeredCourse = new ArrayList<>();
        rvAddCourse = (RecyclerView)findViewById(R.id.rvAddCourse);

        //Recycle View
        courseAdapter = new NewCourseAdapter(this,  mCourses);
        rvAddCourse.setAdapter(courseAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddCourse.setLayoutManager(linearLayoutManager);
        showProgressDialog();

        mDatabases.child("Users").child(uuid).child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseRegistered : dataSnapshot.getChildren()) {

                    if (courseRegistered.getValue().equals(true)) {
                    registeredCourse.add(courseRegistered.getKey());
                    }
                    }

                mDatabases.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                            Boolean registeredStatus = false;
                            Course course = courseSnapshot.getValue(Course.class);
                        if (registeredCourse.isEmpty()) {
                            registeredStatus = false;}
                            else {

                            for (int i =0; i<registeredCourse.size();i++) {
                                if (course.getCourseid().equals(registeredCourse.get(i).toString())){
                                    registeredStatus = true;
                                }
                            }
                        }
                        if (!registeredStatus) {
                            mCourses.add(course);
                        }
                        }

                        courseAdapter.notifyDataSetChanged();
                        hideProgressDialog();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




    }




}
