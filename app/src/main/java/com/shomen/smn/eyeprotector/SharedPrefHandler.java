package com.shomen.smn.eyeprotector;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefHandler {

    private SharedPreferences sharedPreferences;
    private String TAG = "asl_" + SharedPrefHandler.class.getSimpleName();
    private SharedPreferences.Editor editor;

    public SharedPrefHandler(Context context) {
//        this.context = context;
        sharedPreferences = context.getSharedPreferences(
                Constants.SHAREDPREFNAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setDefaultValues() {
        this.setARGB(50,0,0,0);
    }

    public void setARGB(int... values) {
        editor.putInt(Constants.ALPHA, values[0]);
        editor.putInt(Constants.RED, values[1]);
        editor.putInt(Constants.GREEN, values[2]);
        editor.putInt(Constants.BLUE, values[3]);

        editor.commit();
    }

    public int getAlpha() {
        return sharedPreferences.getInt(Constants.ALPHA, 0);
    }

    public int getRed() {
        return sharedPreferences.getInt(Constants.RED, 0);
    }

    public int getGreen() {
        return sharedPreferences.getInt(Constants.GREEN, 0);
    }

    public int getBlue() {
        return sharedPreferences.getInt(Constants.BLUE, 0);
    }

    public boolean isFirstTimeLaunched() {
        return sharedPreferences.getBoolean(Constants.FIRSTLAUNCH, true);
    }

    public void setFirstTimeLaunched(boolean b) {
        editor.putBoolean(Constants.FIRSTLAUNCH, b);
        editor.commit();
    }

    public void showValues(String tag) {
        int alpha = this.getAlpha();
        int red = this.getRed();
        int green = this.getGreen();
        int blue = this.getBlue();

        Log.d(tag, "A:" + alpha + " R:" + red + " G:" + green + " B:" + blue);
    }

    public void setCustomModeStartTime(int hour, int minute) {

        editor.putString(Constants.TAG_START_TIME_CUSTOM, new Util().intToTimeString24Hour(hour, minute));
        editor.commit();
    }

    public void setCustomModeStopTime(int hour, int minute) {

        editor.putString(Constants.TAG_STOP_TIME_CUSTOM, new Util().intToTimeString24Hour(hour, minute));
        editor.commit();
    }

    public String getCustomModeStartTime() {
        return sharedPreferences.getString(Constants.TAG_START_TIME_CUSTOM, "0:0");
    }

    public String getCustomModeStopTime() {
        return sharedPreferences.getString(Constants.TAG_STOP_TIME_CUSTOM, "0:0");
    }

    public void setUserProfile(UserProfile profile) {

        switch (profile) {

            case DEFAULT:
                editor.putString(Constants.TAG_PROFILE,UserProfile.DEFAULT.getVal());
                break;
            case NIGHT_MODE:
                editor.putString(Constants.TAG_PROFILE,UserProfile.NIGHT_MODE.getVal());
                break;
            case CUSTOM:
                editor.putString(Constants.TAG_PROFILE,UserProfile.CUSTOM.getVal());
                break;
        }

        editor.commit();
    }

    public UserProfile getSelectedUserProfile() {
        switch (sharedPreferences.getString(Constants.TAG_PROFILE, "1")) {

            case "2":
                return UserProfile.NIGHT_MODE;
            case "3":
                return UserProfile.CUSTOM;
            default:
                return UserProfile.DEFAULT;
        }
    }
}
