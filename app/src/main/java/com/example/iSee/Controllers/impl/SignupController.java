package com.example.iSee.Controllers.impl;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.iSee.Activities.SignupActivity;
import com.example.iSee.Controllers.facade.ISignupController;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Services.RetrofitService;
import com.example.iSee.Views.ISignupView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignupController implements ISignupController {
//Parameters & global variables
  Retrofit retrofit = RetrofitService.getRetrofitInstance();
     IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);
    Map<String, String> map = new HashMap<>() ;

//Attributs of this class

    ISignupView signupView;
//Constructor
    public SignupController(ISignupView signupView ) {
        this.signupView=signupView;
    }

    @Override
    public void onSignup(String email, String password,String fullname,String langage,String vision) {
        final User user=new User(email,password,fullname,langage,vision);
        int signupcode=user.isValid();
        if (signupcode==0){
            signupView.onSignupFailed("enter the email");
        }else if (signupcode==1){
            signupView.onSignupFailed("Incorrect email !!");
        }
        else if (signupcode==2){
            signupView.onSignupFailed("Enter the password !");
        }
        else if (signupcode==3){
            signupView.onSignupFailed("Le mot de necessite au moins 6 caractères !");
        }
        else {
            map.put("email", email);
            map.put("password", password);
            map.put("fullname",fullname);
            map.put("langage",langage);
            map.put("vision",String.valueOf(vision));
            Call<Void> call = retrofitInterface.executeSignup(map);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 200) {
                        try {
                            signupView.onSignupSuccess("Signup Success !",email);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /* Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);*/
                    } else if (response.code() == 409) {
                        signupView.onSignupFailed(" Already exist ! Try login below !");
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {

                }
            });
        }

    }
}
