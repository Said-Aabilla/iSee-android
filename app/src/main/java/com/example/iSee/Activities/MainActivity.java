package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.iSee.R;

public class MainActivity extends AppCompatActivity {
//Variables...


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
//initialisations...


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