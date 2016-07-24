package com.shomen.smn.eyeprotector;


public class Constants {

    public static String SHAREDPREFNAME = "EyeProtectorSharedPref";
    public static String ALPHA = "alpha";
    public static String RED = "red";
    public static String GREEN = "green";
    public static String BLUE = "blue";
    public static String FIRSTLAUNCH = "amIVirgin";

    public static String TAG_PROFILE = "userProfile";
    public static String TAG_START_TIME_NIGHT = "START_TIME_NIGHT";
    public static String TAG_STOP_TIME_NIGHT = "STOP_TIME_NIGHT";
    public static String TAG_START_TIME_CUSTOM = "START_TIME_CUSTOM";
    public static String TAG_STOP_TIME_CUSTOM = "STOP_TIME_CUSTOM";


    public interface ACTION {
        String STARTFOREGROUND_ACTION = "com.shomen.smn.eyeprotector.BackgroundService.action.startforeground";
    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }
}