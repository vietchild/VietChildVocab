package vn.vietchild.vietchildvocab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import vn.vietchild.vietchildvocab.Fragments.GameVocabFragment;
import vn.vietchild.vietchildvocab.Fragments.PlayingVocabFragment;
import vn.vietchild.vietchildvocab.Fragments.UserSettingFragment;
import vn.vietchild.vietchildvocab.Model.Item;
import vn.vietchild.vietchildvocab.Model.Scores;
import vn.vietchild.vietchildvocab.Model.Topics;

import static com.firebase.ui.auth.ui.AcquireEmailHelper.RC_SIGN_IN;

public class MainNavigationActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, GameVocabFragment.OnFragmentInteractionListener, PlayingVocabFragment.OnFragmentInteractionListener, UserSettingFragment.onUpdateUserSetting {

        private static final String TAG = "Activity_Navigation";
        private static final String SHARED_PREFERENCE_NAME = "Setting";
        private static final String dataFile = "vietchildvocab-Topic-export.json"; // Dung de luu giu du lieu hien hanh
        private static final String tempDataFile = "temp_vietchildvocab-Topic-export.json"; //dung de luu giu du lieu moi
        HashSet<String> listdownloadItems; //Lưu các danh sách file cần load
        private ProgressDialog progressDialog;
        private SharedPreferences sharedPreferences;
        public List<Topics> currentDatabase = null; //Du lieu hien hanh
        public List<Topics> updatedDatabase = null; // Du lieu moi
        public ArrayList<Scores> allTopicAndScore;
        public String DataVersion;
        String json;
        Button btnUpdate;
        private Boolean isUpdate = true;
        private Boolean isFirstTime = false;
        private FragmentTransaction fragmentMain;

       //Database init
        public static DatabaseReference mData;
        public static FirebaseStorage mStorage;
        public static FirebaseAuth mAuth;
        StorageReference storageRef;
        DatabaseReference dataCourses, dataUsers, dataStats;



        // NAVIGATION HEADER INIT
        private ImageView imgUserAvatar;
        private TextView tvUserName, tvUserEmail, tvUserCoin;
        private Button btnUserEdit, btnUserLogout;
        private NavigationView navigationView;
        public static int REQUEST_CODE_IMAGE = 1;
        View headerview;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_navigation);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
    /* Đoạn này của Navigation
    */
    FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();
    mAuth = FirebaseAuth.getInstance();
    navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

    // TODO: KIỂM TRA VÀ BẮT UPDATE GOOGLE PLAY SERVICES
// TODO: KIỂM TRA TÌNH TRẠNG INTERNET TRONG LẦN ĐẦU TIỀN MỞ MÁY
    initControls(); // Gọi các khai báo biến
    //  showGameVocabFragment();
    initUserDetail();

    //CheckDataVersion();

//        fragmentMain        =  getSupportFragmentManager().beginTransaction();
//        fragmentMain.replace(R.id.content_main_navigation, new UpdateFragment(),"UpdateFragment");
//        fragmentMain.commit();
    // ATTENTION: This was auto-generated to implement the App Indexing API.
    // See https://g.co/AppIndexing/AndroidStudio for more information.

}



    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */

    //TODO: Xử lý các CallBack Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Sau khi user login xong thi hien ra ham nay
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // user is signed in!
                startActivity(new Intent(this, MainNavigationActivity.class));
                finish();
                initUserDetail();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }

        //Khi bấm vào Avatar thì chụp ảnh mới và lưu vào bộ nhớ máy
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            Bitmap avatar = (Bitmap) data.getExtras().get("data");
            File mypath = new File(getFilesDir().getAbsolutePath(), "myavatar.jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                avatar.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //TODO: upload avartar
            if (mAuth.getCurrentUser() != null) {
                uploadAvatar(avatar);
            }
            //Update picture in header's avatar
            Picasso.with(getApplicationContext()).
                    load("file://" + getFilesDir().getAbsolutePath() + "/myavatar.jpg").
                    transform(new CropCircleTransformation()).
                    memoryPolicy(MemoryPolicy.NO_CACHE).
                    into(imgUserAvatar);

            // INVOKE FRAGMENT User Setting Fragment
            if (getSupportFragmentManager().
                    getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1)
                    .getName().toString().equals("UserSettingFragment"))            {
                UserSettingFragment currentFragment = (UserSettingFragment)this
                        .getSupportFragmentManager().findFragmentByTag("UserSettingFragment");
                currentFragment.onActivityResult(requestCode, resultCode, data);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Upload Avatar vào Firebase và lưu lại URI vào userPhoto URI
    private void uploadAvatar(Bitmap bitmap) {
        // Create a reference to 'images/mountains.jpg'
        StorageReference avatarRef = storageRef.child("Vocab/UserAvatar/" + mAuth.getCurrentUser().getUid() + ".jpg");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] uploadAvatar = baos.toByteArray();
        UploadTask uploadTask = avatarRef.putBytes(uploadAvatar);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.e(TAG, "Upload Avatar to Storage Error");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUrl)
                        .build();
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User profile updated.");
                                    //  Toast.makeText(MainNavigationActivity.this, "PROFILE UPDATED", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void CheckDataVersion() {
        /* Method này có tác dụng load DataVersion của cơ sở dữ liệu. Nếu dataversion khác với lưu trong SharedPrefrences thì tải về cơ sở dữ liệu mới.
        Lưu thông tin trong Array và để chế độ offline. Lưu ảnh + Âm thanh trong bộ nhớ máy.
        */
        File localFile = new File(getFilesDir(), dataFile);
        if (localFile.exists() == false) {
            downloadDataFirstTime();
        } else {
            showGameVocabFragment();
            mData.child("Vocabulary").child("DataVersion").addValueEventListener(new ValueEventListener() {
                // Lấy database version trên cloud, save vào biến DataVersion, so sánh với Shared Preferences
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataVersion = dataSnapshot.getValue().toString();

                    if (DataVersion.equals(sharedPreferences.getString("DataVersion", null)) != true) {

                        btnUpdate.setVisibility(View.VISIBLE); // Cho hiện button Update
                        btnUpdate.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Tải bản data mới về và show Content
                                DownloadUpdate();

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "loadDataVersion:onCancelled", databaseError.toException());

                }
            });
        }

    }


    private List<Topics> readJSON(String filename)
    // Đọc file dữ liệu vietchildvocab-Topic-export.json
    {
        File localFile = new File(getFilesDir(), filename);
        if (localFile.exists()) {
            try {
                FileInputStream in = openFileInput(filename);
                BufferedReader reader = new
                        BufferedReader(new InputStreamReader(in));
                String data = "";
                StringBuilder builder = new StringBuilder();
                while ((data = reader.readLine()) != null) {
                    builder.append(data);
                    builder.append("\n");
                }
                in.close();
                json = builder.toString();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "CHƯA TẢI ĐƯỢC DỮ LIỆU", Toast.LENGTH_SHORT).show();
            json = null;
        }

        Gson dataVocab = new Gson();
        Type TopicListType = new TypeToken<ArrayList<Topics>>() {
        }.getType();
        List<Topics> listTopic = dataVocab.fromJson(json, TopicListType);
        return listTopic;
    }

    private ArrayList<Scores> getListAllTopicsAndScores() {
        ArrayList<Scores> topicAndScore = new ArrayList<>();
        Scores newItem = new Scores();
        if (currentDatabase.isEmpty() != true) {
            for (int i = 0; i < currentDatabase.size(); i++) {
                topicAndScore.add(new Scores(currentDatabase.get(i).getTopicname(),
                        currentDatabase.get(i).getTopicalias(),
                        currentDatabase.get(i).getTopicdesc(),
                        sharedPreferences.getFloat(currentDatabase.get(i).getTopicalias(), 0)

                ));
            }
            return topicAndScore;
        } else {
            return null;
        }
    }

    private void downloadDataFirstTime() {

        StorageReference dataFileRef = storageRef.child(dataFile);
        File localFile = new File(getFilesDir(), dataFile);
        try {
            localFile.createNewFile();
            dataFileRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    // Tải về file JSON dataFile
                    Toast.makeText(getApplicationContext(), "Đã tải xong Json", Toast.LENGTH_SHORT).show();
                    currentDatabase = readJSON(dataFile);
                    // Tải về cơ sở dữ liệu hình ảnh, âm thanh
                    DownloadItems(isFirstTime);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(getApplicationContext(), "TẢI VỀ DỮ LIỆU LẦN ĐẦU THẤT BẠI", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void DownloadUpdate() {
        /*
        Tải về datafile JSON mới và save vào tempDataFile
         */
        StorageReference islandRef = storageRef.child(dataFile);
        File localFile = new File(getFilesDir(), tempDataFile);
        try {
            localFile.createNewFile();
            islandRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    currentDatabase = readJSON(dataFile);
                    updatedDatabase = readJSON(tempDataFile);
                    // Local temp file has been created
                    DownloadItems(isUpdate);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Log.e(TAG, "DownloadUpdate - FAIL");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashSet<String> getListImageAlias(List<Topics> arrayData) {
        HashSet<String> listImageAlias = new HashSet<>();
        if (arrayData != null) {
            for (int j = 0; j < arrayData.size(); j++) {
                listImageAlias.add(arrayData.get(j).getTopicalias());
                for (int k = 0; k < arrayData.get(j).getItems().size(); k++) {
                    listImageAlias.add(arrayData.get(j).getItems().get(k).getItemalias());
                }
            }
            return listImageAlias;
        } else {
            return null;
        }
    }

    private HashSet<String> getNewFileList(HashSet<String> currentdb, HashSet<String> updateddb) {
        if (updateddb.isEmpty()) {
            return null;
        } else {
            updateddb.removeAll(currentdb);
            return updateddb;
        }
    }

    public void DownloadItems(boolean isUpdate) {
        if (isUpdate == true) {
            listdownloadItems = getNewFileList(getListImageAlias(currentDatabase), getListImageAlias(updatedDatabase));
        } else {
            listdownloadItems = getListImageAlias(currentDatabase);
        }

        if (listdownloadItems.isEmpty() != true) {
            progressDialog.show();
            progressDialog.setMax(listdownloadItems.size());
            for (int i = 0; i < listdownloadItems.size(); i++) {
                String aliasimage = "Vocab/Images/" + listdownloadItems.toArray()[i].toString() + ".jpg";
                //String aliassound = "Vocab/Sound/" + listdownloadItems.toArray()[i].toString() + ".mp3";
                StorageReference imagesRef = storageRef.child(aliasimage);
                File localImage = new File(getFilesDir(), listdownloadItems.toArray()[i].toString() + ".jpg");
                try {
                    final int progress = i;
                    localImage.createNewFile();
                    imagesRef.getFile(localImage).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(final FileDownloadTask.TaskSnapshot taskSnapshot) {
                            //  Download cơ sở dữ liệu về xong sẽ showContent
                            if (progress == listdownloadItems.size() - 1) {
                                progressDialog.dismiss();
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("DataVersion", DataVersion);
                                editor.apply();
                                showGameVocabFragment();
                            } else {
                                progressDialog.setProgress(progress);
                            }

                       /* final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 1s = 1000ms
                                    }
                        }, 5000);  */
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle any errors
                            Log.e(TAG, "DownloadItem - FAIL");
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("DataVersion", DataVersion);
            editor.apply();
            showGameVocabFragment();

        }
    }


    private void showGameVocabFragment() {
        currentDatabase = readJSON(dataFile);
        allTopicAndScore = getListAllTopicsAndScores();

        fragmentMain = getSupportFragmentManager().beginTransaction();
        fragmentMain.replace(R.id.content_main_navigation, new GameVocabFragment().newInstance(allTopicAndScore), "GameVocabFragment");
        fragmentMain.addToBackStack("GameVocabFragment");
        fragmentMain.commit();


    }




// TODO: BLOCK TEMPLATE

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //TODO: XU LY ACTIVITY CYCLE
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    //TODO: Xử ly các Fragment
    @Override
    public void onInteractionGameTopicClicked(int postion) {
        Toast.makeText(getApplicationContext(), "" + postion, Toast.LENGTH_SHORT).show();
        fragmentMain = getSupportFragmentManager().beginTransaction();
        fragmentMain.replace(R.id.content_main_navigation, new PlayingVocabFragment().newInstance((ArrayList<Item>) currentDatabase.get(postion).getItems()), "PlayingVocabFragment");
        fragmentMain.addToBackStack("PlayingVocabFragment");
        fragmentMain.commit();

    }

    @Override
    public void onInteractionGameVocabFinished(Uri uri) {
        Toast.makeText(getApplicationContext(), "NOTHING", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onInteractionUpdateUserSetting(Boolean isUpdated) {
        if (isUpdated){
            initUserDetail();
        }

    }


    //TODO: khai bác các OnClickListener
    // PHỤC VỤ VIỆC SIGN IN
    View.OnClickListener signin = new View.OnClickListener() {
        public void onClick(View v) {
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setProviders(
                                    AuthUI.EMAIL_PROVIDER,
                                    AuthUI.GOOGLE_PROVIDER)
                            .build(),
                    RC_SIGN_IN);
        }
    };
    View.OnClickListener singout = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            AuthUI.getInstance()
                    .signOut(MainNavigationActivity.this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // user is now signed out
                            initUserDetail();
                            //startActivity(new Intent(getApplicationContext(), MainNavigationActivity.class));
                            //finish();
                        }
                    });
        }
    };
    View.OnClickListener takephoto = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent cameraIntents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntents, REQUEST_CODE_IMAGE);
        }
    };
    View.OnClickListener callUserSettingFragment = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            fragmentMain = getSupportFragmentManager().beginTransaction();
            fragmentMain.replace(R.id.content_main_navigation, new UserSettingFragment(), "UserSettingFragment");
            fragmentMain.addToBackStack("UserSettingFragment");
            fragmentMain.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    };


    private void initControls() {
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        mData = FirebaseDatabase.getInstance().getReference();
        mStorage = FirebaseStorage.getInstance();
        storageRef = mStorage.getReferenceFromUrl("gs://vietchildvocab.appspot.com");
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setTitle(getString(R.string.progressbar_title));
        progressDialog.setMessage(getString(R.string.progressbar_message));
    }
    public void initUserDetail() {
        //ĐOẠN CODE QUẢN LÝ USER DETAIL
        headerview = navigationView.getHeaderView(0); // Khai báo mục header Navigation View
        tvUserName = (TextView) headerview.findViewById(R.id.tvUserName);
        tvUserEmail = (TextView) headerview.findViewById(R.id.tvUserEmail);
        tvUserCoin = (TextView) headerview.findViewById(R.id.tvUserCoin);
        imgUserAvatar = (ImageView) headerview.findViewById(R.id.imgUserAvatar);
        btnUserEdit = (Button) headerview.findViewById(R.id.buttonUserEdit);
        btnUserLogout = (Button) headerview.findViewById(R.id.buttonUserLogout);
        // Nếu người dùng đã đăng nhập thì thêm thông tin người dùng vào header
        if (mAuth.getCurrentUser() != null) {
            tvUserName.setText(mAuth.getCurrentUser().getDisplayName().toString());
            tvUserEmail.setText(mAuth.getCurrentUser().getEmail().toString());
            btnUserEdit.setText(getResources().getString(R.string.user_info_btnUserEdit));
            btnUserLogout.setVisibility(View.VISIBLE);
            btnUserLogout.setText(getResources().getString(R.string.user_info_btnUserLogout));
            //LOGOUT Khoi he thong
            btnUserLogout.setOnClickListener(singout);
            //Set file ảnh
            File mypath = new File(getFilesDir().getAbsolutePath(), "myavatar.jpg");
            if (mypath.exists()) {
                //Picasso.with(getApplicationContext()).load(auth.getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).into(imgUserAvatar);
                Picasso.with(getApplicationContext()).load("file://" + getFilesDir().getAbsolutePath() + "/myavatar.jpg").transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgUserAvatar);
            } else {
                if (mAuth.getCurrentUser().getPhotoUrl() != null) {
                    Picasso.with(getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgUserAvatar);
                }
            }
            //Khi bấm vào Avatar thì chụp ảnh mới và lưu vào bộ nhớ máy
            imgUserAvatar.setOnClickListener(takephoto);
            btnUserEdit.setOnClickListener(callUserSettingFragment);
            // tvUserCoin.setText((CharSequence) auth.getCurrentUser().getPhotoUrl());
        }
        // Nếu người dùng chưa đăng nhập thì mời đăng nhập.
        else {
            tvUserName.setText(getResources().getString(R.string.user_info_tvUserName));
            tvUserEmail.setText("");
            btnUserEdit.setText(getResources().getString(R.string.user_info_btnUserEdit_Login));
            btnUserLogout.setVisibility(View.INVISIBLE);
            Picasso.with(getApplicationContext())
                    .load(R.drawable.defaultavatar)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(imgUserAvatar);
            btnUserEdit.setOnClickListener(signin);

        }
    }

}
