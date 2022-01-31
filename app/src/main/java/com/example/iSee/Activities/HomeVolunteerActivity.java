package com.example.iSee.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.iSee.Controllers.facade.IUpdateLocController;
import com.example.iSee.Controllers.impl.UpdateLocController;
import com.example.iSee.R;
import com.example.iSee.Views.ICallVIew;
import com.example.iSee.Views.WebJavascriptInterface;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;
import java.util.UUID;

public class HomeVolunteerActivity extends AppCompatActivity implements ICallVIew {
    IUpdateLocController updateLocController;

    BottomNavigationItemView settingsItem;
    BottomNavigationItemView profileItem;
    //    LOCALISATION HELPER VARIABLES
    private static final String TAG = "HomeVolunteerActivity";
    int LOCATION_REQUEST_CODE = 10001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    double Latitude=0.0,Longitude=0.0;
    //    UI CALL HELPER VARIABLES
    WebView webView;
    ConstraintLayout initialLayout;
    BottomNavigationView navMenu;
    RelativeLayout callLayout;
    LinearLayout callControlLayout;
    TextView incomingCallText;
    ImageView toggleAudioBtn;
    ImageView acceptBtn;
    ImageView rejectBtn;
    String username = "";
    String connId = "";
    boolean isAudio = true;
    boolean isVideo = true;
    boolean isPeerConnected = true;
    Button tuto;

    //    FireBase instance
    DatabaseReference fireBaseRef = FirebaseDatabase.getInstance().getReference("users");


    //    CallBack To Update the location
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    Latitude = location.getLatitude();
                    Longitude = location.getLongitude();

                }
            }
        };
   //********************************************************************** OnCreate ***************************************************************************
    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer_home);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        webView = findViewById(R.id.webview);
        callControlLayout = findViewById(R.id.callControlLayout);
        callLayout = findViewById(R.id.callLayout);
        incomingCallText = findViewById(R.id.incomingCallText);
        toggleAudioBtn = findViewById(R.id.toggleAudioBtn);
        acceptBtn = findViewById(R.id.acceptBtn);
        rejectBtn = findViewById(R.id.rejectBtn);
        navMenu = findViewById(R.id.Bottom_menu);
        initialLayout = findViewById(R.id.initialLayout);
        profileItem = findViewById(R.id.profileItem);
        settingsItem = findViewById(R.id.settingsItem);
        tuto=findViewById(R.id.Tuto_app);



//Animation
        Animation animation= AnimationUtils.loadAnimation(HomeVolunteerActivity.this,R.anim.rotate);
        (findViewById(R.id.imageView)).startAnimation(animation);
        //Get User fullname & email
        TextView usernameText = findViewById(R.id.user_name);
        usernameText.setText("Hi " + getIntent().getStringExtra("fullname") + " !");
        String email=getIntent().getStringExtra("email");
        updateLocController=new UpdateLocController(this);
// update user location every 10 s
        final Handler handler = new Handler();
        final int delay =10000;

        handler.postDelayed(new Runnable() {
            public void run() {
                updateLocController.onUpdateLocalisation(email,Latitude,Longitude);
                Toast.makeText(HomeVolunteerActivity.this, "Localisation updated !", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this, delay);
            }
        }, delay);
 //_________________________________________________________________

        username = Objects.requireNonNull(getIntent().getStringExtra("fullname")).trim();
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

        profileItem.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("fullname",getIntent().getStringExtra("fullname"));
            profileIntent.putExtra("email",getIntent().getStringExtra("email"));
            startActivity(profileIntent);
        });
        settingsItem.setOnClickListener(view -> {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            settingsIntent.putExtra("email",getIntent().getStringExtra("email"));
            settingsIntent.putExtra("fullname",getIntent().getStringExtra("fullname"));

            startActivity(settingsIntent);
        });

        tuto.setOnClickListener(view -> {
            Intent intent = new Intent(this, TipsActivity.class);
            startActivity(intent);
        });




    }
//*************************************************************** Oncreate End ************************************************************************
    @Override
    protected void onStart() {
        setUpWebview();
        super.onStart();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            checkSettingsAndStartLocationUpdates();

        } else {
            askLocationPermission();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopLocationUpdates();
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

//    Location Helper Function
    private void checkSettingsAndStartLocationUpdates() {
        LocationSettingsRequest request = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();
        SettingsClient client = LocationServices.getSettingsClient(this);

        Task<LocationSettingsResponse> locationSettingsResponseTask = client.checkLocationSettings(request);
        locationSettingsResponseTask.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                startLocationUpdates();
            }
        });
        locationSettingsResponseTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException apiException = (ResolvableApiException) e;
                    try {
                        apiException.startResolutionForResult(HomeVolunteerActivity.this, 1001);
                    } catch (IntentSender.SendIntentException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());



    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void getLastLocation() {
        @SuppressLint("MissingPermission")
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    Log.d(TAG, "" + location.toString());
                    Log.d(TAG, "" + location.getLatitude());
                    Log.d(TAG, "" + location.getLongitude());
                } else {
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            }
        });
    }

    private void askLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkSettingsAndStartLocationUpdates();
            } else {
            }
        }
    }


    @Override
    public void onBackPressed() {
        fireBaseRef.child(username).setValue(null);
    }

    @Override
    protected void onDestroy() {
        fireBaseRef.child(username).setValue(null);
        webView.loadUrl("about:blank");
        super.onDestroy();
    }
}