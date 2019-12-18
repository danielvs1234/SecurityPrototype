package com.example.securityprototype.Model;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.securityprototype.Factories.RunningAppsFactory;
import com.example.securityprototype.Interfaces.IEncryption;
import com.example.securityprototype.Interfaces.IRunningApps;
import com.example.securityprototype.Interfaces.IStorage;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TrackHandler extends Activity {

    private IStorage storageHandler;
    private IEncryption encryptionHandler;
    private Context context;

    private ArrayList<Track> tempTrackArrayList;
    private BatteryManager batteryManager;
    private final IRunningApps runningApps;
    public TrackHandler(Context context) {
        this(context, RunningAppsFactory.getInstance());
    }
    public TrackHandler(Context context, IRunningApps runningApps) {

        this.context = context;
        storageHandler = new StorageHandler(context);
        encryptionHandler = new EncryptionHandler();

        tempTrackArrayList = new ArrayList<>();

        batteryManager = (BatteryManager) context.getSystemService(BATTERY_SERVICE);
        this.runningApps = runningApps;
    }

    private int MY_PERMISSIONS_FINE_LOCATION = 69;

    public void checkAndRequestPermissions() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_FINE_LOCATION);
        }
    }

    public void newTrack(String date) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        try {
            checkAndRequestPermissions();
            Location lat = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Long msAgo = 86400L;

            if(date != null){
                msAgo = Long.parseLong(date);
            }
            Map<Long, AppModel> apps =  this.runningApps.getRunningApps(context, msAgo);
            if (lat != null) {
                ArrayList<String> applicationNames = new ArrayList<>();
                int counter = 0;
                for (Map.Entry<Long, AppModel> entry1 : apps.entrySet()) {
                    if (counter == 3) {
                        break;
                    }
                    if (!entry1.getValue().getApplicationName().matches("SecurityPrototype|OnePlus-startprogram|Android-system|Indstillinger" )) {
                        applicationNames.add(entry1.getValue().getApplicationName());
                        counter++;
                    }

                }

                LatLng tempLatLng = new LatLng(lat.getLatitude(), lat.getLongitude());
                Log.d("newTrack", "newTrack: Lat" + lat.getLatitude());
                Log.d("newTrack", "newTrack: List of applications" + applicationNames);
                Track track = new Track(tempLatLng);

                track.setApplicationName(applicationNames);
                tempTrackArrayList.add(track);

                if(batteryManager.isCharging()){
                    saveTrackArrayToStorage();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        System.out.println("OK");
    }


    public void saveTrackArrayToStorage() {

        if (storageHandler.checkIfFileExists()) {
            ArrayList<Track> storedList = loadTrackArrayListFromStorage();
            storedList.addAll(tempTrackArrayList);
            Log.d("TrackController", "saveTrackArrayToStorage: trackListSizeBeforeSaving = " + storedList.size());
            try {
                byte[] bytes = getByteArrayOfTracks(storedList);
                storageHandler.write(encryptionHandler.encryptBytes(bytes), "map.dat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                byte[] bytes = getByteArrayOfTracks(tempTrackArrayList);
                storageHandler.write(encryptionHandler.encryptBytes(bytes), "map.dat");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        tempTrackArrayList.clear();
    }
    public byte[] getByteArrayOfTracks(ArrayList<Track> list) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(baos);
        for (Track element : list) {
            out.writeUTF(element.convertToJson());
        }
        return baos.toByteArray();
    }
    public ArrayList<Track> loadTrackArrayListFromStorage() {

        ArrayList<Track> trackList = new ArrayList<>();

        byte[] readArray = encryptionHandler.decryptData((HashMap) storageHandler.read("map.dat"));
        ByteArrayInputStream bais = new ByteArrayInputStream(readArray);
        DataInputStream in = new DataInputStream(bais);

        try {
            while (in.available() > 0) {
                String element = in.readUTF();
                JsonHandler jsonHandler = new JsonHandler();
                Track track = jsonHandler.convertTrackFromJson(element);
                trackList.add(track);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.d("TrackController", "loadTrackArrayListFromStorage: listSizeAfterLoading = " + trackList.size());
        return trackList;
    }

    public void setStorageHandler(IStorage storageHandler) {
        this.storageHandler = storageHandler;
    }

    public void setEncryptionHandler(IEncryption encryptionHandler) {
        this.encryptionHandler = encryptionHandler;
    }

    public void setTempTrackArrayList(ArrayList<Track> tempTrackArrayList) {
        this.tempTrackArrayList = tempTrackArrayList;
    }

}

