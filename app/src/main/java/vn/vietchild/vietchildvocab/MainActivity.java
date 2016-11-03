package vn.vietchild.vietchildvocab;

import android.content.Intent;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import vn.vietchild.vietchildvocab.Model.Course;

public class MainActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private   DatabaseReference mDatabases;
    private   FirebaseStorage mStorages;
    private   FirebaseAuth mAuths;
    private   int RC_SIGN_IN = 1980;

    private Button btnCourse, btnNewActivity;
    ArrayList<Course> mCourses;

    // TODO: KIỂM TRA VÀ BẮT UPDATE GOOGLE PLAY SERVICES
    // TODO: KIỂM TRA TÌNH TRẠNG INTERNET TRONG LẦN ĐẦU TIỀN MỞ MÁY
    StorageReference mStorageRef;
    DataSnapshot mdataSnapshot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuths = FirebaseAuth.getInstance();


             //LOGIN

        if(mAuths.getCurrentUser()!= null) {
            //TODO : ket noi database
            Toast.makeText(this, "ĐÃ SIGN IN", Toast.LENGTH_SHORT).show();
           /* VCDownloader newDownloader = new VCDownloader(getApplicationContext(),"c2");
            newDownloader.downloadItemsImages("c2m1"); */
        } else {
            // not signed in
            startActivityForResult(
                    // not signed in
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(AuthUI.EMAIL_PROVIDER)
                            .setIsSmartLockEnabled(false)
                            .build(),
                    RC_SIGN_IN);
        }



        File localfile = new File(getFilesDir(), "watermelon.jpg");
        if (localfile.exists()) {
            Toast.makeText(this, localfile.getPath().toString(), Toast.LENGTH_SHORT).show();
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

}
