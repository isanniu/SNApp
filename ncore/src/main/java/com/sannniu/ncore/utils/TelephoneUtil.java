package com.sannniu.ncore.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.Locale;
import java.util.UUID;

/**
 * 电话-工具
 */
public class TelephoneUtil {

    /**
     * 网络是否激活状态
     * 
     * @param context
     * @return 权限：android.Manifest.permission#ACCESS_NETWORK_STATE
     */
    public static boolean isNetworkAvailable(Context context) {
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            return (networkInfo != null) && (networkInfo.isAvailable() && networkInfo.isConnected());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * WIFI是否可用
     * 
     * @param context
     * @return 权限：android.Manifest.permission#ACCESS_NETWORK_STATE
     */
    public static boolean isWifiEnable(Context context) {
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                    .getActiveNetworkInfo();
            return (networkInfo != null)
                    && (networkInfo.isAvailable() && networkInfo.getType() == ConnectivityManager.TYPE_WIFI);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 获取android SDK 版本
     * 
     * @return
     */
    public static int getAndroidSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 获取缓存目录大小（单个应用最大缓存限制）
     * 
     * @param context
     * @return
     */
    public static int getCacheSize(Context context) {
        return 1024 * ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
    }

    /**
     * 获取DEVICE
     * 
     * @return
     */
    public static String getDevice() {
        return Build.DEVICE;
    }

    /**
     * 获取手机 IMEI(the IMEI for GSM and the MEID or ESN for CDMA phones) Requires Permission:
     * {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getIMEI(Context context) {
        String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        if (str == null)
            str = "";
        return str;
    }

    /**
     * 获取手机 IMSI Requires Permission: {@link android.Manifest.permission#READ_PHONE_STATE READ_PHONE_STATE}
     */
    public static String getIMSI(Context context) {
        String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSubscriberId();
        if (str == null)
            str = "";
        return str;
    }

    /**
     * 获取手机MAC地址
     * 
     * @param context
     * @return
     */
    public static String getLocalMacAddress(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getMacAddress();
    }

    /**
     * 获取手机网络名称
     * 
     * @param context
     * @return
     */
    public static String getNetWorkName(Context context) {
        return getNetworkType(context).toLowerCase();
    }

    /**
     * Returns the alphabetic name of current registered operator.
     * <p>
     * Availability: Only when user is registered to a network. Result may be unreliable on CDMA networks (use
     * {@link TelephonyManager#getPhoneType()} to determine if on a CDMA network).
     */
    public static String getNetworkOperatorName(Activity activity) {
        return ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }

    public static String getNetworkOperatorName(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getNetworkOperatorName();
    }

    /**
     * 获取手机当前语言
     * 
     * @return
     */
    public static String getPhoneLanguage() {
        String str = Locale.getDefault().getLanguage();
        if (str == null)
            str = "";
        return str;
    }

    /**
     * 获取手机类型
     * 
     * @return
     */
    public static String getPhoneType() {
        String str = Build.MODEL;
        if (str != null)
            str = str.replace(" ", "");
        return str.trim();
    }



    /**
     *
     * 获取Product
     * 
     * @return
     */
    public static String getProduct() {
        return Build.PRODUCT;
    }

    /**
     * @param context
     * @param unit
     *            The unit to convert from. {@link TypedValue#TYPE_DIMENSION}.
     * @param value
     *            value The value to apply the unit to.
     * @return The complex floating point value multiplied by the appropriate metrics depending on its unit.
     */
    public static int getRawSize(Context context, int unit, float value) {
        Resources resources;
        if (context == null)
            resources = Resources.getSystem();
        else
            resources = context.getResources();
        return (int) TypedValue.applyDimension(unit, value, resources.getDisplayMetrics());
    }

    /**
     * 获取手机分辨率
     * 
     * @param activity
     * @return
     */
    public static String getResolution(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    /**
     * 获取手机分辨率
     * 
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels + "x" + dm.heightPixels;
    }

    /***
     * 获取手机屏幕密度
     */
    public static String getDensityDpi(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return ""+dm.densityDpi;
    }

    /***
     * 获取手机屏幕密度
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }


    /**
     * 获取SDK版本
     * 
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getSDKVersion() {
        return Build.VERSION.SDK;
    }

    /**
     * 获取SDK版本名称
     * 
     * @return
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前服务(网络)类型
     * 
     * @param context
     * @return wifi/mobile/unicom/telecom
     */
    public static String getServiceName(Context context) {
        if (getNetworkType(context).equals("wifi"))
            return "wifi";
        if (isConnectChinaMobile(context))
            return "移动";
        if (isConnectChinaUnicom(context))
            return "联通";
        if (isConnectChinaTelecom(context))
            return "电信";
        return "";
    }

    public static String getType() {
        return Build.TYPE;
    }

    /**
     * 获取UserAgent
     * 
     * @return
     */
    public static String getUserAgent() {
        return getPhoneType();
    }

    public static String getAccessPointType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
            return networkInfo.getExtraInfo();
        return null;
    }

    public static boolean isIpAddress(String host) {
        boolean isAllValidChars = true;
        int dotCount = 0; // .
        int colonCount = 0; // :
        for (int i = 0; i < host.length(); i++) {
            char ch = host.charAt(i);
            if (ch == ':') {
                colonCount++;
            }
            else if (ch == '.') {
                dotCount++;
            }
            else {
                char upper = Character.toUpperCase(ch);
                if ((upper >= 48 && upper <= 57) || (upper >= 65 && upper <= 70)) {
                    // is valid ip char
                }
                else {
                    // invalid char
                    isAllValidChars = false;
                    break;
                }
            }
        }

        if (isAllValidChars) {
            if (dotCount == 3) {
                return true;
            }
            else if (colonCount >= 2) {
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    // 得到当前使用的接入点的代理地址
    public static Proxy getApnProxy(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (ni.isConnected()) { // 如果有wifi连接，则选择wifi，不返回代理
            return null;
        } else {
            try{
                Cursor c = getCurrentApn(context); // 得到默认apn
                if (c != null && c.getCount() > 0) {
                    c.moveToFirst();

                    String proxy = c.getString(c.getColumnIndex("proxy")); // 得到代理
                    String port = c.getString(c.getColumnIndex("port")); // 得到断开号
                    c.close();

                    if (proxy != null && !proxy.equals("")) { // 代理地址不为空
                        if (port != null && !port.equals("")) {
                            if (isIpAddress(proxy)) {
                                return new Proxy(Proxy.Type.HTTP, InetSocketAddress.createUnresolved(proxy, Integer.valueOf(port)));
                            }
                            else {
                                return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxy, Integer.valueOf(port)));
                            }
                        } else {
                            return null;
                        }
                    }
                }
                else {
                    if (c != null) {
                        c.close();
                    }
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 得到手机上面当前默认使用的接入点
     * @param context
     * @return
     */
    public static Cursor getCurrentApn(Context context) {
        return context.getContentResolver().query(
                Uri.parse("content://telephony/carriers/preferapn"), null,
                null, null, null);
    }

    public static String getCurrentApnProxy(Context context) {
        Cursor cur = null;
        try {
            Uri uri = Uri.parse("content://telephony/carriers/preferapn");
            cur = context.getContentResolver().query(uri, null, null, null, null);
            if ((cur != null) && (cur.moveToFirst())) {
                String str = cur.getString(cur.getColumnIndex("proxy"));
                return str;
            }
        } finally {
            if (cur != null)
                cur.close();
        }
        return null;
    }

    /**
     * 获取当前网络类型
     * 
     * @param context
     * @return
     */
    public static String getNetworkType(Context context) {
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE))
                .getActiveNetworkInfo();
        if ((networkInfo != null) && (networkInfo.isAvailable())) {
            if (networkInfo.getTypeName().toLowerCase().equals("wifi"))
                return "wifi";
            return networkInfo.getExtraInfo().toLowerCase();
        }
        return "wifi not available";
    }

    /**
     * 获取代理IP
     * 
     * @param paramString
     * @param context
     * @return
     */
    public static String getProxyIp(String paramString, Context context) {
        if (paramString == null)
            return null;
        Cursor cur = null;
        try {
            Uri uri = Uri.parse("content://telephony/carriers");
            cur = context.getContentResolver().query(uri, null, null, null, null);
            String id;
            do {
                if (cur != null) {
                    boolean bool = cur.moveToNext();
                    if (bool)
                        ;
                } else {
                    return null;
                }
                id = cur.getString(cur.getColumnIndex("_id"));
            } while (!paramString.trim().equals(id));
            return cur.getString(cur.getColumnIndex("proxy"));
        } finally {
            if (cur != null)
                cur.close();
        }

    }

    /**
     * 获取WIFI信息
     * 
     * @param context
     * @return
     */
    public static WifiInfo getWifiStatus(Context context) {
        return ((WifiManager) context.getSystemService(Context.WIFI_SERVICE)).getConnectionInfo();
    }

    /**
     * 是否为cmwap网络
     * 
     * @param context
     * @return
     */
    public static boolean isCmwap(Context context) {
        if ((!isConnectChinaMobile(context)) || (!isMobileType(context)))
            return false;
        String str = getCurrentApnProxy(context);
        if (str == null)
            return false;
        return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
    }

    /**
     * 是否为中国移动
     * 
     * @param context
     * @return
     */
    public static boolean isConnectChinaMobile(Context context) {
        // MCC+MNC (mobile country code + mobile network code)
        String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
        if (str != null)
            return (str.startsWith("46000")) || (str.startsWith("46002"));
        return false;
    }

    /**
     * 是否为中国电信
     * 
     * @param context
     * @return
     */
    public static boolean isConnectChinaTelecom(Context context) {
        String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
        if (str != null)
            return str.startsWith("46003");
        return false;
    }

    /**
     * 是否为中国联通
     * 
     * @param context
     * @return
     */
    public static boolean isConnectChinaUnicom(Context context) {
        String str = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getSimOperator();
        if (str != null)
            return str.startsWith("46001");
        return false;
    }

    /**
     * 当前网络类型（Is Mobile）
     * 
     * @param context
     * @return
     */
    public static boolean isMobileType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null)
            return false;
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo == null)
            return false;
        return networkInfo.getTypeName().equalsIgnoreCase("mobile");
    }

    /**
     * Network Access point Uniwap
     * 
     * @param context
     * @return
     */
    public static boolean isUniwap(Context context) {
        if ((!isConnectChinaUnicom(context)) || (!isMobileType(context)))
            return false;
        String str = getCurrentApnProxy(context);
        if (str == null)
            return false;
        return (str.equals("10.0.0.172")) || (str.equals("010.000.000.172"));
    }

    /**
     * 手机-短-震动
     * 
     * @param context
     */
    public static void shotVibratePhone(Context context) {
        Vibrator vib = ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE));
        vib.vibrate(100L);
    }

    /**
     * 获取当前版本号，升级用
     * 
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo pi;
        int version = 1;
        try {
            pi = pm.getPackageInfo(context.getPackageName(), 0);
            version = pi.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    /**
     * 获取当前版本,升级用
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        String versionName = "";
        try {
            versionName = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * SDCard是否可用
     * 
     * @return
     */
    public static boolean existSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else
            return false;
    }

    /**
     *
     * @param context
     * @return
     */
    public static String getPhoneNumber(Context context){
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return mTelephonyMgr.getLine1Number();
    }




    /**获取手机唯一识别码
     * UUID+设备号序列号 唯一识别码（不可变）**/
    public static String getPhoneUUID(Context context){

        final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        final String tmDevice, tmSerial, tmPhone, androidId;

        tmDevice = "" + tm.getDeviceId();

        tmSerial = "" + tm.getSimSerialNumber();

        androidId = "" + android.provider.Settings.Secure.getString(context.getContentResolver(),android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());

        String uniqueId = deviceUuid.toString();


        return uniqueId;
    }
}
