package com.julienhammer.go4lunch.data.location;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.Task;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LocationRepository {
    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private LocationCallback callback;
    public LocationRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        Task<Location> location = fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                Location userLocation = (Location) task.getResult();
                if (userLocation == null){
                    userLocation = new Location("network");
                    userLocation.setLatitude(48.5735);
                    userLocation.setLongitude(7.7523);
                }
                locationMutableLiveData.postValue(userLocation);
            }
        });
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }
}
