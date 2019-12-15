package com.example.securityprototype.Model;

import java.util.ArrayList;
import java.util.List;

public class AppModel {
    private String packageName;
    private String applicationName;
    private List<String> correlatedPermissions = new ArrayList<>();


    public AppModel() {

    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<String> getCorrelatedPermissions() {
        return correlatedPermissions;
    }

    public void setCorrelatedPermissions(List<String> correlatedPermissions) {
        this.correlatedPermissions = correlatedPermissions;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }


}