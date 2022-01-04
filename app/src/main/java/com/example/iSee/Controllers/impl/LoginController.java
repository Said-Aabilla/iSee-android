package com.example.iSee.Controllers.impl;

import com.example.iSee.Controllers.facade.ILoginController;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Services.RetrofitService;
import com.example.iSee.Views.ILoginView;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class LoginController implements ILoginController {
//Parameters & globals variables

    Retrofit retrofit = RetrofitService.getRetrofitInstance();
    IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);
//Attributs of this class
    ILoginView loginView;


//Constructor
    public LoginController(ILoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void onLogin(String email, String password) {
        final User user = new User(email, password);
        int signupcode = user.isValid();
        if (signupcode == 0) {
            loginView.onLoginFailed("enter the email");
        } else if (signupcode == 1) {
            loginView.onLoginFailed("Incorrect email !!");
        } else if (signupcode == 2) {
            loginView.onLoginFailed("Enter the password !");
        } else if (signupcode == 3) {
            loginView.onLoginFailed("Le mot de passe necessite au moins 6 caractères !");
        } else {

            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("password", password);

            Call<User> call = retrofitInterface.executeLogin(map);

            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                    if (response.code() == 200) {
                        User result = response.body();
                        loginView.onLoginSuccess("Login Success !", result);

                    }
                     else if (response.code() == 409) {
                        loginView.onLoginFailed(" User not found ! Try Signup !!");
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });

        }
    }

}
