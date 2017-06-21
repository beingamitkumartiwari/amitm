package com.getfreerecharge.mpaisafreerecharge.utils.app_constants;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import com.getfreerecharge.mpaisafreerecharge.storage.MyPrefs;

import java.util.Random;

/**
 * Created by DEVEN SINGH on 7/3/2015.
 */
public class AppConstants {

    public static final String GOOGLE_PROJ_ID = "873478001335";
    public static final String[] AMOUNT = {"50", "100"};
    public static final String[] OPERATOR = {"Airtel", "BSNL", "MTNL", "Reliance", "Idea", "Vodafone", "Aircel", "Tata Docomo", "Uninor", "MTS", "Videocon", "Loop Mobile"};


    public static String getImeiNum(Context context) {
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        if (identifier == null || identifier.length() == 0)
            identifier = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public static String getUserKey(Context context,MyPrefs myPrefs){
        String imei=AppConstants.getImeiNum(context);
        String key=imei.substring(0,3)+myPrefs.getUserDev()+imei.substring(imei.length()-3);
        return key;
    }

    public static String getReferralCode(Context context,MyPrefs myPrefs){
        String imei=AppConstants.getImeiNum(context);
        String refCode=myPrefs.getUserDev()+imei.substring(imei.length()-3);
        return refCode;
    }

    public static String getDeviceImeiNumber(Context context){
        String identifier = null;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (tm != null)
            identifier = tm.getDeviceId();
        return identifier;
    }

    public static String getDeviceAndroidId(Context context){
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    public static String getMacAddress(Context context){
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress();
    }

    public static String getAndroidVersion() {
//        int sdkVersion = Build.VERSION.SDK_INT;
        return Build.VERSION.RELEASE;
    }
    public static int getRandomnumForAd(int[] numbers){
        int rnd = new Random().nextInt(numbers.length);
        return numbers[rnd];
    }
}
