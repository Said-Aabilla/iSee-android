package com.example.iSee.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;



public class SettingsActivity extends AppCompatActivity {
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

        profileItem = findViewById(R.id.profileItem);
        homeItem = findViewById(R.id.dashboardItem);

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









