package julien.hammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;

import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LocationViewModel {

    @NonNull
    private final PermissionCheck permissionCheck;

    @NonNull
    private final LocationRepository locationRepository;

    private final LiveData<Double> mUserLatitude;


    public LocationViewModel(
            @NonNull PermissionCheck permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        this.permissionCheck = permissionChecker;
        this.locationRepository = locationRepository;

        gpsMessageLiveData = Transformations.map(locationRepository.getLocationLiveData(), location -> {
            if (location == null) {
                return "Je suis perdu...";
            } else {
                return "Je suis aux coordonnées (" + location.getLatitude() + "," + location.getLongitude() + ")";
            }
        });
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        // No GPS permission
        if (!permissionCheck.hasLocationPermission()) {
            locationRepository.stopLocationRequest();
        } else {
            locationRepository.startLocationRequest();
        }
    }

    public LiveData<String> getGpsMessageLiveData() {
        return gpsMessageLiveData;
    }
}
