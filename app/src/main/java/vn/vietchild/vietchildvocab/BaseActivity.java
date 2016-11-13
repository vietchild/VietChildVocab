package vn.vietchild.vietchildvocab;

/**
 * Created by Nguyen Phung Hung on 24/10/16.
 */

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import vn.vietchild.vietchildvocab.SQLite.DatabaseHelper;


public class BaseActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;
    DatabaseHelper vc_db;
    static final String SHARED_PREFERENCE_NAME = "setting";
    SharedPreferences sharedPreferences ;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public String getUid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }


}