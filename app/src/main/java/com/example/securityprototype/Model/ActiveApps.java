package com.example.securityprototype.Model;

import android.Manifest;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.example.securityprototype.Interfaces.IRunningApps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class ActiveApps implements IRunningApps {

    private static ActiveApps instance = null;


    private static String[] positionPermissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_BACKGROUND_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
            Manifest.permission.ACCESS_MEDIA_LOCATION,
            Manifest.permission.CONTROL_LOCATION_UPDATES,
            Manifest.permission.LOCATION_HARDWARE,
            Manifest.permission.INSTALL_LOCATION_PROVIDER,
            Manifest.permission.INSTALL_LOCATION_PROVIDER
    };

    public ActiveApps() {
    }

    /**
     * Get all applications used in a timeperiod
     * @param context
     * @param msAgo
     * @return
     */
    private Map<Long, String> getUsedApplicationsInTimeperiod(Context context, int msAgo) {
        SortedMap<Long, String> mySortedMap = new TreeMap<>();
        UsageStatsManager usm = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
        long time = System.currentTimeMillis();
        List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY,  time - msAgo, time);
        if (appList != null && appList.size() > 0) {
            for (UsageStats usageStats : appList) {
                mySortedMap.put(usageStats.getLastTimeUsed(), usageStats.getPackageName());
            }
        }
        return mySortedMap;
    }
    /**
     *
     * @return a list of running apps which has certain permissions.
     */
    public Map<Long, AppModel> getRunningApps(Context context, int msAgo) {
        Map<Long, AppModel> appList = new TreeMap<>(Collections.reverseOrder());

        //Get applications used sorted by time since used
        Map<Long, String> list = getUsedApplicationsInTimeperiod(context, 1000*msAgo);
        for (Map.Entry<Long, String> entry : list.entrySet()) {
            AppModel app = checkAndGetAppWithPermissions(context, entry.getValue(), positionPermissions);
            if(app != null){
                appList.put(entry.getKey(), app);
            }
        }
        return appList;
    }

    /**
     * Checks if an application has certain granted permissions
     * @param context
     * @param packageName the application name
     * @param withPermissions permissions to check
     * @return the correlated permissions that has been given
     * @throws PackageManager.NameNotFoundException
     */
    private List<String> getCorrelatedGrantedPermissions(Context context, final String packageName, String[] withPermissions) throws PackageManager.NameNotFoundException{
        List<String> correlatedPermissions = new ArrayList<>();
        PackageInfo pi = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        if(pi.requestedPermissions != null){
            for (int i = 0; i < pi.requestedPermissions.length; i++) {
                //bitwise operator AND
                if ((pi.requestedPermissionsFlags[i] & PackageInfo.REQUESTED_PERMISSION_GRANTED) != 0) {
                    for(String permission: withPermissions){
                        if(pi.requestedPermissions[i].equals(permission)){
                            correlatedPermissions.add(permission);
                        }
                    }
                }
            }
        }
        return correlatedPermissions;
    }

    /**
     * Check if an application has certain permissions. If none are correlated returns null.
     * @param context
     * @param packageName the application name
     * @param withPermissions permissions to check
     * @return null if no correlated permissions, else returns App object with correlated permissions
     */
    private AppModel checkAndGetAppWithPermissions(Context context, final String packageName, String[] withPermissions){
        try {
            List<String> correlatedPermissions = this.getCorrelatedGrantedPermissions(context, packageName, withPermissions);
            if(correlatedPermissions.size() > 0){
                AppModel appWithCorrectPermissions = new AppModel();
                appWithCorrectPermissions.setCorrelatedPermissions(correlatedPermissions);
                appWithCorrectPermissions.setPackageName(packageName);
                final PackageManager pm = context.getPackageManager();
                ApplicationInfo ai;
                try {
                    ai = pm.getApplicationInfo( packageName, 0);
                } catch (final PackageManager.NameNotFoundException e) {
                    ai = null;
                }
                appWithCorrectPermissions.setApplicationName((String)(ai != null ? pm.getApplicationLabel(ai) : "(unknown)"));
                return appWithCorrectPermissions;
            }
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}