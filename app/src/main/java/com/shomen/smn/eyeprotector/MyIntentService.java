package com.shomen.smn.eyeprotector;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

public class MyIntentService extends IntentService {

    private String type;

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("ASL","MyIntentService onHandleIntent ...."+intent.getExtras().getString(MyAlarmReceiver.TYPE));
        type = intent.getExtras().getString(MyAlarmReceiver.TYPE);

        EventBus.getDefault().post(new ExampleEvent("Hello World Event!"));
        EventBus.getDefault().post(type);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("ASL","MyIntentService onDestroy ....");
    }
}
