package com.example.iSee.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                       builder.setItems(R.array.settings, new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int which) {
                               switch (which) {
                                   case 0:
                                       Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                       startActivity(intent);
                                       break;
                                   case 1:
                                       LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                                       boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                                       if (statusOfGPS == false)
                                           Toast.makeText(ProfileActivity.this, "GPS IS DISABLED!",
                                                   Toast.LENGTH_LONG).show();

                                       else {
                                           Toast.makeText(ProfileActivity.this, "GPS IS ENABLED!",
                                                   Toast.LENGTH_LONG).show();
                                       }
                                       break;

                                   case 2:
                                       AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                       builder.setMessage("Do you want to log out from this application ?")
                                               .setCancelable(false)
                                               .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                   public void onClick(DialogInterface dialog, int id) {
                                                       SessionManager sessionManager = new SessionManager(ProfileActivity.this, SessionManager.Session_user);
                                                       sessionManager.LogoutUserFromSession();
                                                       Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
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
                                       break;
                                   case 3:
                                       try {


                                           userHelper.DeleteUser(user.getEmail().trim());
                                           SessionManager sessionManager = new SessionManager(ProfileActivity.this, SessionManager.Session_user);
                                           sessionManager.LogoutUserFromSession();
                                           Intent fin = new Intent(ProfileActivity.this, LoginActivity.class);
                                           startActivity(fin);
                                           finish();

                                       } catch (Exception e) {
                                           Toast.makeText(ProfileActivity.this, "Failed to delete",
                                                   Toast.LENGTH_LONG).show();
                                           break;

                                       }

                               }
                           }
                       });
                       builder.show();});
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