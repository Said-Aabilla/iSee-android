package com.example.iSee.Views;

public interface ISignupView {
    void onSignupSuccess(String message) throws InterruptedException;
    void onSignupFailed(String message);
}
