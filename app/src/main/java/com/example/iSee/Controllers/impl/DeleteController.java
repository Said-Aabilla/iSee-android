package com.example.iSee.Controllers.impl;



import com.example.iSee.Controllers.facade.IDeleteController;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Services.RetrofitService;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DeleteController implements IDeleteController {

    //Parameters & globals variables
    Retrofit retrofit = RetrofitService.getRetrofitInstance();
    IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);
    //Attributs of this class




    @Override
    public void onDelete(String email) {



            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);


            Call<Void> call = retrofitInterface.executeDelete(map);

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
