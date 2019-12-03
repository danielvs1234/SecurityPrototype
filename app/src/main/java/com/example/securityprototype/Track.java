package com.example.securityprototype;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;

public class Track {

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


}
