package julien.hammer.go4lunch;

import android.app.Application;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainApplication extends Application {
    private static Application sApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        sApplication = this;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
