package com.example.iSee.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Services.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class ProfileActivity extends AppCompatActivity {
    TextView emailTextView;
    TextView fullnameTextView;
    BottomNavigationItemView settingsItem;
    BottomNavigationItemView dashboardItem;
    AlertDialog.Builder builder;
    UserDbHelper userHelper = new UserDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_1);
//Get the user from SQLite
        String email=getIntent().getStringExtra("email");
        User user=userHelper.getUser(email);
        emailTextView = findViewById(R.id.email_show);
        fullnameTextView = findViewById(R.id.fullname_show);
        settingsItem = findViewById(R.id.settingsItem);
        dashboardItem = findViewById(R.id.dashboardItem);
        builder = new AlertDialog.Builder(this);
        emailTextView.append("   " + user.getEmail().trim());
        fullnameTextView.append("      " +user.getFullname().trim());

        findViewById(R.id.logout_btn).setOnClickListener(view -> {
            builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);
            builder.setMessage("Do you want to log out from this application ?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            SessionManager sessionManager=new SessionManager(ProfileActivity.this,SessionManager.Session_user);
                            sessionManager.LogoutUserFromSession();
                            Intent intent = new Intent(ProfileActivity.this,MainActivity.class);
                            startActivity(intent);
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
            settingsIntent.putExtra("email",getIntent().getStringExtra("email"));
            startActivity(settingsIntent);
        });
        dashboardItem.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, HomeVolunteerActivity.class);
            homeIntent.putExtra("fullname",user.getFullname().trim());
            startActivity(homeIntent);
        });
    }
}