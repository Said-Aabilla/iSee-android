package com.example.iSee.Views;

public interface ILoginView {
   void onLoginSuccess(String message, String email);
   void onLoginFailed(String message);
}
