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

import com.example.iSee.R;

public class NoInternetActivity extends AppCompatActivity {
    //Check Internet connection
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_internet);
        findViewById(R.id.try_again).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isNetworkConnected()) {
                    Intent intent = new Intent(NoInternetActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                Animation zoomin = AnimationUtils.loadAnimation(NoInternetActivity.this, R.anim.blink_anim);
                (findViewById(R.id.try_again)).startAnimation(zoomin);
                handler.postDelayed(this, 5000);
            }
        }, 1000);

    }
}