package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.iSee.R;


public class CheckvisionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkvision_activity);

        findViewById(R.id.vision_true).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(CheckvisionActivity.this,SignupActivity.class);
                intent.putExtra("vision","true");
                startActivity(intent);

            }
        });
        findViewById(R.id.vision_false).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(CheckvisionActivity.this,SignupActivity.class);
                intent.putExtra("vision","false");
                startActivity(intent);

            }
        });

    }

}
