package com.example.iSee.Models;

import android.text.TextUtils;
import android.util.Patterns;

public class User implements IUser{

   private String fullname, email,password;
   private String langage;
   private String vision;
   private double latitude,longitude;

   public User(String email,String password) {
        this.email=email;
        this.password=password;
    }

    public User(String email, String password, String fullname, String langage, String vision) {
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

    public User() {

    }

    public User(String aTrue) {
       this.vision=aTrue;
    }


    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLangage(String langage) {
        this.langage = langage;
    }

    public void setVision(String vision) {
        this.vision = vision;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
      public String getFullname() {
          return fullname;
      }
    @Override
    public String getVision() {
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
