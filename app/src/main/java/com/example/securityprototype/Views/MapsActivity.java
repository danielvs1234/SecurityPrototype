package com.example.securityprototype.Views;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AppOpsManager;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import com.example.securityprototype.Controllers.TrackViewController;
import com.example.securityprototype.Interfaces.IStorage;
import com.example.securityprototype.Interfaces.ITrackViewController;
import com.example.securityprototype.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {

    private GoogleMap mMap;
    private Button backButton;
    private Button dateButton;
    private LocationManager lm;
    private String selectedDate;
    private Long selectedDateInMillis;

    private ITrackViewController trackViewController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dateButton = findViewById(R.id.dateButton);
        backButton = findViewById(R.id.backButton);

        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng berlin = new LatLng(52.321911, 13.271296);
     //   mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(berlin));

        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        handleLocationChanged();

        trackViewController = new TrackViewController(mMap, getApplicationContext());
        trackViewController.setMarkers(null);
    }

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                );

        datePickerDialog.show();

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        String dateString = dayOfMonth + "/" + month+1 + "/" + year;
        Log.d("date", "onDateSet: Date format = " + dateString);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        try {
            Date date = simpleDateFormat.parse(dateString);
            selectedDateInMillis = date.getTime();
            trackViewController.setMarkers(selectedDateInMillis);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private int MY_PERMISSIONS_FINE_LOCATION = 69;

    public void checkAndRequestPermissions(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_FINE_LOCATION);
        }
    }

    public void handleLocationChanged() {
        Log.d("onStarted", "handleLocationChanged: yai ");


        //do something with marker here,
        GnssStatus.Callback gnssListener = new GnssStatus.Callback() {

            @Override
            public void onStarted() {
                Log.d("onStarted", "onStarted: Main nåede hertil ");

                trackViewController.gpsUpdateOccured(selectedDate);
                trackViewController.setMarkers(selectedDateInMillis);
                super.onStarted();
            }

            @Override
            public void onStopped() {
                super.onStopped();
            }
        };

        try{
            checkAndRequestPermissions();
            lm.registerGnssStatusCallback(gnssListener);
        }catch(SecurityException e){
            e.printStackTrace();
        }
        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }

            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /*
    * Method runs when the application is put in the background
    * Saving here
     */
    @Override
    public void onStop() {
        trackViewController.saveTrackArrayToStorage();
        Log.d("mapsActivity.onStop", "onStopMethod ran");
        super.onStop();
    }

    /*
    * Method only partially runs when the app is destroyed, due to system killing this activity
    *
     */
    @Override
    public void onDestroy(){
        super.onDestroy();
    }
}
