package com.example.securityprototype.Controllers;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.securityprototype.Interfaces.ITrackViewController;
import com.example.securityprototype.R;
import com.example.securityprototype.Model.Track;
import com.example.securityprototype.Model.TrackController;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class TrackViewController implements ITrackViewController {

    private GoogleMap mMap;
    private TrackController trackController;
    private Context context;

    public TrackViewController(GoogleMap mMap, Context context) {
        this.mMap = mMap;
        trackController = new TrackController(context);
        this.context = context;

    }

    @Override
    public void gpsUpdateOccured(){
        trackController.newTrack();
    }

    public void setMarkers() {


        ArrayList<Track> trackList = trackController.loadTrackArrayListFromStorage();
        ArrayList<Marker> markerList = new ArrayList<>();

        for (Track track : trackList) {
            Marker marker = mMap.addMarker(new MarkerOptions().position(track.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            marker.setTag(track);
            markerList.add(marker);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Track track = (Track) marker.getTag();

                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.windowlayout, null);

                TextView tvLat = v.findViewById(R.id.tv_lat);
                TextView tvDateTime = v.findViewById(R.id.tv_timeStamp);


                tvLat.setText("Possible Trackers: \n" + "* " + track.getApplicationName().get(0) + "\n" + "* " + track.getApplicationName().get(1) + "\n" + "* " + track.getApplicationName().get(2));

                tvDateTime.setText("Date: " + track.getDate() + "\n" + "Time: " + track.getTime());
                Log.d("date", "getInfoContents: date format = " + track.getDate());

                return v;

            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(marker.getPosition());

                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                marker.showInfoWindow();

                return true;

            }

        });
    }
}
