package com.example.iSee.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.iSee.Controllers.ISignupController;
import com.example.iSee.Controllers.SignupController;
import com.example.iSee.R;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Views.ISignupView;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class SignupActivity extends AppCompatActivity implements ISignupView {
//variables
    ISignupController signupController;


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
        final boolean vision = getIntent().getExtras().getBoolean("vision");


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

               /* Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);*/


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
    public void onSignupSuccess(String message)  {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
       // Thread.sleep(5000);
          Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);
    }

    @Override
    public void onSignupFailed(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();

    }

    //uses functions...



}