package com.example.iSee.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.Controllers.ILoginController;
import com.example.iSee.Controllers.LoginController;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Views.ILoginView;


public class LoginActivity extends AppCompatActivity implements ILoginView {

    //Global variables
    ILoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController = new LoginController(this);
//initialisation


        final EditText emailEdit = findViewById(R.id.user_email);
        final EditText passwordEdit = findViewById(R.id.user_password);

        (findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);*/
                loginController.onLogin(emailEdit.getText().toString().trim(), passwordEdit.getText().toString().trim());

            }
        });

        (findViewById(R.id.signup_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.IconBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });


    }

    @Override
    public void onLoginSuccess(String message, User user) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        Intent volunteerIntent = new Intent(this, HomeVolunteerActivity.class);
        Intent visuallyImpairedIntent = new Intent(this, HomeImpairedActivity.class);

        visuallyImpairedIntent.putExtra("fullname",user.getFullname());
        visuallyImpairedIntent.putExtra("email",user.getEmail());
        visuallyImpairedIntent.putExtra("vision",user.getVision());
        visuallyImpairedIntent.putExtra("language",user.getLanguage());
        volunteerIntent.putExtra("fullname",user.getFullname());
        volunteerIntent.putExtra("email",user.getEmail());
        volunteerIntent.putExtra("vision",user.getVision());
        volunteerIntent.putExtra("language",user.getLanguage());

        if (user.getVision()) {
            startActivity(volunteerIntent);
        }else if (!user.getVision()){
            startActivity(visuallyImpairedIntent);
        }
    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}