package com.example.iSee.Models;

public interface IUser {
    String getEmail();
    String getPassword();
    String getFullname();
    String getVision();
    String getLanguage();
    double getLatitude();
    double getLongitude();
    int isValid();



}