package com.shomen.smn.eyeprotector;

import android.app.Application;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class AppController extends Application {

    public static final String TAG = AppController.class
            .getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Iconify.with(new FontAwesomeModule());

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/RobotoCondensed-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .addCustomStyle(TextField.class, R.attr.textFieldStyle)
                .build());
    }

}
