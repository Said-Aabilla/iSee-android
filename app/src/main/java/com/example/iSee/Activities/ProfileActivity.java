package com.example.iSee.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class ProfileActivity extends AppCompatActivity {
    TextView emailTextView;
    TextView fullnameTextView;
    BottomNavigationItemView settingsItem;
    BottomNavigationItemView dashboardItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        emailTextView = findViewById(R.id.email_show);
        fullnameTextView = findViewById(R.id.fullname_show);
        settingsItem = findViewById(R.id.settingsItem);
        dashboardItem = findViewById(R.id.dashboardItem);

        emailTextView.append(": " + getIntent().getStringExtra("email"));
        fullnameTextView.append(": " + getIntent().getStringExtra("fullname"));

        findViewById(R.id.logout_btn).setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
        });

        settingsItem.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("fullname",getIntent().getStringExtra("fullname"));
            startActivity(settingsIntent);
        });
        dashboardItem.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, HomeVolunteerActivity.class);
            startActivity(homeIntent);
        });
    }
}