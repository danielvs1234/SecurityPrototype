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

    public void setMarkersForEachTrack(){
        ArrayList<Track> trackList = trackController.loadTrackArrayListFromStorage();
        final ArrayList<Marker> markerList = new ArrayList<>();

        for (Track track : trackList){
           Marker marker = mMap.addMarker(new MarkerOptions().position(track.getLatLng()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
           marker.setTag(track);
           markerList.add(marker);
        }

        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker marker) {

                Track track = (Track) marker.getTag();
                // Getting view from the layout file info_window_layout
                LayoutInflater inflater = LayoutInflater.from(context);
                View v = inflater.inflate(R.layout.windowlayout, null);

                // Getting the position from the marker
                LatLng latLng = track.getLatLng();

                // Getting reference to the TextView to set latitude
                TextView tvLat = v.findViewById(R.id.tv_lat);

                // Getting reference to the TextView to set longitude
                TextView tvLng = v.findViewById(R.id.tv_lng);

                TextView tvDateTime = v.findViewById(R.id.tv_timeStamp);

                // Setting the latitude
                tvLat.setText("Latitude: " + track.getLatLng().latitude);

                // Setting the longitude
                tvLng.setText("Longitude: " + track.getLatLng().longitude);

                tvDateTime.setText("Date: " + track.getDate() + "\n" + "Time: " + track.getTime());

                // Returning the view containing InfoWindow contents
                return v;

            }
        });

        // Adding and showing marker while touching the GoogleMap
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(Marker marker) {
                // Creating an instance of MarkerOptions to set position
                MarkerOptions markerOptions = new MarkerOptions();

                // Setting position on the MarkerOptions
                markerOptions.position(marker.getPosition());

                // Animating to the currently touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));


                // Showing InfoWindow on the GoogleMap
                marker.showInfoWindow();

                return true;

            }

        });
    }


}
