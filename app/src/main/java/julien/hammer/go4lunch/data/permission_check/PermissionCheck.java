package julien.hammer.go4lunch.data.permission_check;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class PermissionCheck {
    @NonNull
    private final Application application;

    public PermissionCheck(@NonNull Application application) {
        this.application = application;
    }

    public boolean hasLocationPermission() {
        return ContextCompat.checkSelfPermission(application, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }
}
