package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.R;
import com.example.iSee.Services.SessionManager;
import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity {
//Variables...
UserDbHelper userHelper = new UserDbHelper(this);
    //Check Internet connection
    private boolean isNetworkConnected(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Stetho.initializeWithDefaults(this);
//initialisations...
        if (isNetworkConnected()){
            SessionManager sessionManager=new SessionManager(this,SessionManager.Session_user);
            if (sessionManager.CheckLogin()){
                Intent intent=new Intent(this,HomeVolunteerActivity.class);
                intent.putExtra("email",sessionManager.getEmailFromSession());
                startActivity(intent);
            }
        }else{
            Intent intent=new Intent(this, NoInternetActivity.class);
            startActivity(intent);
        }


        findViewById(R.id.login_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

            }

        });

        findViewById(R.id.signup_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,CheckvisionActivity.class);
                startActivity(intent);

    }
        });

//some animations
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation zoomin= AnimationUtils.loadAnimation(MainActivity.this,R.anim.zoomin);
                (findViewById(R.id.eye)).startAnimation(zoomin);
                handler.postDelayed(this, 1000);
            }
        }, 1000);




}
    }