package vn.vietchild.vietchildvocab.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import vn.vietchild.vietchildvocab.R;

import static vn.vietchild.vietchildvocab.MainNavigationActivity.REQUEST_CODE_IMAGE;
import static vn.vietchild.vietchildvocab.MainNavigationActivity.mAuth;

/*
Fragment này dùng để Update thông tin cá nhân của người dùng
Trong trường hợp người dùng lưu thay đổi, isUpdated = True, và gửi dữ liệu update đến người dùng
Trong trường hợp người dùng không lưu thay đổi, isUpdate = False;

 */
public class UserSettingFragment extends Fragment {
    private final static String TAG = "UserSettingFragment";
    private onUpdateUserSetting mListener;

    public UserSettingFragment() {
        // Required empty public constructor
    }
    private ImageView imgSettingUserAvatar;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        final Button btnEditProfile = (Button)view.findViewById(R.id.btnEditProfile);
        final Button btnSaveProfile = (Button)view.findViewById(R.id.btnSaveProfile);
        btnSaveProfile.setEnabled(false);
        final EditText edtUserName = (EditText)view.findViewById(R.id.edtUserName);
        final EditText edtOldPassword = (EditText)view.findViewById(R.id.edtUserOldPassword);
        final EditText edtNewPassword = (EditText)view.findViewById(R.id.edtUserNewPassword);
        imgSettingUserAvatar = (ImageView)view.findViewById(R.id.imgSettingUserAvatar);
        Picasso.with(getActivity().getApplicationContext()).load(R.drawable.defaultavatar).into(imgSettingUserAvatar);
      //  File mypath = new File(getActivity().getFilesDir().getAbsolutePath(), "myavatar.jpg");
       // edtUserName.setText(mypath.toString());

        if (mAuth.getCurrentUser() != null) {
            File mypath = new File(getActivity().getFilesDir().getAbsolutePath(), "myavatar.jpg");
            if (mypath.exists()) {
                //Picasso.with(getApplicationContext()).load(auth.getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).into(imgUserAvatar);
                Picasso.with(getActivity().getApplicationContext()).load("file://" + getActivity().getFilesDir().getAbsolutePath() + "/myavatar.jpg").transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgSettingUserAvatar);
            } else {
                if (mAuth.getCurrentUser().getPhotoUrl() != null) {
                    Picasso.with(getActivity().getApplicationContext()).load(mAuth.getCurrentUser().getPhotoUrl()).transform(new CropCircleTransformation()).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgSettingUserAvatar);
                }
            }
            imgSettingUserAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent cameraIntents = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    getActivity().startActivityForResult(cameraIntents, REQUEST_CODE_IMAGE);
                }
            });

            edtUserName.setText(mAuth.getCurrentUser().getDisplayName().toString());
            btnEditProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean editStatus = !edtUserName.isEnabled();
                    edtUserName.setEnabled(editStatus);
                    edtNewPassword.setEnabled(editStatus);
                    edtOldPassword.setEnabled(editStatus);
                    btnSaveProfile.setEnabled(editStatus);
                }
            });
            btnSaveProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    //Toast.makeText(getActivity().getApplicationContext(), "Da click save", Toast.LENGTH_SHORT).show();
                    if (!edtUserName.getText().toString().isEmpty()) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(edtUserName.getText().toString())
                                .build();
                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                           @Override
                                                           public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    edtUserName.setText(user.getDisplayName().toString());
                                    btnSaveProfile.setEnabled(false);
                                    edtUserName.setEnabled(false);
                                    edtNewPassword.setEnabled(false);
                                    edtOldPassword.setEnabled(false);
                                    onButtonPressed(true);
                                 Log.d(TAG, "User profile updated.");
                                 }
                                if (!task.isSuccessful()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.user_change_password_fail),
                                                                           Toast.LENGTH_SHORT).show();
                                }
                                     }
                                                       }
                                );

                    }


                    if (!edtNewPassword.getText().toString().isEmpty() && !edtOldPassword.getText().toString().isEmpty()) {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), edtOldPassword.getText().toString());

                        // Prompt the user to re-provide their sign-in credentials
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Log.d(TAG, "User re-authenticated.");
                                        user.updatePassword(edtNewPassword.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                        Toast.makeText(getActivity(), getResources().getString(R.string.user_change_password_success),
                                                                    Toast.LENGTH_SHORT).show();
                                            AuthCredential credential = EmailAuthProvider
                                                    .getCredential(user.getEmail(), edtNewPassword.getText().toString());
                                            user.reauthenticate(credential);
                                            edtNewPassword.setText("");
                                            edtOldPassword.setText("");
                                            btnSaveProfile.setEnabled(false);
                                            edtUserName.setEnabled(false);
                                            edtNewPassword.setEnabled(false);
                                            edtOldPassword.setEnabled(false);
                                            Log.d(TAG, "User password updated.");
                                                        }
                                            if (!task.isSuccessful()) {
                                            Toast.makeText(getActivity(), getResources().getString(R.string.user_change_password_fail),
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                });

                    }
                }

            });
        }

        return view;
    }

    /**
     * Receive the result from a previous call to
     * {@link #startActivityForResult(Intent, int)}.  This follows the
     * related Activity API as described there in
     * {link Activity#onActivityResult(int, int, Intent)}.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Picasso.with(getActivity().getApplicationContext()).
                load("file://" + getActivity().getFilesDir().getAbsolutePath() + "/myavatar.jpg").
                transform(new CropCircleTransformation()).
                memoryPolicy(MemoryPolicy.NO_CACHE).
                into(imgSettingUserAvatar);
        super.onActivityResult(requestCode, resultCode, data);

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Boolean isUpdated) {
        if (mListener != null) {
            mListener.onInteractionUpdateUserSetting(isUpdated);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof onUpdateUserSetting) {
            mListener = (onUpdateUserSetting) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface onUpdateUserSetting {
        // TODO: Update argument type and name
        void onInteractionUpdateUserSetting(Boolean isUpdated);

    }
}
