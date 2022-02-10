package com.example.iSee.Services;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {

    SharedPreferences userSession;
    SharedPreferences.Editor editor;
    Context context;

    public static final String Session_user="userLoginSession";
    public static final String Rememberme_session="rememberMe";

//Login Session
    private static final String IS_LOGIN="isloggedin";
    private static final String KEY_FULLNAME="fullname";
    private static final String KEY_EMAIL="email";
    private static final String KEY_PASSWORD="password";
    private static final String KEY_LANGUAGE="language ";
    private static final boolean KEY_VISION=false;

    //Remember Me variables
    public static final String IS_REMEMBERME="isrememberme";
    public static final String KEY_EMAILREMEMBERME="email";
    public static final String KEY_PASSWORDREMEMBERME="password";

    public SessionManager(Context _context,String sessionName) {
        context=_context;
     userSession=context.getSharedPreferences(sessionName,Context.MODE_PRIVATE);
  editor=userSession.edit();
        /**
         * Sign up session
         */
    }
    public void createLoginSession(String email,String password){


        editor.putBoolean(IS_LOGIN,true);
        editor.putString(KEY_EMAIL,email);
        editor.putString(KEY_PASSWORD,password);
        editor.commit();
    }
    public HashMap <String,String> getUsersDetailFromSession(){

        HashMap<String,String>userData=new HashMap<String,String>();
        userData.put(KEY_FULLNAME,userSession.getString(KEY_FULLNAME,null));
        userData.put(KEY_EMAIL,userSession.getString(KEY_EMAIL,null));
        userData.put(KEY_PASSWORD,userSession.getString(KEY_PASSWORD,null));
        userData.put(KEY_LANGUAGE,userSession.getString(KEY_LANGUAGE,null));
       return  userData;
    }
    public String getEmailFromSession(){
        return userSession.getString(KEY_EMAIL,null);
    }
    public boolean CheckLogin(){
        if (userSession.getBoolean(IS_LOGIN,false)){
            return true;
        }else
            return false;

    }
    public void LogoutUserFromSession(){
editor.clear();
editor.commit();

    }
    /**
     * Log in Session
     */
    public void createRememberMeSession(String email, String password){


        editor.putBoolean(IS_REMEMBERME,true);
        editor.putString(KEY_EMAILREMEMBERME,email);
        editor.putString(KEY_PASSWORDREMEMBERME,password);
        editor.commit();
    }

    public HashMap <String,String> getRemembermeDetailsFromSession(){

        HashMap<String,String>rememberData=new HashMap<String,String>();
        rememberData.put(KEY_EMAILREMEMBERME,userSession.getString(KEY_EMAILREMEMBERME,null));
        rememberData.put(KEY_PASSWORDREMEMBERME,userSession.getString(KEY_PASSWORDREMEMBERME,null));
        return  rememberData;
    }

    public boolean CheckRememberME(){
        if (userSession.getBoolean(IS_REMEMBERME,false)){
            return true;
        }else
            return false;

    }




}
