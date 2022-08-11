package com.julienhammer.go4lunch.data;

import android.app.Application;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.utils.NearbySearch;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsRepository {
    private FusedLocationProviderClient fusedLocationProviderClient;

    LocationRepository mLocationRepository;

    private final MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData;

    public RestaurantsRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient){
        this.fusedLocationProviderClient = fusedLocationProviderClient;
        mRestaurantMutableLiveData = new MutableLiveData<>();

    }
    public LiveData<PlacesSearchResult[]> getRestaurantsLiveData() {
        return mRestaurantMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void getAllRestaurants(String apiKey) {
        PlacesSearchResult[] results;
        Task<Location> location = fusedLocationProviderClient.getLastLocation();
        final Location[] userLocation = new Location[0];
        location.addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                userLocation[0] = (Location) task.getResult();
                if (userLocation[0] == null){
                    userLocation[0] = new Location("network");
                    userLocation[0].setLatitude(48.5735);
                    userLocation[0].setLongitude(7.7523);
                }
            }
        });
        if (userLocation[0] != null && apiKey != null){
            results = new NearbySearch().run(apiKey,userLocation[0]).results;
            mRestaurantMutableLiveData.setValue(results);

        }
    }

}
