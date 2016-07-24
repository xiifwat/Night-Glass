package com.shomen.smn.eyeprotector;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by server on 6/1/2016.
 */
public class AlarmScheduler {

    private final String LOGTAG = "asl_"+this.getClass().getSimpleName();

    private Context context;

    public AlarmScheduler(Context context) {
        this.context = context;
    }

    //not completed
    public void scheduleAlarm(int fh, int fm, int th, int tm){

        Log.d("ASL","inside scheduleAlarm ");

        Calendar from_calender = Calendar.getInstance();
        Calendar to_calender = Calendar.getInstance();

        from_calender.set(Calendar.HOUR_OF_DAY,fh);
        from_calender.set(Calendar.MINUTE,fm);

        to_calender.set(Calendar.HOUR_OF_DAY,th);
        to_calender.set(Calendar.MINUTE,tm);

    }

    public void scheduleAlarm(int hh, int mm, String type){

//        Log.d("ASL","inside scheduleAlarm ");

        Calendar calender = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        calender.set(Calendar.HOUR_OF_DAY,hh);
        calender.set(Calendar.MINUTE,mm);

        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra("TYPE",type);

        int request_code;

        if(type.equalsIgnoreCase(MyAlarmReceiver.START_AT)){
            request_code = MyAlarmReceiver.START_AT_REQUEST_CODE;
        }else{
            request_code = MyAlarmReceiver.STOP_AT_REQUEST_CODE;
        }

        PendingIntent pend_intent =  PendingIntent.getBroadcast(context, request_code,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(calender.before(now)){
//            alarm.setExact(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),pend_intent);
//            calender.add(Calendar.DATE,1);
        }

        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP,calender.getTimeInMillis(),
                AlarmManager.INTERVAL_FIFTEEN_MINUTES,pend_intent);

    }

    //not completed
    public void scheduleAlarm() {

        /*Calendar myCalenderInstance = Calendar.getInstance();
        Log.d("ASL","getTime "+myCalenderInstance.get(Calendar.MINUTE)+" getTimeInMill "+myCalenderInstance.getTimeInMillis());

        Calendar start = Calendar.getInstance();
        Calendar stop = Calendar.getInstance();
        Calendar now = Calendar.getInstance();

        start.set(Calendar.HOUR_OF_DAY,now.get(Calendar.HOUR_OF_DAY));
        start.set(Calendar.MINUTE,now.get(Calendar.MINUTE)+5);

        stop.set(Calendar.HOUR_OF_DAY,now.get(Calendar.HOUR_OF_DAY));
        stop.set(Calendar.MINUTE,now.get(Calendar.MINUTE)+10);

        Intent intent = new Intent(context, MyAlarmReceiver.class);
        intent.putExtra("SMN","DAS");

        Intent intent_t = new Intent(context, MyAlarmReceiver.class);
        intent_t.putExtra("SMN","from Second");

        final PendingIntent pIntent = PendingIntent.getBroadcast(context, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        final PendingIntent pIntent_t = PendingIntent.getBroadcast(context, 0,
                intent_t, PendingIntent.FLAG_UPDATE_CURRENT);

        long firstMillis = System.currentTimeMillis();

//        alarm.setExact(AlarmManager.RTC_WAKEUP, start.getTimeInMillis(), pIntent);
//        alarm.setExact(AlarmManager.RTC_WAKEUP, stop.getTimeInMillis(), pIntent_t);*/

//        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

//        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 8000, pend_intent);
    }

    public void cancelAlarm() {
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, MyAlarmReceiver.class);
        PendingIntent pend_intent_start =  PendingIntent.getBroadcast(context, MyAlarmReceiver.START_AT_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pend_intent_stop =  PendingIntent.getBroadcast(context, MyAlarmReceiver.STOP_AT_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarm.cancel(pend_intent_start);
        alarm.cancel(pend_intent_stop);
        Log.d(LOGTAG,"Alarm cancelled....");
    }
}
