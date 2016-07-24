package com.shomen.smn.eyeprotector;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private String TAG = "asl_" + this.getClass().getSimpleName();

    public String intToTimeString24Hour(int hour, int minute) {
        // Convert 7:1 to 07:01
        DateFormat format = new SimpleDateFormat("HH:mm");
        Date date = null;
        try {
            date = format.parse(hour + ":" + minute);
//            Log.d(TAG, format.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return format.format(date);
    }

    public String timeString24ToTimeString12(String temp) {

        String[] t1 = temp.split(":");
        int hour = Integer.parseInt(t1[0]);
        int minute = Integer.parseInt(t1[1]);

        if(hour > 12){
            return String.format("%02d:%02d %s", hour-12, minute, "PM");
        } else {
            return String.format("%02d:%02d %s", hour, minute, "AM");
        }
    }
}
