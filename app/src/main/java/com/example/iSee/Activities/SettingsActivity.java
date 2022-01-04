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

import com.example.iSee.Controllers.impl.DeleteController;
import com.example.iSee.R;

public class SettingsActivity extends AppCompatActivity {
    DeleteController deleteController;
    // Bundle extra =this.getIntent().getExtras();
    //String userEmail = extra.getString("email");
    String userEmail = "jimouhsami@gmail.com";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        final Button b = findViewById(R.id.button01);
        final Button b2 = findViewById(R.id.button02);
        final Button b3 = findViewById(R.id.button03);


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
                deleteController.onDelete("jimouhsami@gmail.com");

            }
        });


    }

}









