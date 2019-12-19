package com.example.securityprototype.Controllers;

import android.content.Context;

import com.example.securityprototype.Interfaces.IMainActivityViewController;
import com.example.securityprototype.Model.Track;
import com.example.securityprototype.Model.TrackHandler;

import java.util.List;

public class MainActivityViewController implements IMainActivityViewController {

    private TrackHandler trackController;

    public MainActivityViewController(Context context){
        trackController = new TrackHandler(context);
    }

    @Override
    public List<Track> loadTrackArrayListFromStorage() {
        return trackController.loadTrackArrayListFromStorage();
    }

    @Override
    public void saveTrackArrayToStorage() {
        trackController.saveTrackArrayToStorage();
    }
}
