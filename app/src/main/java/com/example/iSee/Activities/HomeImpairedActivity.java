package com.example.iSee.Activities;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;


import com.example.iSee.R;
import com.example.iSee.Views.ICallVIew;
import com.example.iSee.Views.WebJavascriptInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class HomeImpairedActivity extends AppCompatActivity implements ICallVIew {

    WebView webView;
    ConstraintLayout initialLayout;
    BottomNavigationView navMenu;
    LinearLayout callControlLayout;
    String username = "";
    String connId = "";
    boolean isPeerConnected = true;
    boolean isUserAvailable = false;
    List<String> availableUsers;

    DatabaseReference fireBaseRef = FirebaseDatabase.getInstance().getReference("users");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_home);


        webView = findViewById(R.id.webview);
        navMenu = findViewById(R.id.Bottom_menu);
        initialLayout = findViewById(R.id.initialLayout);
        callControlLayout = findViewById(R.id.callControlLayout);

        username= Objects.requireNonNull(getIntent().getStringExtra("fullname")).trim();

        findViewById(R.id.callBtn).setOnClickListener(v -> {

            availableUsers = new ArrayList<>();
            availableUsers.add("ismail");
            availableUsers.add("hajar");
            availableUsers.add("nabil");
            availableUsers.add("reda");

            for (int i = 0; i<= availableUsers.size(); i++){
                if (isUserAvailable) break;
                try {
                    fireBaseRef.child(availableUsers.get(i-1)).child("incoming").setValue(null);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "testing " + i, Toast.LENGTH_SHORT).show();
                try {
                    sendCallRequest(availableUsers.get(i));
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        setUpWebview();

    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setUpWebview() {
        webView.setWebChromeClient(new WebChromeClient() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onPermissionRequest(PermissionRequest request) {
                request.grant(request.getResources());
            }
        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        webView.addJavascriptInterface(new WebJavascriptInterface(this), "Android");
        loadVideoCall();
    }

    private void loadVideoCall() {
        webView.loadUrl("file:android_asset/call.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                initializePeer();
            }
        });
    }

    private void initializePeer() {
        webView.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void run() {
                connId = getUniqueId();
                String func = "javascript:init" + "(" + "\"" + connId + "\"" + ")";
                webView.evaluateJavascript(func, null);
            }
        });
    }

    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }

    private void sendCallRequest(String friendUsername) {
        if (!isPeerConnected) {
            Toast.makeText(getApplicationContext(), "Check your internet connexion!", Toast.LENGTH_SHORT).show();
        }

        fireBaseRef.child(friendUsername).child("incoming").setValue(username);
        fireBaseRef.child(friendUsername).child("isAvailable").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean result =  snapshot.getValue(Boolean.class);
                if (result!=null && result) {
                    isUserAvailable = true;
                    Toast.makeText(getApplicationContext(), "Call Accepted", Toast.LENGTH_SHORT).show();
                    listenForConnId(friendUsername);
                }else {
                    Toast.makeText(getApplicationContext(), "Call ended", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenForConnId(String friendUsername) {
        fireBaseRef.child(friendUsername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(), "I'm here!", Toast.LENGTH_SHORT).show();
                if ((String) snapshot.getValue() == null){
                    return;
                }

                //Call js
                webView.post(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void run() {
                        //SWitch controls
                        navMenu.setVisibility(View.GONE);
                        initialLayout.setVisibility(View.GONE);
                        callControlLayout.setVisibility(View.VISIBLE);

                        String call = "javascript:startCall" + "(" + "\"" + (String) snapshot.getValue() + "\"" + ")";
                        webView.evaluateJavascript(call, null);
                        isUserAvailable = true;
                        Toast.makeText(getApplicationContext(), "started call", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public void onPeerConnected() {
        isPeerConnected = true;
    }

    @Override
    public void onBackPressed() {
        navMenu.setVisibility(View.VISIBLE);
        initialLayout.setVisibility(View.VISIBLE);
        fireBaseRef.child(username).removeValue();
        finish();
    }

    @Override
    protected void onDestroy() {
        fireBaseRef.child(username).setValue(null);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }
}
