package com.example.iSee.Controllers;

import com.example.iSee.Models.LoginResult;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Views.ILoginView;
import java.security.cert.CertificateException;
import java.util.HashMap;
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


public class LoginController implements ILoginController {
//Parameters & globals variables
String BASE_URL="http://10.0.2.2:3000";
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getUnsafeOkHttpClient().build())
            .build();
    IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);
//Attributs of this class
    ILoginView loginView;


//Constructor
    public LoginController(ILoginView loginView) {
        this.loginView = loginView;
    }


    @Override
    public void onLogin(String email, String password) {
        User user = new User(email, password);
        int signupcode = user.isValid();
        if (signupcode == 0) {
            loginView.onLoginFailed("enter the email");
        } else if (signupcode == 1) {
            loginView.onLoginFailed("Incorrect email !!");
        } else if (signupcode == 2) {
            loginView.onLoginFailed("Enter the password !");
        } else if (signupcode == 3) {
            loginView.onLoginFailed("Le mot de passe necessite au moins 6 caractères !");
        } else {

            HashMap<String, String> map = new HashMap<>();
            map.put("email", email);
            map.put("password", password);

            Call<LoginResult> call = retrofitInterface.executeLogin(map);

            call.enqueue(new Callback<LoginResult>() {
                @Override
                public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {

                    if (response.code() == 200) {
                        LoginResult result = response.body();
                        loginView.onLoginSuccess("Login Success !");


                    }
                     else if (response.code() == 409) {
                        loginView.onLoginFailed(" User not found ! Try Signup !!");
                    }
                }

                @Override
                public void onFailure(Call<LoginResult> call, Throwable t) {

                }
            });

        }
    }

 //uses functions
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
