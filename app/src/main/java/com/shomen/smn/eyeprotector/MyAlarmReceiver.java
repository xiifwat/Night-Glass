package com.shomen.smn.eyeprotector;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MyAlarmReceiver extends BroadcastReceiver {

    private final String LOGTAG = "asl_"+this.getClass().getSimpleName();

    public static final int START_AT_REQUEST_CODE = 101;
    public static final int STOP_AT_REQUEST_CODE = 102;
    public static final String ACTION = "com.shomen.smn.eyeprotector.alarm";
    public static final  String TYPE = "TYPE";
    public static final  String START_AT = "START_AT";
    public static final  String STOP_AT = "STOP_AT";

    private String type = "not set";

    public MyAlarmReceiver() {

    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Calendar calendar = Calendar.getInstance();
        type = intent.getExtras().getString(TYPE);
        Log.d(LOGTAG,"Alarm recieved...."+intent.getExtras().getString("SMN")+" time "+calendar.getTime() );
        Toast.makeText(context,"Alart Alart "+intent.getExtras().getString("SMN"),Toast.LENGTH_SHORT).show();
        sendNotification(context);
        Intent i = new Intent(context, MyIntentService.class);

        i.putExtra(TYPE, type);
        context.startService(i);
    }

    public void sendNotification(Context context) {

        Calendar calendar = Calendar.getInstance();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Alarm Notifications ");
        builder.setContentText(type + " " +calendar.getTime().toString());
        builder.setContentIntent(null);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        notificationManager.notify(calendar.get(Calendar.SECOND), builder.build());
    }

}
