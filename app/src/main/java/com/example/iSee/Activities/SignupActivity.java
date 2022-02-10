package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iSee.Controllers.facade.ISignupController;
import com.example.iSee.Controllers.impl.SignupController;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.R;
import com.example.iSee.Views.ISignupView;

import java.util.ArrayList;


public class SignupActivity extends AppCompatActivity implements ISignupView {
//variables
    ISignupController signupController;
    UserDbHelper userHelper = new UserDbHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signupController = new SignupController(this);


//initialisation


        final EditText emailEdit = findViewById(R.id.user_email);
        final EditText passwordEdit = findViewById(R.id.user_password);
        final EditText firstnameEdit = findViewById(R.id.user_firstName);
        final EditText lastnameEdit = findViewById(R.id.user_lastName);
        Spinner spinner = (Spinner) findViewById(R.id.myspinner);
        final String langage = spinner.getSelectedItem().toString();
        final String vision = getIntent().getExtras().getString("vision");


        (findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.signup_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupController.onSignup(emailEdit.getText().toString().trim(), passwordEdit.getText().toString().trim(),
                        lastnameEdit.getText().toString().trim() + " " + firstnameEdit.getText().toString().trim(), langage, vision);
                try {
                    userHelper.InsertUser(lastnameEdit.getText().toString().trim() + " " + firstnameEdit.getText().toString().trim(),
                            emailEdit.getText().toString().trim(),passwordEdit.getText().toString().trim(),langage,vision);
                    Toast.makeText(SignupActivity.this,"Local database updated",Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        });
        (findViewById(R.id.back_img_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onSignupSuccess(String message,String email)  {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
       // Thread.sleep(5000);
          Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
          intent.putExtra("email",email);
                startActivity(intent);
    }

    @Override
    public void onSignupFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }
}