package com.example.securityprototype;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class TrackViewController {

    GoogleMap mMap;
    TrackController trackController;
    Context context;

    public TrackViewController(GoogleMap mMap, Context context) {
        this.mMap = mMap;
        trackController = new TrackController(context);
        this.context = context;

    }

    public void newTrack(){
        trackController.newTrack();
    }

    public void setMarkersForEachTrack(String optionalDate){


        ArrayList<Track> trackList = trackController.loadTrackArrayListFromStorage();
        ArrayList<Marker> markerList = new ArrayList<>();
        ArrayList<Track> dateTrackList = new ArrayList<>();

        if(optionalDate != null){
            for (Track track : trackList){
                if(track.getDate().equals(optionalDate)){
                    dateTrackList.add(track);
                }
            }
            trackList = dateTrackList;
        }

        for (Track track : trackList){
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

                LatLng latLng = track.getLatLng();

                TextView tvLat = v.findViewById(R.id.tv_lat);
                TextView tvLng = v.findViewById(R.id.tv_lng);
                TextView tvDateTime = v.findViewById(R.id.tv_timeStamp);

                tvLat.setText("Latitude: " + track.getLatLng().latitude);
                tvLng.setText("Longitude: " + track.getLatLng().longitude);

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
