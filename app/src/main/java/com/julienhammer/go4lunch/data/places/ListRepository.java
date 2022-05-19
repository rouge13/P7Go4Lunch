package com.julienhammer.go4lunch.data.places;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ListRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;


    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<Location> locationForPlacesMutableLiveData = new MutableLiveData<>(null);

    private LocationCallback callback;

    public ListRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }



    public LiveData<Location> getLocationForPlacesLiveData() {
        return locationForPlacesMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        Task<Location> location= fusedLocationProviderClient.getLastLocation();
        location.addOnCompleteListener(new OnCompleteListener<Location>(){
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if(task.isSuccessful()) {
                    Location userLocation = (Location) task.getResult();
                    locationForPlacesMutableLiveData.setValue(userLocation);
                }
            }
        });
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }
}
