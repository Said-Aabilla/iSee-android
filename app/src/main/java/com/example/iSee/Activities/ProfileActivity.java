package com.example.iSee.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class ProfileActivity extends AppCompatActivity {
    TextView emailTextView;
    TextView fullnameTextView;
    BottomNavigationItemView settingsItem;
    BottomNavigationItemView dashboardItem;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_1);

        emailTextView = findViewById(R.id.email_show);
        fullnameTextView = findViewById(R.id.fullname_show);
        settingsItem = findViewById(R.id.settingsItem);
        dashboardItem = findViewById(R.id.dashboardItem);
        builder = new AlertDialog.Builder(this);
        emailTextView.append("   " + getIntent().getStringExtra("email"));
        fullnameTextView.append("      " + getIntent().getStringExtra("fullname"));

        findViewById(R.id.logout_btn).setOnClickListener(view -> {
            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
            builder.setMessage("Do you want to log out from this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //the logout logic here
                            finish();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("log out");
            alert.show();
        });

        settingsItem.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("fullname",getIntent().getStringExtra("fullname"));
            startActivity(settingsIntent);
        });
        dashboardItem.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, HomeVolunteerActivity.class);
            homeIntent.putExtra("fullname",getIntent().getStringExtra("fullname"));
            startActivity(homeIntent);
        });
    }
}