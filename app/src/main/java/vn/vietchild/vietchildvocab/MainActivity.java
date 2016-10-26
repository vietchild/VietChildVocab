package vn.vietchild.vietchildvocab;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import vn.vietchild.vietchildvocab.Adapters.NewCourseAdapter;
import vn.vietchild.vietchildvocab.Model.Course;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private   DatabaseReference mDatabases;
    private   FirebaseStorage mStorages;
    private   FirebaseAuth mAuths;
    private   int RC_SIGN_IN = 1980;
    private    String uuid = getUid();
    ArrayList<Course> mCourses;
    GridView gridViewCourse;

    StorageReference mStorageRef;
    DataSnapshot mdataSnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCourses = new ArrayList<>();
        mDatabases = FirebaseDatabase.getInstance().getReference();
        mStorages = FirebaseStorage.getInstance();
        mAuths = FirebaseAuth.getInstance();
        mCourses = new ArrayList<Course>();
        gridViewCourse = (GridView)findViewById(R.id.gridViewCourse);
        final NewCourseAdapter courseAdapter = new NewCourseAdapter(getApplicationContext(),mCourses);
        gridViewCourse.setAdapter(courseAdapter);

        //LOGIN
        if(mAuths.getCurrentUser()!= null) {
            //TODO : ket noi database
         /*
            Intent intent = new Intent(MainActivity.this, MainNavigationActivity.class);
            startActivity(intent);
            finish();
            */
        } else {
            // not signed in
            startActivityForResult(
                    // not signed in
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(AuthUI.EMAIL_PROVIDER)
                            .build(),
                    RC_SIGN_IN);
        }
        showProgressDialog();

       mDatabases.child("Courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot courseSnapshot : dataSnapshot.getChildren()) {
                Course course = courseSnapshot.getValue(Course.class);
                 mCourses.add(course);
                    Toast.makeText(MainActivity.this, " " + course.getCourseimage() + " " + course.getCoursetotalitems()
                            , Toast.LENGTH_SHORT).show();
                }
                courseAdapter.notifyDataSetChanged();
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Sign in again", Toast.LENGTH_SHORT).show();
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }
  /*  public void saveCourse (DataSnapshot dataSnapshot)
    {

        int position = 0;
        for(DataSnapshot courseSnapshot: dataSnapshot.getChildren()){
            mCourses.add(position,courseSnapshot.getKey().toString());
            position++;
        }
        Toast.makeText(this,mCourses.size()+" is size ", Toast.LENGTH_SHORT).show();
    }
*/


}
