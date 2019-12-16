package com.example.securityprototype.Interfaces;

import android.content.Context;

import com.example.securityprototype.Model.AppModel;

import java.util.Map;

public interface IRunningApps {

    Map<Long, AppModel> getRunningApps(Context context);

}
