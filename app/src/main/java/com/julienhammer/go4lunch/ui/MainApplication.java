package com.julienhammer.go4lunch.ui;

import android.app.Application;
import com.google.android.libraries.places.api.Places;
import com.julienhammer.go4lunch.R;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainApplication extends Application {
    private static Application sApplication;
    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
        Places.initialize(this , getString(R.string.google_map_key));
    }

    public static Application getApplication() {
        return sApplication;
    }

}
