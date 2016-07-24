package com.shomen.smn.eyeprotector;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by server on 5/30/2016.
 */
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final String LOGTAG = "asl_"+this.getClass().getSimpleName();
    private static final String TYPE = "type";
    private String type;
    private final Calendar calender = Calendar.getInstance();
    private int hour,minute;

    public TimePickerFragment(){

    }

    public static TimePickerFragment newInstance(String param, int hour, int minute) {
        TimePickerFragment fragment = new TimePickerFragment();
        Bundle args = new Bundle();
        args.putString(TYPE, param);
        fragment.setArguments(args);

        args.putInt("hour", hour);
        args.putInt("minute", minute);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments() != null) {
            type = getArguments().getString(TYPE);
            hour = getArguments().getInt("hour");
            minute = getArguments().getInt("minute");
        }

        calender.set(Calendar.HOUR_OF_DAY,hour);
        calender.set(Calendar.MINUTE,minute);

/*        hour = calender.get(Calendar.HOUR_OF_DAY);
        minute = calender.get(Calendar.MINUTE);

        Log.d(LOGTAG,"hour"+hour+" minute "+minute+" type "+type);*/

        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d(LOGTAG,"onTimeSet time "+hourOfDay+" "+minute+" type "+type);
        FragmentInteractionListener timePicked = (FragmentInteractionListener) getTargetFragment();
        timePicked.onTimePicked(type,hourOfDay,minute);
    }

}