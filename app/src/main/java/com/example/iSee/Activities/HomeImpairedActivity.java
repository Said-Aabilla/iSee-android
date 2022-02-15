package com.example.iSee.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.iSee.Controllers.facade.ICloseUsersController;
import com.example.iSee.Controllers.impl.CloseUsersController;
import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.example.iSee.Views.ICallVIew;
import com.example.iSee.Views.IHomeImpairedView;
import com.example.iSee.Views.WebJavascriptInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class HomeImpairedActivity extends AppCompatActivity implements ICallVIew, IHomeImpairedView {


    private static final String TAG = "HomeImpairedActivity";
    int LOCATION_REQUEST_CODE = 10001;
    UserDbHelper userHelper = new UserDbHelper(this);
    FusedLocationProviderClient fusedLocationProviderClient;


    //    UI CALL HELPER VARIABLES
    ProgressBar progressBar;
    Button locationCallBtn;
    Button normalCallBtn;
    WebView webView;
    LinearLayout callControlLayout;
    ConstraintLayout initialLayout;
    BottomNavigationView navMenu;
    String username = "";
    List<String> languages = new ArrayList<>();
    public static Handler handler = new Handler();
    String connId = "";
    boolean isPeerConnected = true;
    boolean isUserAvailable = false;
    ICloseUsersController closeUsersController;
    List<User> availableUsers = new ArrayList<>();

    DatabaseReference fireBaseRef = FirebaseDatabase.getInstance().getReference("users");

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_home);
// Get the user from SQLite
        String email = getIntent().getStringExtra("email");
        User user = userHelper.getUser(email);

        //localisation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //get close users
        closeUsersController = new CloseUsersController(this);


        webView = findViewById(R.id.webview);
        navMenu = findViewById(R.id.Bottom_menu);
        initialLayout = findViewById(R.id.initialLayout);
        progressBar = findViewById(R.id.progressBar);
        normalCallBtn = findViewById(R.id.normalcallBtn);
        locationCallBtn = findViewById(R.id.locationCallBtn);
        callControlLayout = findViewById(R.id.callControlLayout);

        username = Objects.requireNonNull(user.getFullname()).trim();
        languages.add(Objects.requireNonNull(user.getLanguage()).trim());

        locationCallBtn.setOnClickListener(v -> {
            //showProgressBar();
            getLocationCloseUsers();
            final Runnable r = new Runnable() {
                public void run() {
                    tryOutCalls();
                }
            };
            handler.postDelayed(r, 3000);
            // hideProgressBar();
        });
        normalCallBtn.setOnClickListener(v -> {
            //showProgressBar();
            closeUsersController.onGetCloseUsersByLang(languages);
            final Runnable r = new Runnable() {
                public void run() {
                    tryOutCalls();
                }
            };
            handler.postDelayed(r, 2000);

            //hideProgressBar();
        });

        findViewById(R.id.endCallBtn).setOnClickListener(view -> {
            finish();
            startActivity(getIntent());
        });

    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        locationCallBtn.setVisibility(View.GONE);
        normalCallBtn.setVisibility(View.GONE);
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
        locationCallBtn.setVisibility(View.VISIBLE);
        normalCallBtn.setVisibility(View.VISIBLE);
    }

    private void getLocationCloseUsers() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            @SuppressLint("MissingPermission")
            Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
            locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        closeUsersController.onGetCloseUsers(location.getLatitude(), location.getLongitude());
                    }
                }
            });
            locationTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e(TAG, e.getLocalizedMessage());
                }
            });
        } else {
            askLocationPermission();
        }
    }

    @SuppressLint("LongLogTag")
    private void tryOutCalls() {
        Log.e(TAG, "hani anbda les appels");
        for (int i = 0; i <= availableUsers.size(); i++) {
            if (isUserAvailable) {
                //progressBar.setVisibility(View.GONE);
                break;
            }
            try {
                fireBaseRef.child(availableUsers.get(i - 1).getFullname().trim()).child("incoming").setValue(null);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(getApplicationContext(), "testing " + i, Toast.LENGTH_SHORT).show();
            try {
                Log.e("SENDING CAAAAAAAAAAAAAAAAAL", availableUsers.get(i).getEmail());
                sendCallRequest(availableUsers.get(i).getFullname().trim());
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onStart() {
        setUpWebview();
        super.onStart();

    }


    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Log.d(TAG, "askLocationPermission: you should show an alert dialog...");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
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
        webView.setWebViewClient(new WebViewClient() {
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
                Boolean result = snapshot.getValue(Boolean.class);
                if (result != null && result) {
                    isUserAvailable = true;
                    Toast.makeText(getApplicationContext(), "Call Accepted", Toast.LENGTH_SHORT).show();
                    listenForConnId(friendUsername);
                } else {
                    Toast.makeText(getApplicationContext(), "Call ended", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void listenForConnId(String friendUsername) {
        fireBaseRef.child(friendUsername).child("connId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Toast.makeText(getApplicationContext(), "I'm here!", Toast.LENGTH_SHORT).show();
                if ((String) snapshot.getValue() == null) {
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
        callControlLayout.setVisibility(View.GONE);
        webView.setVisibility(View.GONE);
        fireBaseRef.child(username).child("incoming").setValue(null);
        navMenu.setVisibility(View.VISIBLE);
        initialLayout.setVisibility(View.VISIBLE);
        setUpWebview();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpWebview();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setUpWebview();
    }

    @Override
    protected void onDestroy() {
        fireBaseRef.child(username).setValue(null);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }

    @Override
    public void onGetCloseUsers(List<User> users) {
        availableUsers.clear();
        Log.e(TAG, "hahuma jaw hna :" + users.toString());
        availableUsers = users;
    }
}
