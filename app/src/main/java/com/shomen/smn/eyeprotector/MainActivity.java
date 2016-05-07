package com.shomen.smn.eyeprotector;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();

    Button btnSwitch;

    private BackgroundService backgroundService;



    // -------------------------------Service connector class------------------------------//

    private ServiceConnection myServiceConnector= new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder service) {
            BackgroundService.ServiceBinder binder = (BackgroundService.ServiceBinder) service;
            backgroundService = binder.getService();
            Log.d(TAG, "service connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG,"service disconnected");
        }
    };

    //-------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSwitch = (Button) findViewById(R.id.btn_service);

        if(isMyServiceRunning(BackgroundService.class))
            btnSwitch.setText("Stop Service");
        else
            btnSwitch.setText("Start Service");

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isMyServiceRunning(BackgroundService.class)){
                    Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                    stopService(intent);
                    btnSwitch.setText("Start Service");
                }else{
                    Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                    intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
//                    bindService(intent, myServiceConnector, Context.BIND_AUTO_CREATE);
                    startService(intent);

                    startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                    btnSwitch.setText("Stop Service");
                }

            }
        });

        SharedPrefHandler handler = new SharedPrefHandler(this);

        if(handler.isFirstTimeLaunched()) {
            //TODO display wizard
            handler.setDefaultValues();
            handler.setFirstTimeLaunched(false);
        }

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
