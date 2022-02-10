package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;


public class SuccessActivity extends AppCompatActivity {

    UserDbHelper userHelper = new UserDbHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        User user=userHelper.getUser(getIntent().getStringExtra("email").trim());


        (findViewById(R.id.start)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getVision().equals("true")){
                    Intent intent=new Intent(getApplicationContext(), HomeVolunteerActivity.class);
                    intent.putExtra("email",getIntent().getStringExtra("email").trim());
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(getApplicationContext(), HomeImpairedActivity.class);
                    intent.putExtra("email",getIntent().getStringExtra("email").trim());
                    startActivity(intent);
                }

            }
        });

        (findViewById(R.id.back_to_welcome)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });


    }
}