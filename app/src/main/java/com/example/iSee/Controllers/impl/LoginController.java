package com.example.iSee.Controllers.impl;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iSee.Controllers.facade.ILoginController;
import com.example.iSee.Models.User;
import com.example.iSee.Views.ILoginView;


public class LoginController implements ILoginController {
//Parameters & globals variables

//Attributs of this class
    ILoginView loginView;



    //Constructor
    public LoginController(ILoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public boolean onLogin(String email, String password) {
        final User user = new User(email,password);
        int signupcode = user.isValid();
        if (signupcode == 0) {
            loginView.onLoginFailed("enter the email");
            return false;

        } else if (signupcode == 1) {
            loginView.onLoginFailed("Incorrect email !!");
            return false;

        } else if (signupcode == 2) {
            loginView.onLoginFailed("Enter the password !");
            return false;

        } else if (signupcode == 3) {
            loginView.onLoginFailed("Le mot de passe necessite au moins 6 caractères !");
            return false;
        } else {
            return true;
        }
//                     loginView.onLoginSuccess("Login Success !", result);
        }
    }


