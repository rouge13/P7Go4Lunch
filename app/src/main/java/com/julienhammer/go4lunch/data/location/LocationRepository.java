package com.julienhammer.go4lunch.data.location;

import android.app.Application;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.maps.model.LatLng;
import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.ui.map.MapsFragment;

import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LocationRepository {
//    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
//    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

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
