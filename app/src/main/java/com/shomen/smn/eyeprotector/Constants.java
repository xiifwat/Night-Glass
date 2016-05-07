package com.shomen.smn.eyeprotector;


public class Constants {

    public static String SHAREDPREFNAME = "EyeProtectorSharedPref";
    public static String ALPHA = "alpha";
    public static String RED = "red";
    public static String GREEN = "green";
    public static String BLUE = "blue";
    public static String FIRSTLAUNCH = "amIVirgin";

    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "com.shomen.smn.eyeprotector.BackgroundService.action.startforeground";
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;
    }
}