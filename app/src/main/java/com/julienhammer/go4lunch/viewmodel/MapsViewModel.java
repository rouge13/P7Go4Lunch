package com.julienhammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import java.util.concurrent.Executor;

import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
import com.julienhammer.go4lunch.di.ViewModelFactory;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MapsViewModel extends ViewModel {

    @NonNull
    private final PermissionCheck permissionCheck;

    @NonNull
    private final LocationRepository locationRepository;

//    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
//    public void onActivityCreated(@Nullable final Bundle SavedInstanceState){
//
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider((ViewModelStoreOwner) this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//    }

    public MapsViewModel(
            @NonNull PermissionCheck permissionChecker,
            @NonNull LocationRepository locationRepository
    ) {
        this.permissionCheck = permissionChecker;
        this.locationRepository = locationRepository;
        LiveData<Location> locationLiveData = locationRepository.getLocationLiveData();
    }

    public LiveData<Location> getLocationLiveData() {
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
