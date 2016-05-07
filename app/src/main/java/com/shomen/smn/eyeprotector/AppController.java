package com.shomen.smn.eyeprotector;

import android.app.Application;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());
    }

}
