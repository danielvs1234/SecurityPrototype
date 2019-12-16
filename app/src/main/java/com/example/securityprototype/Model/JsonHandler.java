package com.example.securityprototype.Model;

import com.google.gson.Gson;

public class JsonHandler {

    public JsonHandler(){

    }

    public Track convertTrackFromJson(String jsonString){
        Gson gson = new Gson();
        Track track = gson.fromJson(jsonString, Track.class);
        return track;
    }
}
