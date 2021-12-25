package com.example.iSee.Views;

import com.example.iSee.Models.User;

public interface ILoginView {
   void onLoginSuccess(String message, User user);
   void onLoginFailed(String message);
}
