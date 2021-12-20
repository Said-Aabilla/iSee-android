package com.example.iSee.Models;

public class LoginResult implements IUser {

    private String email;

    private String password;

    public String getPassword() {
        return password;
    }

    @Override
    public int isValid() {
        return 0;
    }

    public String getEmail() {
        return email;
    }
}