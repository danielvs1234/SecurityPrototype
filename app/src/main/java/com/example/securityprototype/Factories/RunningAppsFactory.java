package com.example.securityprototype.Factories;

import com.example.securityprototype.Interfaces.IRunningApps;
import com.example.securityprototype.Model.ActiveApps;

public final class RunningAppsFactory {

    private static IRunningApps instance;

    /*
    * Factory returning instance through singleton
    * @return IRunningApps
    */
    public static IRunningApps getInstance(){
        if(instance == null)
            instance = new ActiveApps();
        return instance;
    }

}
