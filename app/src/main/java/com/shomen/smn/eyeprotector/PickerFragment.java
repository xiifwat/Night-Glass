package com.shomen.smn.eyeprotector;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class PickerFragment extends DialogFragment implements FragmentInteractionListener{

    private String TAG = "asl_" + this.getClass().getSimpleName();
    private int fromHour,fromMinute,toHour,toMinute;
    private View rootView;
    private TextView tvStart, tvStop;

    public static PickerFragment newInstance() {
        PickerFragment fragment = new PickerFragment();
        fragment.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.picker_fragment, container, false);
        getDialog().setTitle("Simple Dialog");

        tvStart = (TextView) rootView.findViewById(R.id.tvStartAt);
        tvStop = (TextView) rootView.findViewById(R.id.tvStopAt);
        setInitialTime();

        setDefaultTime();

        Button btnStartAt = (Button) rootView.findViewById(R.id.btn_start_at);
        btnStartAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment fromFragment = TimePickerFragment.newInstance(MyAlarmReceiver.START_AT,
                        fromHour,fromMinute);
                fromFragment.setTargetFragment(PickerFragment.this,0);
                fromFragment.show(getFragmentManager(), "");
            }
        });

        Button btnStopAt = (Button) rootView.findViewById(R.id.btn_stop_at);
        btnStopAt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment toFragment = TimePickerFragment.newInstance(MyAlarmReceiver.STOP_AT,
                        toHour,toMinute);
                toFragment.setTargetFragment(PickerFragment.this,0);
                toFragment.show(getFragmentManager(), "");
            }
        });

        Button btnSet = (Button) rootView.findViewById(R.id.btn_set);
        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentInteractionListener listener = (FragmentInteractionListener) getActivity();
                listener.onSetButtonClicked(fromHour,fromMinute,toHour,toMinute);
                dismiss();
            }
        });

        return rootView;
    }

    /* use this function to setup initial value for
     * fromHour,fromMinute,toHour,toMinute from sharedPref
     */
    private void setInitialTime() {

        Util util = new Util();

        SharedPrefHandler handler = new SharedPrefHandler(getContext());
        String startTime = util.timeString24ToTimeString12(handler.getCustomModeStartTime());
        String stopTime = util.timeString24ToTimeString12(handler.getCustomModeStopTime());

        tvStart.setText(startTime);
        tvStop.setText(stopTime);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
//        Log.d(TAG,"onDismiss");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        Log.d(TAG,"onDestroy");
    }

    private void setDefaultTime(){

        SharedPrefHandler handler = new SharedPrefHandler(getContext());
        String startTime = handler.getCustomModeStartTime();
        String[] t1 = startTime.split(":");
        fromHour = Integer.parseInt(t1[0]);
        fromMinute = Integer.parseInt(t1[1]);

        String stopTime = handler.getCustomModeStopTime();
        String[] t2 = stopTime.split(":");
        toHour = Integer.parseInt(t2[0]);
        toMinute = Integer.parseInt(t2[1]);
    }

    @Override
    public void onTimePicked(String type, int hourOfDay, int minute) {
//        Log.d(TAG,"type "+type+" hh "+hourOfDay+" mm "+minute);
        Util util = new Util();
        String temp = util.intToTimeString24Hour(hourOfDay,minute);
        temp = util.timeString24ToTimeString12(temp);

        if(type.equalsIgnoreCase(MyAlarmReceiver.START_AT)) {
            fromHour = hourOfDay;
            fromMinute = minute;
            tvStart.setText(temp);
        } else {
            toHour = hourOfDay;
            toMinute = minute;
            tvStop.setText(temp);
        }

    }

    @Override
    public void onSetButtonClicked(int fh, int fm, int th, int tm) {

        // This method will never be called.
        // Overridden methos in MainActivity will be called
    }
}
