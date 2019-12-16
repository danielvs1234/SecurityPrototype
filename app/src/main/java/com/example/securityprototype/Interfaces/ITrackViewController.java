package com.example.securityprototype.Interfaces;

import com.example.securityprototype.Model.Track;

import java.util.List;

public interface ITrackViewController {

    void gpsUpdateOccured(String date);
    void setMarkers(Long date);
    void saveTrackArrayToStorage();
    List<Track> loadTrackArrayListFromStorage();

}
