package com.example.securityprototype;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import java.io.Serializable;
import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class Track implements Serializable {

    private LatLng latLng;
    private Timestamp timestamp;

    public Track(){
        timestamp = new Timestamp(System.currentTimeMillis());

    }
    public Track(LatLng latLng){
        this.latLng = latLng;
        timestamp = new Timestamp(System.currentTimeMillis());

    }

    public LatLng getLatLng(){
        return latLng;
    }

    public Timestamp getTimeStamp(){
        return timestamp;
    }

    public void setLatLng(LatLng latLng){
        this.latLng = latLng;
    }

    public String convertToJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public String getDateAndTime(){
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/YY;HH:mm");
        String dateString = formatter.format(this.timestamp);
        return dateString;
    }




}
