package com.example.securityprototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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

        lm = this.getSystemService(LocationManager.class);

    }

}
