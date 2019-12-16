package com.example.securityprototype.Interfaces;

import android.content.Context;

import com.google.android.gms.maps.GoogleMap;

public interface ITrackViewController {

    void gpsUpdateOccured();
    void setMarkers();
    void setMarkers(String date);

}
