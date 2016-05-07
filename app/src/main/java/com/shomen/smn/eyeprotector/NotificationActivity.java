package com.shomen.smn.eyeprotector;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

public class NotificationActivity extends Activity {

    private String TAG = "tfx_" + this.getClass().getSimpleName();

    private  BackgroundService backgroundService;
    private boolean isServiceConnected = false;

    // -------------------------------Service connector class------------------------------//

    private ServiceConnection myServiceConnector= new ServiceConnection() {

        public void onServiceConnected(ComponentName className,IBinder service) {
            BackgroundService.ServiceBinder binder = (BackgroundService.ServiceBinder) service;
            backgroundService = binder.getService();
            isServiceConnected = true;
            Log.d(TAG, "service connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG,"service disconnected");
        }
    };

    //-------------------------------------------------------------------------------------//


    private Button btnStop, btnPause;
    private SeekBar seekBarBrightness, seekBarRed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_notification);

        btnStop = (Button) findViewById(R.id.btn_stop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationActivity.this,MainActivity.class));
                finish();
            }
        });


        btnPause = (Button) findViewById(R.id.btn_pause);

        if(BackgroundService.isPaused) btnPause.setText(getResources().getString(R.string.resume));
        else btnPause.setText(getResources().getString(R.string.pause));

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(BackgroundService.isPaused) {
                    // resume it
                    backgroundService.decreaseBrightnessGradually();
                    BackgroundService.isPaused = false;
                } else {
                    // pause it
                    backgroundService.increaseBrightnessGradually();
                    BackgroundService.isPaused = true;
                }
                finish();

            }
        });

        SharedPrefHandler handler = new SharedPrefHandler(this);

        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBrightness);
        seekBarBrightness.setMax(200);
        seekBarBrightness.setProgress(handler.getAlpha());
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundService.adjustBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                backgroundService.saveSettings();
            }
        });

        seekBarRed = (SeekBar) findViewById(R.id.seekBarRed);
        seekBarRed.setMax(100);
        seekBarRed.setProgress(handler.getRed());
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                backgroundService.adjustRed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                backgroundService.saveSettings();
                new SharedPrefHandler(NotificationActivity.this).showValues(TAG);
            }
        });

        Intent intent = new Intent(NotificationActivity.this, BackgroundService.class);
        bindService(intent, myServiceConnector, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
//        Log.d(TAG,"inside onDestroy....");
        if (isServiceConnected) unbindService(myServiceConnector);
        super.onDestroy();
    }

    @Override
    public void finish() {
//        Log.d(TAG,"inside finish.....");
        super.finish();
    }

}
