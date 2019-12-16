package com.example.securityprototype.Views;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securityprototype.R;
import com.example.securityprototype.Model.Track;
import com.example.securityprototype.Model.TrackController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private Button decryptButton;
    private Button deleteButton;
    private Button goToMapButton;
    private TextView textView;
    private LocationManager lm;
    private TrackController trackController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = findViewById(R.id.saveButton);
        decryptButton = findViewById(R.id.decryptButton);
        deleteButton = findViewById(R.id.deleteButton);
        goToMapButton = findViewById(R.id.goToMapsButton);

        textView = findViewById(R.id.textView1);
        textView.setMovementMethod(new ScrollingMovementMethod());
        trackController = new TrackController(getApplicationContext());


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                trackController.newTrack();
                trackController.newTrack();
                trackController.newTrack();
                trackController.saveTrackArrayToStorage();


            }
        });

        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  decryptTextFromHashMapAndShowIt();
                ArrayList<Track> tracks = trackController.loadTrackArrayListFromStorage();
                textView.append("Track 1: " + "Latlng: " + tracks.get(0).getDateAndTime());
                textView.append("Track 2: " + "Latlng: " + tracks.get(1).getDateAndTime());
                textView.append("Track 3: " + "Latlng: " + tracks.get(2).getDateAndTime());

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().deleteFile("map.dat");
            }
        });

        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
        });

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
}
