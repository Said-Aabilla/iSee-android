package com.example.iSee.Controllers.impl;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.iSee.Controllers.facade.ICloseUsersController;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Services.RetrofitService;
import com.example.iSee.Views.IHomeImpairedView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;


public class CloseUsersController implements ICloseUsersController {

    IHomeImpairedView homeImpairedView;

    Retrofit retrofit = RetrofitService.getRetrofitInstance();

    IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);

    public CloseUsersController(IHomeImpairedView homeImpairedView) {
        this.homeImpairedView = homeImpairedView;
    }

    @Override
    public void onGetCloseUsers( double latitude, double longitude) {
        HashMap<String, Double> map = new HashMap<>();
        map.put("latitude", latitude);
        map.put("longitude", longitude);

        Call<List<User>> call = retrofitInterface.getCloseUsers(map);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.code() == 200) {
                    List<User> result =response.body();
                    homeImpairedView.onGetCloseUsers(result);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

    @Override
    public void onGetCloseUsersByLang(List<String> languages) {
        HashMap<String, List<String>> map = new HashMap<>();
        map.put("languages", languages);

        Call<List<User>> call = retrofitInterface.getCloseUsersByLanguage(map);

        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(@NonNull Call<List<User>> call, @NonNull Response<List<User>> response) {
                if (response.code() == 200) {
                    List<User> result =response.body();
                    homeImpairedView.onGetCloseUsers(result);
                }

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {

            }
        });
    }

}
