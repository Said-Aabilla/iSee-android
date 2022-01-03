package com.example.iSee.Models;

import android.text.TextUtils;
import android.util.Patterns;

public class User implements IUser{

   private String fullname, email,password;
   private String langage;
   private boolean vision;
   private double latitude,longitude;

   public User(String email,String password) {
        this.email=email;
        this.password=password;
    }

    public User(String email, String password, String fullname, String langage, boolean vision) {
       this.email=email;
       this.password=password;
       this.fullname=fullname;
       this.langage=langage;
       this.vision=vision;
    }

    public User(String email, double latitude, double longitude) {
       this.email=email;
       this.latitude=latitude;
       this.longitude=longitude;
    }


    @Override
      public String getFullname() {
          return fullname;
      }
    @Override
    public boolean getVision() {
        return vision;
    }
    @Override
    public String getLanguage() {
        return langage;
    }
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }
    @Override
    public double getLatitude() {
        return latitude;
    }
    @Override
    public double getLongitude() {return longitude; }

    @Override
    public int isValid() {
if(TextUtils.isEmpty(getEmail())){
return 0;
}
else if (!Patterns.EMAIL_ADDRESS.matcher(getEmail()).matches()){
    return 1;
}
else if (TextUtils.isEmpty(getPassword())){
            return 2;
        }
else if(getPassword().length()<6){
    return 3;

        }
else
    return -1;
    }
}
