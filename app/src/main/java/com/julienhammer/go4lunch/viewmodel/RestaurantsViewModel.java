package com.julienhammer.go4lunch.viewmodel;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
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

    public void initIsSomeoneEatingThere(String resId){
        mRestaurantsRepository.initIsSomeoneEatingThere(resId);
    }

    public LiveData<Boolean> getIfEatingHere(){
        return mRestaurantsRepository.getIfEatingHere();
    }

    public LiveData<List<String>> getAllRestaurantChoosed(){
        return mRestaurantsRepository.getAllRestaurantChoosed();
    }

    public void initAllRestaurantChoosed() {
        mRestaurantsRepository.initAllRestaurantChoosed();
    }

    public void getAllRestaurants(String apiKey, Location userLocation){
        mRestaurantsRepository.getAllRestaurants(apiKey, userLocation);
    }

}
