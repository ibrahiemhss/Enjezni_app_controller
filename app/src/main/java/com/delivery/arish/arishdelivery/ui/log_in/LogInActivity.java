package com.delivery.arish.arishdelivery.ui.log_in;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.delivery.arish.arishdelivery.R;
import com.delivery.arish.arishdelivery.base.BaseActivity;
import com.delivery.arish.arishdelivery.data.SharedPrefManager;
import com.delivery.arish.arishdelivery.mvp.presenter.LogInPresenter;
import com.delivery.arish.arishdelivery.ui.Main.MainActivity;
import com.delivery.arish.arishdelivery.ui.log_in.resetPassword.ResetPasswordActivity;
import com.google.firebase.iid.FirebaseInstanceId;

import butterknife.BindView;

@SuppressLint("Registered")
public class LogInActivity extends BaseActivity {
    private static final String TAG = "LogInActivity";

    /*@SuppressWarnings("WeakerAccess")
    @BindView(R.id.rv_container_login)
    protected RelativeLayout mRelativeLayout;*/
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.etEmail)
    EditText mEtEmail;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.etPassword)
    EditText mEtPassword;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.btnRegister)
    Button btnRegister;
    @SuppressWarnings("WeakerAccess")
    @BindView(R.id.txt_reset_password)
    TextView mTxtResetPass;

    private String mToken;


    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        /*first thing call SharedPrefManager that
        have sharedpreferences object we save all we want inside it
        here we want be sure if there saved value true in  isUserLoggedIn
         method  this method will save inside it boolean value true when the user
          log in first time and false value when the user log out*/
        if (SharedPrefManager.getInstance(getApplicationContext()).isUserLoggedIn()) {
            Intent myIntent = new Intent(LogInActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();

        }

    }


    @SuppressWarnings("SameReturnValue")
    @Override
    protected int getResourceLayout() {//method come from BasActivity initialize setContentView method
        return R.layout.activity_log_in;
    }

    @Override
    protected void init() {//method come from BasActivity
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressWarnings("deprecation")
    @Override
    protected void setListener() {//method come from BasActivity

        /*get new token of current device in string to send this string to server when log in*/
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(LogInActivity.this, instanceIdResult -> {
            mToken = instanceIdResult.getToken();
            Log.d(TAG, "FCM_TOKEN" + mToken);
        });
        //Log.d(TAG,"mytoken "+ FirebaseMessaging.getInst);
        Toast.makeText(this, "mytoken " + FirebaseInstanceId.getInstance().getToken(), Toast.LENGTH_LONG).show();
        onClickViews();
    }


    @SuppressLint("MissingFirebaseInstanceTokenRefresh")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void onClickViews() {//method onclick


        //when click On Log in Button
        btnLogin.setOnClickListener(v -> {


            LogInPresenter logInPresenter = new LogInPresenter(LogInActivity.this);//LogInPresenter

            //noinspection deprecation
            logInPresenter.requestLogin(//call requestLogin method to make Log in request
                    mEtEmail.getText().toString(), mEtPassword.getText().toString(), FirebaseInstanceId.getInstance().getToken());
        });

        btnRegister.setOnClickListener(v -> {//when click on Register  Button
            startActivity(new Intent(LogInActivity.this, RegisterActivity.class));
        });


        mTxtResetPass.setOnClickListener(view -> {//when click on reset password textView
            Intent intent = new Intent(LogInActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

    }

}
