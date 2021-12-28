package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.iSee.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        final Switch s = findViewById(R.id.switch01);
        final LinearLayout l = findViewById(R.id.l1);
        final Button b = findViewById(R.id.b01);


        s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (s.isChecked()) {

                    l.setVisibility(View.VISIBLE);

                } else {
                    l.setVisibility(View.GONE);
                }

            }
        });
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

    }


}