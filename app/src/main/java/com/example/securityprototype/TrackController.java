package com.example.securityprototype;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class TrackController {

    private IStorage storageHandler;
    private IEncryption encryptionHandler;
    private Context context;

    private ArrayList<LatLng> latLngTestList;
    private ArrayList<Track> tempTrackArrayList;

    public TrackController(Context context) {

        this.context = context;
        storageHandler = new StorageHandler(context);
        encryptionHandler = new EncryptionHandler();

        latLngTestList = new ArrayList<>();
        tempTrackArrayList = new ArrayList<>();

        LatLng latLng1 = new LatLng(48.458352, 2.377730);
        LatLng latLng2 = new LatLng(52.321911, 13.271296);
        LatLng latLng3 = new LatLng(55.329144, 10.319999);
        LatLng latLng4 = new LatLng(51.454007, -0.222162);

        latLngTestList.add(latLng1);
        latLngTestList.add(latLng2);
        latLngTestList.add(latLng3);
        latLngTestList.add(latLng4);

    }


    public void newTrack() {
        Track newTrack = new Track(latLngTestList.get(1));
        tempTrackArrayList.add(newTrack);

    }

    public void saveTrackArrayToStorage() {

        if (storageHandler.checkIfFileExists()) {
            ArrayList<Track> storedList = loadTrackArrayListFromStorage();
            storedList.addAll(tempTrackArrayList);

            Log.d("TrackController", "saveTrackArrayToStorage: trackListSizeBeforeSaving = " + storedList.size());
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(baos);
                for (Track element : storedList) {
                    out.writeUTF(element.convertToJson());
                }
                byte[] bytes = baos.toByteArray();
                storageHandler.write(encryptionHandler.encryptBytes(bytes), "");

            } catch (IOException e) {
                e.printStackTrace();
            }

            tempTrackArrayList.clear();
        } else {

            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(baos);
                for (Track element : tempTrackArrayList) {
                    out.writeUTF(element.convertToJson());
                }
                byte[] bytes = baos.toByteArray();
                storageHandler.write(encryptionHandler.encryptBytes(bytes), "");

            } catch (IOException e) {
                e.printStackTrace();
            }

            tempTrackArrayList.clear();
        }


    }

    public ArrayList<Track> loadTrackArrayListFromStorage() {

        ArrayList<Track> trackList = new ArrayList<>();

        byte[] readArray = encryptionHandler.decryptData((HashMap) storageHandler.read(""));
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

}

