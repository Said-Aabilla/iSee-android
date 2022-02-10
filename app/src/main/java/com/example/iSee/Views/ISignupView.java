package com.example.iSee.Views;

public interface ISignupView {
    void onSignupSuccess(String message,String email) throws InterruptedException;
    void onSignupFailed(String message);
}
