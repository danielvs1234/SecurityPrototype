package com.example.securityprototype.Views;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.securityprototype.Controllers.MainActivityViewController;
import com.example.securityprototype.Interfaces.IMainActivityViewController;
import com.example.securityprototype.Model.Track;
import com.example.securityprototype.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button goToMapButton;
    private Button deleteButton;
    private TextView textView;
    private IMainActivityViewController mainActivityViewController;

    protected void onResume() {
        super.onResume();
        updateTextView();
    }
    protected void onStart() {
        super.onStart();
        updateTextView();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        goToMapButton = findViewById(R.id.goToMapsButton);
        deleteButton = findViewById(R.id.deleteButton);

        textView = findViewById(R.id.textView1);
        textView.setMovementMethod(new ScrollingMovementMethod());
        mainActivityViewController = new MainActivityViewController(this);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApplicationContext().deleteFile("map.dat");
                updateTextView();
            }
        });

        goToMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapsIntent);
            }
        });

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }
    }
    private void updateTextView(){
        List<Track> tracks = mainActivityViewController.loadTrackArrayListFromStorage();
        textView.setText("");
        int counter = 1;
        for(Track track: tracks){
            textView.append("Track "+ (counter++) +": " + "was taken " + track.getDateAndTime() + " and " + track.getLatLng().toString() +  "\n");
        }
        if(counter == 1){
            textView.setText("No data to load.");
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
