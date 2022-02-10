package com.example.iSee.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.Controllers.facade.IDeleteController;
import com.example.iSee.Controllers.impl.DeleteController;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;

public class SettingsActivity extends AppCompatActivity {
    IDeleteController deleteController;
    BottomNavigationItemView homeItem;
    BottomNavigationItemView profileItem;
    UserDbHelper userHelper = new UserDbHelper(this);




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//Get the user from SQLite
        String email=getIntent().getStringExtra("email");
        User user=userHelper.getUser(email);

        final Button b = findViewById(R.id.button01);
        final Button b2 = findViewById(R.id.button02);
        final Button b3 = findViewById(R.id.button03);
        profileItem = findViewById(R.id.profileItem);
        homeItem = findViewById(R.id.dashboardItem);


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });


        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (statusOfGPS == false)
                    Toast.makeText(SettingsActivity.this, "GPS IS DISABLED!",
                            Toast.LENGTH_LONG).show();
                else Toast.makeText(SettingsActivity.this, "GPS IS ENABLED!",
                        Toast.LENGTH_LONG).show();
            }
        });


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               try {
                   deleteController.onDelete(user.getEmail().trim());
                   Intent fin = new Intent(SettingsActivity.this,LoginActivity.class);
                   startActivity(fin);
               }  catch(Exception e) {
                   Toast.makeText(SettingsActivity.this, e.toString(),
                           Toast.LENGTH_LONG).show();

            }

            }
        });

        profileItem.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("email",user.getEmail().trim());
            startActivity(profileIntent);
        });
        homeItem.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, HomeVolunteerActivity.class);
            homeIntent.putExtra("email",user.getEmail().trim());

            startActivity(homeIntent);

        });


    }

}









