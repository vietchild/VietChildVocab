package vn.vietchild.vietchildvocab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import vn.vietchild.vietchildvocab.DownloadManager.VCDownloader;
import vn.vietchild.vietchildvocab.Model.Course;
import vn.vietchild.vietchildvocab.Model.Module;
import vn.vietchild.vietchildvocab.SQLite.DatabaseHelper;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_NAME = "setting";
    private   DatabaseReference mDatabases;
    private   FirebaseStorage mStorages = FirebaseStorage.getInstance();
    private StorageReference storageRef = mStorages.getReferenceFromUrl("gs://vietchildvocab.appspot.com");
    private   FirebaseAuth mAuths;
    private   int RC_SIGN_IN = 19;
    private List<Course> allCourse;
    private List<Course> unRegisterdCourse;
    private ImageView ivMainBanner;

    private String serverVersion;

    // TODO: KIỂM TRA VÀ BẮT UPDATE GOOGLE PLAY SERVICES
    // TODO: KIỂM TRA TÌNH TRẠNG INTERNET TRONG LẦN ĐẦU TIỀN MỞ MÁY

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showProgressDialog();
        ivMainBanner = (ImageView)findViewById(R.id.imageViewMainBanner);

        File imgMainBanner = new File(getApplicationContext().getFilesDir(), "mainbanner.jpg");
        if (imgMainBanner.exists()) {
            String mPath = "file://"+ getApplicationContext().getFilesDir().getAbsolutePath()+"/"+ "mainbanner.jpg";
            Picasso.with(getApplicationContext())
                    .load(mPath)
                    .fit()
                    .into(ivMainBanner);
        }

        mAuths = FirebaseAuth.getInstance();
        mDatabases= FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        vc_db = DatabaseHelper.getInstance(getApplicationContext());


/*
        File database  = getApplicationContext().getDatabasePath("vocabdata.db");
        if (database.exists()) {

            Toast.makeText(this, "DATABASE EXIT", Toast.LENGTH_SHORT).show();
          //  allCourse =    vc_db.dbGetAllCourseThumb();

            database.delete();
        }

        else {
            Toast.makeText(this, "DATABASE NOT EXIT", Toast.LENGTH_SHORT).show();
        }

*/



        if(mAuths.getCurrentUser()!= null) {
          //TODO : ket noi database
           checkNewUpdate();


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


  private void checkNewUpdate(){

      final String clientVersion = sharedPreferences.getString("clientVersion", "");
     // final String clientVersion = "";
      mDatabases.child("lastupdate").addListenerForSingleValueEvent(new ValueEventListener() {
              @Override
              public void onDataChange(DataSnapshot dataSnapshot) {
                  serverVersion = dataSnapshot.getValue().toString();

                  if (!clientVersion.equals(serverVersion)){
                      updateCourseDatabase();
                      downloadBanners("mainbanner.jpg");
                      downloadBanners("headbanner.jpg");
                      SharedPreferences.Editor editor = sharedPreferences.edit();
                      editor.putString("clientVersion", serverVersion );
                      editor.apply();
                      String clientVersio = sharedPreferences.getString("clientVersion","");
                      Toast.makeText(MainActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                  }
                  else
                  {
                  hideProgressDialog();
                  Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                  startActivity(intent);
                  finish();
                  }
              }

              @Override
              public void onCancelled(DatabaseError databaseError) {

              }
          });



  }

    private void updateCourseDatabase (){
        mDatabases.child("courses").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot coursesnapshot: dataSnapshot.getChildren() ) {
                    Course course = coursesnapshot.getValue(Course.class);
                    VCDownloader downloadCourseImage = new VCDownloader(getApplicationContext(),course.getCourseid());
                    downloadCourseImage.downloadCourseImages();
                    // TINH TOTAL ITEM CUA COURSE
                    int courseTotalItems = 0;
                    Set<String> strings = course.getModules().keySet();
                    for (String keysetModule : strings ) {
                        Module module = course.getModules().get(keysetModule);
                        courseTotalItems = courseTotalItems + module.getItems().size();
                    }

                    // ADD COURSE
                    course.setCoursetotalitems(courseTotalItems);
                    vc_db.dbAddOrUpdateCourse(course);

                    //TINH TOTAL ITEM Cua MODULE
                    for (String keysetModule : strings )
                    {
                        Module module = course.getModules().get(keysetModule);
                        module.setModuletotalitems(module.getItems().size());
                        vc_db.dbAddOrUpdateModule(module,course.getCourseid().toString());
                    }

                }
                hideProgressDialog();
                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
                finish();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void downloadBanners(final String filename) {
        // Ghi vào course vào Users/UserID/Courses/ với giá trị true

        StorageReference mainbanner = storageRef.child("Vocab/Banners/" + filename);

        File localFile = new File(getApplicationContext().getFilesDir(), filename);
        try {
            localFile.createNewFile();
            mainbanner.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Log.i(TAG, "Download Image - OK:  " + filename);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e(TAG, "Download Image - FAIL" + filename);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
