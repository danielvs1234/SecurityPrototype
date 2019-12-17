package com.example.securityprototype.Interfaces;

import com.example.securityprototype.Model.Track;

import java.util.List;

public interface IMainActivityViewController {

    List<Track> loadTrackArrayListFromStorage();
    void saveTrackArrayToStorage();
}
