package com.julienhammer.go4lunch.viewmodel;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsViewModel extends ViewModel {

    @NonNull
    RestaurantsRepository mRestaurantsRepository;
    public RestaurantsViewModel() {
        mRestaurantsRepository = RestaurantsRepository.getInstance();
    }

    public LiveData<PlacesSearchResult[]> getRestaurantsLiveData() {
       return mRestaurantsRepository.getRestaurantsLiveData();
    }

    public void getAllRestaurants(String apiKey, Location userLocation){
        mRestaurantsRepository.getAllRestaurants(apiKey, userLocation);
    }



}
