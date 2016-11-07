package vn.vietchild.vietchildvocab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import vn.vietchild.vietchildvocab.DownloadManager.VCDownloader;
import vn.vietchild.vietchildvocab.FileManager.JsonManager;
import vn.vietchild.vietchildvocab.Model.AllCourses;
import vn.vietchild.vietchildvocab.Model.AllCoursesThumb;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.Model.Module;
import vn.vietchild.vietchildvocab.SQLite.DatabaseHelper;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private static final String fileCourseThumb = "coursethumb.json";
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_NAME = "setting";
    private   DatabaseReference mDatabases;
    private   FirebaseStorage mStorages;
    private   FirebaseAuth mAuths;
    private   int RC_SIGN_IN = 1980;
    DatabaseHelper vc_db;
    private boolean isNewUpdate = false;
    String dbpath;
    private Button btnCourse, btnNewActivity;
    AllCourses allcourse;
    private String serverDataVersion;

    // TODO: KIỂM TRA VÀ BẮT UPDATE GOOGLE PLAY SERVICES
    // TODO: KIỂM TRA TÌNH TRẠNG INTERNET TRONG LẦN ĐẦU TIỀN MỞ MÁY

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgressDialog();
        allcourse = new AllCourses();
        mAuths = FirebaseAuth.getInstance();
        mDatabases= FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        vc_db = DatabaseHelper.getInstance(getApplicationContext());

        //String databasepath = getApplicationContext().getDatabasePath();

        File database  = getApplicationContext().getDatabasePath("vocabdata.db");
        if (database.exists()) {

            Toast.makeText(this, "DATABASE EXIT", Toast.LENGTH_SHORT).show();
            database.delete();
        }

        else {
            Toast.makeText(this, "DATABASE NOT EXIT", Toast.LENGTH_SHORT).show();
        }






        if(mAuths.getCurrentUser()!= null) {
          //TODO : ket noi database
            final List<Course> courseList = new ArrayList<Course>();
          //  checkNewUpdate();
            mDatabases.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                 for (DataSnapshot coursesnapshot: dataSnapshot.getChildren() ) {
                     Course course = coursesnapshot.getValue(Course.class);
                     vc_db.addOrUpdateCourse(course);

                      Set<String> strings = course.getModules().keySet();

                   for (String keysetModule : strings )
                     {
                         Toast.makeText(MainActivity.this, "Module: " + keysetModule, Toast.LENGTH_SHORT).show();
                         Module module = course.getModules().get(keysetModule);
                         vc_db.addOrUpdateModule(module,course.getCourseid().toString());
                     }

                     courseList.add(course);

                 }

                    hideProgressDialog();

                    Toast.makeText(MainActivity.this, "DONE", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });





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



        btnCourse = (Button) findViewById(R.id.btnCourse);
        btnNewActivity = (Button)findViewById(R.id.btnNewActivity);
        TextView tvNhap1 = (TextView)findViewById(R.id.tvnhap1);
        TextView tvNhap2 = (TextView)findViewById(R.id.tvnhap2);
        tvNhap1.setText(getResources().getDisplayMetrics().toString());
        tvNhap2.setText(dpToPx(1) + " px");
        btnCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddCourseActivity.class);
                startActivity(intent);
               // finish();
            }
        });

        btnNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
                finish();
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

            }
        }
    }

  private int dpToPx(int dp) {
      Resources r = getResources();

      return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
  }

  private void checkNewUpdate(){

      final String dataversion = sharedPreferences.getString("lastupdate", "");
      if (dataversion.isEmpty()) {
         isNewUpdate=true;
      } else
      {

          mDatabases.child("lastupdate").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  serverDataVersion = dataSnapshot.getValue().toString();
                  if (!dataversion.equals(serverDataVersion)){
                      isNewUpdate = true;
                  }
                  else {
                      isNewUpdate = false;
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });
      }

      if (isNewUpdate) {
          mDatabases.addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  AllCoursesThumb courseThumbnail = dataSnapshot.getValue(AllCoursesThumb.class);
                  JsonManager jsonFile = new JsonManager(getApplicationContext());
                  jsonFile.writeCoursesThumbToJSON(courseThumbnail,fileCourseThumb);
                  //for (Course mcourse: courseThumbnail.getCourses().get)
                  for (String keyCourse: courseThumbnail.getCourses().keySet()){
                      VCDownloader downloadCourseImage = new VCDownloader(getApplicationContext(),keyCourse);
                      downloadCourseImage.downloadCourseImages();
                  }
                  SharedPreferences.Editor editor = sharedPreferences.edit();
                  editor.putString("lastupdate", serverDataVersion );
                  editor.apply();

                  hideProgressDialog();
              }
              @Override
              public void onCancelled(DatabaseError databaseError) {
              }
          });

      }



  }


}
