package vn.vietchild.vietchildvocab.DownloadManager;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

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

import java.io.File;
import java.io.IOException;

import vn.vietchild.vietchildvocab.Model.Item;

/**
 * Created by Nguyen Phung Hung on 03/11/16.
 */

public class VCDownloader {
    private static final String TAG = "Downloader Class";
    private DatabaseReference mDatabases = FirebaseDatabase.getInstance().getReference();
    private FirebaseStorage mStorages = FirebaseStorage.getInstance();
    private FirebaseAuth mAuths = FirebaseAuth.getInstance();
    private StorageReference storageRef = mStorages.getReferenceFromUrl("gs://vietchildvocab.appspot.com");
    private Context mContext;

    private String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String courseID;

    public VCDownloader() {
    }

    public VCDownloader(Context mContext, String courseID) {
        this.mContext = mContext;
        this.courseID = courseID;
    }



    public void downloadCourseImages() {
    // Ghi vào course vào Users/UserID/Courses/ với giá trị true

    StorageReference courseImage = storageRef.child("Vocab/Images/"+ courseID + ".jpg");

    File localFile = new File(mContext.getApplicationContext().getFilesDir(), courseID + ".jpg");
    try {
        localFile.createNewFile();
        courseImage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                Log.i(TAG, "Download Course Image - OK" + courseID);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Log.e(TAG, "Download Course Image - FAIL");
            }
        });
    } catch (IOException e) {
        e.printStackTrace();
    }

    }

    public void downloadModulesImages () {
        //ArrayList<String> listModulesImages = new ArrayList<>();
        mDatabases.child("courses").child(courseID).child("modules").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot moduleImageName : dataSnapshot.getChildren()) {
                    final String imageName = moduleImageName.getKey().toString();

                    File localFile = new File(mContext.getApplicationContext().getFilesDir(), imageName + ".jpg");
                    if (!localFile.exists()) {
                        StorageReference moduleImage = storageRef.child("Vocab/Images/" + imageName + ".jpg");

                        try {
                            localFile.createNewFile();
                            moduleImage.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                    Log.i(TAG, "Downloaded successfully: " + imageName);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                    Log.e(TAG, "Download Course Image - FAIL");
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                    else {
                        Log.i(TAG, "Module image: " + imageName + " exists");
                    }

                }
             //   Toast.makeText(mContext, mContext.getResources().getString(R.string.added_new_course) , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void downloadItemsImages (String moduleID) {
          mDatabases.child("courses").child(courseID).child("modules").child(moduleID).child("items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot itemID : dataSnapshot.getChildren()) {
                            Item item = itemID.getValue(Item.class);
                            final String itemName = item.getItemalias();
                            File localImageFile = new File(mContext.getApplicationContext().getFilesDir(), itemName + ".jpg");

                            if (!localImageFile.exists()) {
                                StorageReference itemImage = storageRef.child("Vocab/Images/" + itemName + ".jpg");

                                try {
                                    localImageFile.createNewFile();
                                    itemImage.getFile(localImageFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                                            Log.i(TAG, "Downloaded successfully jpg: " + itemName);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception exception) {
                                            // Handle any errors
                                            Log.e(TAG, "Download Course Image - FAIL");
                                        }
                                    });
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            else {
                                Log.i(TAG, "Item image: " + itemName + " exists");
                            }
                            File filePath = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/VietChild");
                            filePath.mkdirs();
                            File localAudioFile = new File(filePath, itemName + ".mp3");
                            if (!localAudioFile.exists()){
                            StorageReference itemAudio = storageRef.child("Vocab/Sound/" + itemName + ".mp3");

                            try {
                                localAudioFile.createNewFile();
                                itemAudio.getFile(localAudioFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        Log.i(TAG, "Downloaded successfully mp3: " + itemName);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        // Handle any errors
                                        Log.e(TAG, "Download Course Image - FAIL");
                                    }
                                });
                            } catch (IOException e) {
                                e.printStackTrace();
                                }
                            }
                            else {
                                Log.i(TAG, "Item audio: " + itemName + " exists");
                            }
                        }
                  //      Toast.makeText(mContext, mContext.getResources().getString(R.string.added_new_module) , Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
