package com.example.iSee.Services;

import com.example.iSee.Models.User;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRetrofit {

    @POST("/users/signin")
    Call<User> executeLogin(@Body HashMap<String,String>map);
    @POST("/users/signup")
    Call<Void> executeSignup(@Body Map<String,String> map);
}
