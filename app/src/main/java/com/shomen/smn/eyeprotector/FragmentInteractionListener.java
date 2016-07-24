package com.shomen.smn.eyeprotector;

/**
 * Created by server on 5/31/2016.
 */
public interface FragmentInteractionListener {
    void onTimePicked(String type, int hh, int mm);

    void onSetButtonClicked(int fh, int fm, int th, int tm);
}
