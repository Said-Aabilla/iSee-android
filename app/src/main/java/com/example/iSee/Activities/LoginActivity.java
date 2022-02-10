package com.example.iSee.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.iSee.Controllers.facade.ILoginController;
import com.example.iSee.Controllers.impl.LoginController;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Services.SessionManager;
import com.example.iSee.Views.ILoginView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class LoginActivity extends AppCompatActivity implements ILoginView {

    //global variables
    UserDbHelper userHelper = new UserDbHelper(this);
    ILoginView loginView=this;




    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };
 //check internet Connection
 private boolean isNetworkConnected() {
     ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
     return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
 }

    private final int requestCode = 101;

    //Global variables
    ILoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        List<String> userList = new ArrayList<String>();
        userList=userHelper.getUsers();
        Log.e("ssssssssssssssss",userList.toString());
        loginController = new LoginController(this);
//initialisation
        final CheckBox rememberme=findViewById(R.id.remember_me);
        final EditText emailEdit = findViewById(R.id.user_email);
        final EditText passwordEdit = findViewById(R.id.user_password);

// Get remembered values
        SessionManager sessionManager=new SessionManager(this,SessionManager.Rememberme_session);
        if (sessionManager.CheckRememberME()){
            HashMap<String,String > rememberMeDetail=sessionManager.getRemembermeDetailsFromSession();
            emailEdit.setText(rememberMeDetail.get(SessionManager.KEY_EMAILREMEMBERME));
            passwordEdit.setText(rememberMeDetail.get(SessionManager.KEY_PASSWORDREMEMBERME));
        }

        (findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isNetworkConnected()) {
                    if (loginController.onLogin(emailEdit.getText().toString().trim(),
                            passwordEdit.getText().toString().trim())) {
                        if (userHelper.checkUser(emailEdit.getText().toString().trim(), passwordEdit.getText().toString().trim())) {

                            loginView.onLoginSuccess("Login Success !", emailEdit.getText().toString().trim());
                            SessionManager sessionManager=new SessionManager(LoginActivity.this,SessionManager.Session_user);
                            sessionManager.createLoginSession(emailEdit.getText().toString().trim(),passwordEdit.getText().toString().trim());

                        } else {
                            loginView.onLoginFailed("N'existe pas !");

                        }

                    } else {
                        loginView.onLoginFailed("Quelque chose ne marche pas ! Ressayer");

                    }
                }else{
                    Toast.makeText(LoginActivity.this,"pas de connection",Toast.LENGTH_SHORT).show();
                }
                Animation animation= AnimationUtils.loadAnimation(LoginActivity.this,R.anim.sample_anim);
                (findViewById(R.id.login_btn)).startAnimation(animation);
                if(rememberme.isChecked()){
                    SessionManager sessionManager=new SessionManager(LoginActivity.this,SessionManager.Rememberme_session);
                    sessionManager.createRememberMeSession(emailEdit.getText().toString().trim(),passwordEdit.getText().toString().trim());
                }

            }
        });

        (findViewById(R.id.signup_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CheckvisionActivity.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.IconBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!isPermissionGranted(this, PERMISSIONS)) {
            askForPermission(PERMISSIONS);
        }
    }

    @Override
    public void onLoginSuccess(String message, String email) {

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent volunteerIntent = new Intent(this, HomeVolunteerActivity.class);
        Intent visuallyImpairedIntent = new Intent(this, HomeImpairedActivity.class);
        User user=userHelper.getUser(email);
        visuallyImpairedIntent.putExtra("email",user.getEmail());
        volunteerIntent.putExtra("email",user.getEmail());

        if (user.getVision().trim().equals("true")) {
            startActivity(volunteerIntent);
        }else if (user.getVision().equals("false")){
            startActivity(visuallyImpairedIntent);
        }
    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private boolean isPermissionGranted(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void askForPermission(String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, requestCode);
    }


}