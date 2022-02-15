package com.example.iSee.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.example.iSee.Database.UserDbHelper;
import com.example.iSee.Models.User;
import com.example.iSee.R;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;

import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener
,MapboxMap.OnMapClickListener {

    BottomNavigationItemView homeItem;
    BottomNavigationItemView profileItem;
    BottomNavigationView navMenu;
    UserDbHelper userHelper = new UserDbHelper(this);
    private MapView mapview;
    private MapboxMap map;
    private PermissionsManager permissionsManager;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_maps);
        mapview=findViewById(R.id.mapview);
        navMenu = findViewById(R.id.Bottom_menu);
        mapview.onCreate(savedInstanceState);
        mapview.getMapAsync(this);


        profileItem = findViewById(R.id.profileItem);
        homeItem = findViewById(R.id.dashboardItem);
        String email=getIntent().getStringExtra("email");
        User user=userHelper.getUser(email);
        profileItem.setOnClickListener(view -> {
            Intent profileIntent = new Intent(this, ProfileActivity.class);
            profileIntent.putExtra("email",user.getEmail().trim());
            startActivity(profileIntent);
        });
        homeItem.setOnClickListener(view -> {
            Intent homeIntent = new Intent(this, HomeVolunteerActivity.class);
            homeIntent.putExtra("email",user.getEmail().trim());
            startActivity(homeIntent);

        });


    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map=mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();


    }

    private  void enableLocation(){
        if (permissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEgine();
            initializeLocationLayer();

        }else{
            permissionsManager=new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }

    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationEgine(){
        locationEngine= new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation=locationEngine.getLastLocation();
        if (lastLocation!=null){
            originLocation=lastLocation;
            setCameraPosition(lastLocation);

        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }
    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin=new LocationLayerPlugin(mapview,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);


    }
    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                location.getLongitude()),13.0));
    }
    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(destinationMarker!=null){
            map.removeMarker(destinationMarker);
        }
        destinationMarker=map.addMarker(new MarkerOptions().position(point));
        destinationPosition=Point.fromLngLat(point.getLongitude(),point.getLatitude());
        originPosition=Point.fromLngLat(originLocation.getLongitude(),originLocation.getLatitude());



    }
    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location!=null){
            originLocation=location;
            setCameraPosition(location);
        }

    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted){
            enableLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }
        if (locationLayerPlugin!=null){
            locationLayerPlugin.onStart();
        }
        mapview.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapview.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
       /* if (locationEngine!=null){
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin!=null){
            locationLayerPlugin.onStop();
        }
        mapview.onStop();*/

    }

    @Override
    public void onSaveInstanceState( Bundle outState) {
        super.onSaveInstanceState(outState);
        mapview.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapview.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      /*  if (locationEngine!=null){
            locationEngine.deactivate();
        }*/
        mapview.onDestroy();
    }



}