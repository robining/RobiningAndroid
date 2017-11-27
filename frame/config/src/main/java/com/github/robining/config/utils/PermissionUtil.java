package com.github.robining.config.utils;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017\11\20 0020.
 */

public class PermissionUtil {
    public static List<String> getManifestShouldRequestPermissions(Activity activity) {
        List<String> dangerousPermissions = new ArrayList<>();
        try {
            PackageManager pm = activity.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(activity.getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] requested = packageInfo.requestedPermissions;
            for (String str : requested) {
                boolean isShouldRequest = ActivityCompat.shouldShowRequestPermissionRationale(activity, str);
                if (isShouldRequest) {
                    dangerousPermissions.add(str);
                }

                //                PermissionInfo permissionInfo = pm.getPermissionInfo(str, 0);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return dangerousPermissions;
    }
}
