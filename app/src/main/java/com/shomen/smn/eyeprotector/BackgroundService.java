package com.shomen.smn.eyeprotector;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class BackgroundService extends Service {

    private final String TAG = "asl_" + this.getClass().getSimpleName();
    public static boolean isPaused = false;

    private IBinder mBinder = new ServiceBinder();
    private SharedPrefHandler sharedPrefHandler;

    private int alpha = 0, red = 0, green = 0, blue = 0;

    private WindowManager wm;
    private LinearLayout ll;

    @Override
    public IBinder onBind(Intent arg0){
        Log.d(TAG, "inside onbind....");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "inside onUnbind....");
        return true;
    }

    @Override
    public void onCreate() {

        Log.d(TAG, "onCreate");

        sharedPrefHandler = new SharedPrefHandler(this);
//        sharedPrefHandler.showValues();
        alpha = sharedPrefHandler.getAlpha();
        red = sharedPrefHandler.getRed();
        green = sharedPrefHandler.getGreen();
        blue = sharedPrefHandler.getBlue();

        wm = (WindowManager) getSystemService(WINDOW_SERVICE);

        ll = new LinearLayout(this);
        ll.setBackgroundColor(Color.BLACK);
        LinearLayout.LayoutParams layoutParameteres = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        ll.getBackground().setAlpha(alpha);
        ll.setBackgroundColor(Color.argb(alpha, red, green, blue));
        ll.setLayoutParams(layoutParameteres);

        final WindowManager.LayoutParams parameters = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ERROR,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                        | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                        | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                PixelFormat.TRANSLUCENT);
        parameters.gravity = Gravity.CENTER | Gravity.BOTTOM;
        parameters.x = 0;
        parameters.y = 0;

        wm.addView(ll, parameters);

        ll.setOnTouchListener(new View.OnTouchListener() {
            WindowManager.LayoutParams updatedParameters = parameters;
            double x;
            double y;
            double pressedX;
            double pressedY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        x = updatedParameters.x;
                        y = updatedParameters.y;

                        pressedX = event.getRawX();
                        pressedY = event.getRawY();

                        Log.d(TAG, "ACTION_DOWN " + pressedX + " <> " + pressedY);

                        break;

                    case MotionEvent.ACTION_MOVE:
                        updatedParameters.x = (int) (x + (event.getRawX() - pressedX));
                        updatedParameters.y = (int) (y + (event.getRawY() - pressedY));

                        Log.d(TAG, "ACTION_MOVE ");

                        wm.updateViewLayout(ll, updatedParameters);

                    default:
                        break;
                }

                return false;
            }
        });

        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
        /*if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {
            Log.d(TAG, "Received Start Foreground Intent ");
            showNotification();
        }*/
        showNotification();
        return START_STICKY;
    }

    private void showNotification() {
        Intent notificationIntent = new Intent(this, NotificationActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(getResources().getString(R.string.app_name))
//                .setTicker("Eye Protector")
                .setContentText("Click to change settings")
                .setSmallIcon(R.drawable.ic_glass_gray)
                .setPriority(Notification.PRIORITY_MIN)
                .setContentIntent(pendingIntent)
                .setOngoing(true).build();

        startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE,
                notification);

    }

    public void adjustBrightness(int val) {
        Log.d(TAG, "Alpha value: "  + val);
        ll.getBackground().setAlpha(val);
        ll.setBackgroundColor(Color.argb(val, red, green, blue));
        alpha = val;
    }

    public void adjustRed(int value) {
        int color = ((ColorDrawable) ll.getBackground()).getColor();
        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);

        Log.d(TAG, "A:" + alpha + " R:" + red + " G:" + green + " B:" + blue);

        ll.setBackgroundColor(Color.argb(alpha, value, green, blue));
    }

    public void increaseBrightnessGradually() {

        Log.d(TAG, "Inside method increaseBrightnessGradually");
        final Handler uiHandler = new Handler();

        final int[] argb = new int[4];
        argb[0] = alpha; argb[1] = red; argb[2] = green; argb[3] = blue;
        final Timer timer = new Timer();


        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                            if(argb[0]>0 || argb[1]>0 || argb[2]>0 || argb[3]>0) {
                                argb[0]-=1; argb[1]-=1; argb[2]-=1; argb[3]-=1;

                                if(argb[0]<0) argb[0] = 0;
                                if(argb[1]<0) argb[1] = 0;
                                if(argb[2]<0) argb[2] = 0;
                                if(argb[3]<0) argb[3] = 0;

                                ll.setBackgroundColor(Color.argb(argb[0], argb[1], argb[2], argb[3]));
                            } else {
                                // animation finished. So stop timer
                                ll.setVisibility(View.GONE);
                                timer.cancel();
                            }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(tt,0,10);

    }

    public void decreaseBrightnessGradually() {

        final Handler uiHandler = new Handler();

        final int[] argb = new int[4];
        argb[0] = argb[1] = argb[2] = argb[3] = 0;
        final Timer timer = new Timer();

        ll.setVisibility(View.VISIBLE);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(argb[0]<alpha || argb[1]<red || argb[2]<green || argb[3]<blue) {
                            argb[0]++; argb[1]++;// argb[2]++; argb[3]++;

                            if(argb[0]>alpha) argb[0] = alpha;
                            if(argb[1]>red) argb[1] = red;
//                            if(argb[2]<0) argb[2] = 0;
//                            if(argb[3]<0) argb[3] = 0;

                            ll.setBackgroundColor(Color.argb(argb[0], argb[1], argb[2], argb[3]));
                        } else {
                            // animation finished. So stop timer
                            timer.cancel();
                        }
                    }
                });
            }
        };

        timer.scheduleAtFixedRate(tt,0,10);

    }



    public void saveSettings() {
        sharedPrefHandler.setARGB(alpha, red, green, blue);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        wm.removeView(ll);
        this.saveSettings();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public class ServiceBinder extends Binder {
        BackgroundService getService() {
            return BackgroundService.this;
        }
    }

    @Subscribe
    public void onEvent(ExampleEvent event){
        Log.d(TAG,"event catched "+event.getMessage());
    }

    @Subscribe
    public void onEvent(String type){
        Log.d(TAG,"event catched "+type);
        sendNotification(type);

    }

    public void sendNotification(String type) {

        Calendar calendar = Calendar.getInstance();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Alarm Notifications ");
        builder.setContentText(type + " " +calendar.getTime().toString());
        builder.setContentIntent(null);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(getApplicationContext().NOTIFICATION_SERVICE);

        notificationManager.notify(calendar.get(Calendar.SECOND), builder.build());
    }

}
