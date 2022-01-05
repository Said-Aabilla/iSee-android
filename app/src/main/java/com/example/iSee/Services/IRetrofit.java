package com.example.iSee.Services;

import com.example.iSee.Models.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface IRetrofit {
    //public static String userId = null;

    @POST("/users/signin")
    Call<User> executeLogin(@Body HashMap<String,String>map);
    @POST("/users/signup")
    Call<Void> executeSignup(@Body Map<String,String> map);
    @POST("/users/update")
    Call<Void> executeUpdate(@Body Map<String,String> map);
    @DELETE("/users/delete")
    Call<Void> executeDelete(@Body Map<String,String> email);
    @POST("/users/close")
    Call<List<User>> getCloseUsers(@Body HashMap<String, Double> map);
    @POST("/users/close/language")
    Call<List<User>> getCloseUsersByLanguage(@Body HashMap<String, List<String>> map);
}


