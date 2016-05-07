package com.shomen.smn.eyeprotector;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPrefHandler {

    private SharedPreferences sharedPreferences;
//    private Context context;
    private String TAG = "tfx_" + SharedPrefHandler.class.getSimpleName();
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
}
