package com.julienhammer.go4lunch.viewmodel;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;

import java.util.List;

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
