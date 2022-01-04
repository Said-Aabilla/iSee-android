package com.example.iSee.Controllers.impl;

import com.example.iSee.Activities.HomeVolunteerActivity;
import com.example.iSee.Controllers.facade.IUpdateLocController;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Services.RetrofitService;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateLocController implements IUpdateLocController {

    Retrofit retrofit = RetrofitService.getRetrofitInstance();
    IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);

    public UpdateLocController(HomeVolunteerActivity homeVolunteerActivity) {

    }

    @Override
    public void onUpdateLocalisation(String email, double latitude,double longitude) {
        final User user=new User(email,latitude,longitude);
        Map<String, String> map = new HashMap<>() ;
        map.put("email", email);
        map.put("latitude", String.valueOf(latitude));
        map.put("longitude", String.valueOf(longitude));
                Call<Void> call = retrofitInterface.executeUpdate(map);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                    }
                });
            }
}
