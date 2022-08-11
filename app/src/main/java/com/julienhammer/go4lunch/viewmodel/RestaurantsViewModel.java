package com.julienhammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.RestaurantsRepository;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsViewModel extends ViewModel {
    @NonNull
    private final LocationRepository locationRepository;
    @NonNull
    RestaurantsRepository mRestaurantsRepository;
    public RestaurantsViewModel(
            @NonNull LocationRepository locationRepository
    ) {
        this.locationRepository = locationRepository;
    }

    public LiveData<PlacesSearchResult[]> getRestaurantsLiveData() {
       return mRestaurantsRepository.getRestaurantsLiveData();
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void getAllRestaurants(String apiKey){
        mRestaurantsRepository.getAllRestaurants(apiKey);
    }

}
