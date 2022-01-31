package com.example.iSee.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Services.SessionManager;
import com.example.iSee.Views.ILoginView;

import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements ILoginView {

    //global variables

    String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
    };

    private final int requestCode = 101;

    //Global variables
    ILoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginController = new LoginController(this);
//initialisation
        final CheckBox rememberme=findViewById(R.id.remember_me);
        final EditText emailEdit = findViewById(R.id.user_email);
        final EditText passwordEdit = findViewById(R.id.user_password);

// Get rememnered values
        SessionManager sessionManager=new SessionManager(this,SessionManager.Rememberme_session);
        if (sessionManager.CheckRememberME()){
            HashMap<String,String > rememberMeDetail=sessionManager.getRemembermeDetailsFromSession();
            emailEdit.setText(rememberMeDetail.get(SessionManager.KEY_EMAILREMEMBERME));
            passwordEdit.setText(rememberMeDetail.get(SessionManager.KEY_PASSWORDREMEMBERME));
        }

        (findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginController.onLogin(emailEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());
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
    public void onLoginSuccess(String message, User user) {



        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent volunteerIntent = new Intent(this, HomeVolunteerActivity.class);
        Intent visuallyImpairedIntent = new Intent(this, HomeImpairedActivity.class);



        visuallyImpairedIntent.putExtra("fullname",user.getFullname());
        visuallyImpairedIntent.putExtra("email",user.getEmail());
        visuallyImpairedIntent.putExtra("vision",user.getVision());
        visuallyImpairedIntent.putExtra("language",user.getLanguage());
        volunteerIntent.putExtra("fullname",user.getFullname());
        volunteerIntent.putExtra("email",user.getEmail());
        volunteerIntent.putExtra("vision",user.getVision());
        volunteerIntent.putExtra("language",user.getLanguage());



        if (user.getVision()) {
            startActivity(volunteerIntent);
        }else if (!user.getVision()){
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