package com.example.iSee.Controllers;
import com.example.iSee.Models.User;
import com.example.iSee.Services.IRetrofit;
import com.example.iSee.Views.ISignupView;
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

public class SignupController implements ISignupController {
//Parameters & globals variables
  String BASE_URL="http://10.0.2.2:3000";
  Retrofit retrofit = new Retrofit.Builder()
         .baseUrl(BASE_URL)
         .addConverterFactory(GsonConverterFactory.create())
         .client(getUnsafeOkHttpClient().build())
         .build();
     IRetrofit retrofitInterface= retrofit.create(IRetrofit.class);
    Map<String, String> map = new HashMap<>() ;
//Attributs of this class

    ISignupView signupView;
//Constructor
    public SignupController(ISignupView signupView ) {
        this.signupView=signupView;
    }

    @Override
    public void onSignup(String email, String password) {
        User user=new User(email,password);
        int signupcode=user.isValid();
        if (signupcode==0){
            signupView.onSignupFailed("enter the email");
        }else if (signupcode==1){
            signupView.onSignupFailed("Incorrect email !!");
        }
        else if (signupcode==2){
            signupView.onSignupFailed("Enter the password !");
        }
        else if (signupcode==3){
            signupView.onSignupFailed("Le mot de necessite au moins 6 caractères !");
        }
        else {
            map.put("email", email);
            map.put("password", password);
            Call<Void> call = retrofitInterface.executeSignup(map);
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {

                    if (response.code() == 200) {
                        try {
                            signupView.onSignupSuccess("Signup Success !");
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        /* Intent intent=new Intent(getApplicationContext(), SuccessActivity.class);
                startActivity(intent);*/
                    } else if (response.code() == 409) {
                        signupView.onSignupFailed(" Already exist ! Try login below !");
                    }
                }
                @Override
                public void onFailure(Call<Void> call, Throwable t) {

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
