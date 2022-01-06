package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;

import com.example.iSee.R;

public class ProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        (findViewById(R.id.logout_btn)).setOnClickListener(view ->{
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
        });
    }
}