package com.example.aas.firebasephoneauth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback
        , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;
    private ImageView backToHome;
    private LinearLayout arrival;
    private LinearLayout leaving;
    private TextView arrivalTime;
    private TextView leavingTime;

    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location lastLocation;
    private Marker currentUserLocationMarker;
    private static final int REQUEST_USER_LOCATION_CODE=99;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        arrivalTime = (TextView) findViewById(R.id.arrivalTime);
        leavingTime = (TextView) findViewById(R.id.leavingTime);

        arrival = (LinearLayout) findViewById(R.id.arrival);
        leaving = (LinearLayout) findViewById(R.id.leaving);

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M){
            checkUserLocationPermission();
        }


        arrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userArrival();
            }
        });
        leaving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLeaving();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        backToHome = (ImageView) findViewById(R.id.backToHome);
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MapsActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }//onCreate end

    private void userLeaving() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .mustBeOnFuture()
                //.curved()
                .minutesStep(15)
                .displayAmPm(true)
                .displayYears(false)
                .displayHours(true)
                .displayMinutes(true)
                .todayText("Today")
                .titleTextColor(Color.BLACK)
                .mainColor(Color.BLACK)
                .backgroundColor(Color.WHITE)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

                .title("Leaving Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,HH:mm aa");
                        String leavingDate = dateFormat.format(date);
                        leavingTime.setText(leavingDate);
                    }
                }).display();
    }

    private void userArrival() {
        new SingleDateAndTimePickerDialog.Builder(this)
                .bottomSheet()
                .mustBeOnFuture()
                //.curved()
                .minutesStep(15)
                .displayAmPm(true)
                .displayYears(false)
                .displayHours(true)
                .displayMinutes(true)
                .todayText("Today")
                .titleTextColor(Color.BLACK)
                .mainColor(Color.BLACK)
                .backgroundColor(Color.WHITE)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {

                    }
                })

                .title("Arrival Time")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd,HH:mm aa");
                        String arrivalDate = dateFormat.format(date);
                        arrivalTime.setText(arrivalDate);
                    }
                }).display();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

             buildGoogleApiClient();
          mMap.setMyLocationEnabled(true);
        }
    }

    public boolean checkUserLocationPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_USER_LOCATION_CODE);
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_USER_LOCATION_CODE);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode)
        {
            case REQUEST_USER_LOCATION_CODE:
                if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        if(googleApiClient==null){
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }else {
                    Toast.makeText(this, "Permission Denied..", Toast.LENGTH_SHORT).show();
                }return;
        }
    }

    protected synchronized void buildGoogleApiClient(){
        googleApiClient=new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

        @Override
        public void onLocationChanged(Location location) {
        lastLocation=location;
        if(currentUserLocationMarker!=null){
            currentUserLocationMarker.remove();

        }
        LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        currentUserLocationMarker=mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomBy(14));

        if(googleApiClient!=null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);

        }

        }

        @Override
        public void onConnected(@Nullable Bundle bundle) {

        locationRequest=new LocationRequest();
        locationRequest.setInterval(1100);
        locationRequest.setFastestInterval(1100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED)
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }

        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }
    @Override
    protected void onStart() {
        DateFormat dateFormat2 = new SimpleDateFormat("MMM dd,HH:mm aa");
        DateFormat dateFormat1 = new SimpleDateFormat("MMM dd,HH:mm aa");
        String dateString2 = dateFormat2.format(new Date());
        arrivalTime.setText(dateString2);
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 10);
        Date newDate=cal.getTime();
        String dateString1 = dateFormat1.format(newDate);
        leavingTime.setText(dateString1);
        super.onStart();
    }

    }
