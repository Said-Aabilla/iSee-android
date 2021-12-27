package com.example.iSee.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import com.example.iSee.R;
import com.example.iSee.Views.ICallVIew;
import com.example.iSee.Views.WebJavascriptInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class HomeVolunteerActivity extends AppCompatActivity implements ICallVIew {

    WebView webView;
    ConstraintLayout initialLayout;
    BottomNavigationView navMenu;
    RelativeLayout callLayout;
    LinearLayout callControlLayout;
    TextView incomingCallText;
    ImageView toggleAudioBtn;
    ImageView toggleVideoBtn;
    ImageView acceptBtn;
    ImageView rejectBtn;
    String username = "";
    String connId = "";
    boolean isAudio = true;
    boolean isVideo = true;
    boolean isPeerConnected = true;
    DatabaseReference fireBaseRef = FirebaseDatabase.getInstance().getReference("users");


    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);

        webView = findViewById(R.id.webview);
        callControlLayout = findViewById(R.id.callControlLayout);
        callLayout = findViewById(R.id.callLayout);
        incomingCallText = findViewById(R.id.incomingCallText);
        toggleAudioBtn = findViewById(R.id.toggleAudioBtn);
        toggleVideoBtn = findViewById(R.id.toggleVideoBtn);
        acceptBtn = findViewById(R.id.acceptBtn);
        rejectBtn = findViewById(R.id.rejectBtn);
        navMenu = findViewById(R.id.Bottom_menu);
        initialLayout = findViewById(R.id.initialLayout);

        TextView usernameText=findViewById(R.id.user_name);
        usernameText.setText("Hi "+getIntent().getStringExtra("fullname")+" !");
        username= Objects.requireNonNull(getIntent().getStringExtra("fullname")).trim();

        findViewById(R.id.toggleAudioBtn).setOnClickListener(v -> {
            isAudio = !isAudio;
            webView.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    String toggleAudFunc = "javascript:toggleAudio" + "(" + "\"" + isAudio + "\"" + ")";
                    webView.evaluateJavascript(toggleAudFunc, null);
                }
            });
            if (isAudio) toggleAudioBtn.setImageResource(R.drawable.ic_baseline_mic_24);
            else toggleAudioBtn.setImageResource(R.drawable.ic_baseline_mic_off_24);
        });
        findViewById(R.id.toggleVideoBtn).setOnClickListener(v -> {
            isVideo = !isVideo;
            webView.post(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    String toggleVidFunc = "javascript:toggleVideo" + "(" + "\"" + isVideo + "\"" + ")";
                    webView.evaluateJavascript(toggleVidFunc, null);
                }
            });
            if (isVideo) toggleVideoBtn.setImageResource(R.drawable.ic_baseline_videocam_24);
            else toggleVideoBtn.setImageResource(R.drawable.ic_baseline_videocam_off_24);
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
                fireBaseRef.child(username).child("incoming").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        onCallRequest((String) snapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

    }

    private void onCallRequest(String caller) {
        if (caller == null) return;
        callLayout.setVisibility(View.VISIBLE);
        incomingCallText.setText(caller + " is calling !");

        acceptBtn.setOnClickListener(v -> {
            fireBaseRef.child(username).child("connId").setValue(connId);
            fireBaseRef.child(username).child("isAvailable").setValue(true);
            //Switch Controls
            navMenu.setVisibility(View.GONE);
            initialLayout.setVisibility(View.GONE);
            callLayout.setVisibility(View.GONE);
            callControlLayout.setVisibility(View.VISIBLE);
        });
        rejectBtn.setOnClickListener(v -> {
            fireBaseRef.child(username).child("incoming").setValue(null);
            callLayout.setVisibility(View.GONE);
        });
    }


    private String getUniqueId() {
        return UUID.randomUUID().toString();
    }


    @Override
    public void onPeerConnected() {
        isPeerConnected = true;
    }

    @Override
    public void onBackPressed() {
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