package com.sannniu.ncore.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;


public class SysUtil {

    public static String getVersion(Context ctx) {
        PackageManager packagemanager = ctx.getPackageManager();
        String pkgName = ctx.getPackageName();
        int ver = 0;
        try {
            ver = packagemanager.getPackageInfo(pkgName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return ver + "";
    }
}
