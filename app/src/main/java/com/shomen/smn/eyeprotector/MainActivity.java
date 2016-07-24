package com.shomen.smn.eyeprotector;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Calendar;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
        implements FragmentInteractionListener {

    private String TAG = "asl_" + this.getClass().getSimpleName();

    private TextView tvBtnIns;
    private RadioGroup radioGroup;
    private Button btnSwitch, btnPickerFragment;
    private SeekBar seekBarBrightness, seekBarRed;
    private SharedPrefHandler handler;
    private boolean isServiceConnected = false;
    private BackgroundService backgroundService;


    // -------------------------------Service connector class------------------------------//

    private ServiceConnection myServiceConnector = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            BackgroundService.ServiceBinder binder = (BackgroundService.ServiceBinder) service;
            backgroundService = binder.getService();
            isServiceConnected = true;
            Log.d(TAG, "service connected");
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.d(TAG, "service disconnected");
        }
    };

    //-------------------------------------------------------------------------------------//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new SharedPrefHandler(this);

        AlarmScheduler scheduler = new AlarmScheduler(this);

        inItComponent();
        setListenerToViews();
        selectUserProfile();

        if (isMyServiceRunning(BackgroundService.class)) {
            btnSwitch.setText("Stop Service");
            seekBarBrightness.setEnabled(true);
            seekBarRed.setEnabled(true);
        } else {
            btnSwitch.setText("Start Service");
            seekBarBrightness.setEnabled(false);
            seekBarRed.setEnabled(false);
        }

        if (handler.isFirstTimeLaunched()) {
            //TODO display wizard
            handler.setDefaultValues();
            handler.setFirstTimeLaunched(false);
        }
    }

    private void inItComponent() {
        btnSwitch = (Button) findViewById(R.id.btn_service);
        btnPickerFragment = (Button) findViewById(R.id.btnPickerFragment);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.check(R.id.radioDefault);
        tvBtnIns = (TextView) findViewById(R.id.tvBtnIns);
        seekBarBrightness = (SeekBar) findViewById(R.id.seekBarBrightness);
        seekBarRed = (SeekBar) findViewById(R.id.seekBarRed);

        seekBarBrightness.setMax(200);
        seekBarBrightness.setProgress(handler.getAlpha());
        seekBarRed.setMax(100);
        seekBarRed.setProgress(handler.getRed());
    }

    private void setListenerToSeekBar() {
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (backgroundService != null)
                    backgroundService.adjustBrightness(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (backgroundService != null) {
                    backgroundService.saveSettings();
                } else {
                    // TODO store value to sharedPref
                }
            }
        });
        seekBarRed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (backgroundService != null)
                    backgroundService.adjustRed(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (backgroundService != null)
                    backgroundService.saveSettings();
            }
        });
    }

    private void setListenerToViews() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioDefault:
                        handler.setUserProfile(UserProfile.DEFAULT);
                        pickerButtonActivator(false, getString(R.string.default_mode_text), "Always");
                        break;
                    case R.id.radioNight:
                        handler.setUserProfile(UserProfile.NIGHT_MODE);
                        pickerButtonActivator(false, getString(R.string.night_mode_text), "Night Mode");
                        break;
                    case R.id.radioCustom:
                        handler.setUserProfile(UserProfile.CUSTOM);
                        String btnTxt =
                                new Util().timeString24ToTimeString12(handler.getCustomModeStartTime())
                                        + " to " +
                                        new Util().timeString24ToTimeString12(handler.getCustomModeStopTime());
                        pickerButtonActivator(true,getString(R.string.custom_mode_text), btnTxt);
                        break;
                }
            }
        });

        btnPickerFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();
                PickerFragment dialogFragment = PickerFragment.newInstance();
                dialogFragment.show(fm, "Sample Fragment");

            }
        });

        btnSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isMyServiceRunning(BackgroundService.class)) {
                    Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                    stopService(intent);
                    if (isServiceConnected) unbindService(myServiceConnector);
                    btnSwitch.setText("Start");

                    seekBarBrightness.setEnabled(false);
                    seekBarRed.setEnabled(false);
                } else {
                    Intent intent = new Intent(MainActivity.this, BackgroundService.class);
                    intent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
                    bindService(intent, myServiceConnector, Context.BIND_AUTO_CREATE);
                    startService(intent);

                    setListenerToSeekBar();
                    seekBarBrightness.setEnabled(true);
                    seekBarRed.setEnabled(true);

                    btnSwitch.setText("Stop");
                }
            }
        });
    }

    private void selectUserProfile() {
        UserProfile profile = handler.getSelectedUserProfile();
        RadioButton rb = null;

        Log.d(TAG, "selectUserProfile called" + profile.name());

        switch (profile) {

            case DEFAULT:
                rb = (RadioButton) findViewById(R.id.radioDefault);
                pickerButtonActivator(false, getString(R.string.default_mode_text), "Always");
                break;
            case NIGHT_MODE:
                rb = (RadioButton) findViewById(R.id.radioNight);
                pickerButtonActivator(false, getString(R.string.night_mode_text), "Night Mode");
                break;
            case CUSTOM:
                String btnTxt =
                        new Util().timeString24ToTimeString12(handler.getCustomModeStartTime())
                                + " to " +
                                new Util().timeString24ToTimeString12(handler.getCustomModeStopTime());
                rb = (RadioButton) findViewById(R.id.radioCustom);
                pickerButtonActivator(true, getString(R.string.custom_mode_text), btnTxt);
                        break;
        }

        rb.setChecked(true);
    }


    @Override
    protected void onDestroy() {
        try {
            if (isServiceConnected) unbindService(myServiceConnector);
        } catch (Exception e) {}

        super.onDestroy();
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

    private void pickerButtonActivator(boolean toggle, String tvText, String btnTxt) {

        btnPickerFragment.setEnabled(toggle);
        tvBtnIns.setText(tvText);
        btnPickerFragment.setText(btnTxt);
    }

    public void scheduleAlarm() {

        Calendar myCalenderInstance = Calendar.getInstance();
        Log.d(TAG, "getTime " + myCalenderInstance.getTime().toString() + " getTimeInMill " + myCalenderInstance.getTimeInMillis());

        Calendar start = Calendar.getInstance();
        Calendar stop = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        start.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 5);

        stop.set(Calendar.HOUR_OF_DAY, now.get(Calendar.HOUR_OF_DAY));
        stop.set(Calendar.MINUTE, now.get(Calendar.MINUTE) + 10);

        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent.putExtra("SMN", "DAS");

        Intent intent_t = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        intent_t.putExtra("SMN", "from Second");

        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final PendingIntent pIntent_t = PendingIntent.getBroadcast(this, 0,
                intent_t, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 8000, pIntent);

//        alarm.setExact(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), pIntent);
//        alarm.setExact(AlarmManager.RTC_WAKEUP, stop.getTimeInMillis(), pIntent_t);
    }

    @Override
    public void onTimePicked(String type, int hh, int mm) {

    }

    @Override
    public void onSetButtonClicked(int fh, int fm, int th, int tm) {
//        Log.d(TAG, " fh : " + fh + " fm : " + fm + " th " + th + " tm " + tm);

        // Save selected time to shared preferences
        SharedPrefHandler handler = new SharedPrefHandler(this);

        handler.setCustomModeStartTime(fh, fm);
        handler.setCustomModeStopTime(th, tm);

        // to refresh view
        selectUserProfile();

        // TODO set alarm
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
