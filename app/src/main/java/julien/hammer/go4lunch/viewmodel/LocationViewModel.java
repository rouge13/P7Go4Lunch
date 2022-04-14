package julien.hammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.concurrent.Executor;

import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LocationViewModel extends ViewModel {

    @NonNull
    private final PermissionCheck permissionCheck;

    @NonNull
    private final LocationRepository locationRepository;

    public LocationViewModel(
            @NonNull PermissionCheck permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        this.permissionCheck = permissionChecker;
        this.locationRepository = locationRepository;

    }

    public LiveData<Location> getLocationLiveData() {
        return locationRepository.getLocationLiveData();
    }
}
