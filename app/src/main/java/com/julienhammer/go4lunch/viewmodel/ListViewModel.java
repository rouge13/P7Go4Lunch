package com.julienhammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ListViewModel extends ViewModel {
    @NonNull
    private final PermissionCheck permissionCheck;

    @NonNull
    private final LocationRepository locationRepository;

    public ListViewModel(
            @NonNull PermissionCheck permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        this.permissionCheck = permissionChecker;
        this.locationRepository = locationRepository;
    }

//    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
//    public void onActivityCreated(@Nullable final Bundle SavedInstanceState){
//
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider((ViewModelStoreOwner) this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//    }

    public LiveData<Location> getLocationForPlacesLiveData() {
        return locationRepository.getLocationLiveData();
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        boolean hasGpsPermission = permissionCheck.hasLocationPermission();
//        hasGpsPermissionLiveData.setValue(hasGpsPermission);

        if (hasGpsPermission) {
            locationRepository.startLocationRequest();
        } else {
            locationRepository.stopLocationRequest();
        }
    }
}