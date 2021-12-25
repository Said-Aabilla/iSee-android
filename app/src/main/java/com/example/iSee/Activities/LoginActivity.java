package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.iSee.Controllers.ILoginController;
import com.example.iSee.Controllers.ISignupController;
import com.example.iSee.Controllers.LoginController;
import com.example.iSee.Controllers.SignupController;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Views.ILoginView;
import com.example.iSee.Views.ISignupView;


public class LoginActivity extends AppCompatActivity implements ILoginView {

//Global variables
    ILoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginController=new LoginController(this);
//initialisation


        final EditText emailEdit = findViewById(R.id.user_email);
        final EditText passwordEdit = findViewById(R.id.user_password);

        (findViewById(R.id.login_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);*/
                loginController.onLogin(emailEdit.getText().toString().trim(),passwordEdit.getText().toString().trim());

            }
        });

        (findViewById(R.id.signup_btn)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
        (findViewById(R.id.IconBack)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent=new Intent(LoginActivity.this, MainActivity.class);
              startActivity(intent);

            }
        });


    }

    @Override
    public void onLoginSuccess(String message,User user)  {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
        Intent intent=new Intent(this,HomeActivity.class);

        intent.putExtra("fullname",user.getFullname());
        intent.putExtra("email",user.getEmail());
        intent.putExtra("vision",user.getVision());
        intent.putExtra("language",user.getLanguage());

        startActivity(intent);

    }

    @Override
    public void onLoginFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }
}