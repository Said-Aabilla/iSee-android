package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iSee.R;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        TextView usernane=findViewById(R.id.user_name);
        usernane.setText("Hi "+getIntent().getStringExtra("fullname").toString()+" !");

    }
}