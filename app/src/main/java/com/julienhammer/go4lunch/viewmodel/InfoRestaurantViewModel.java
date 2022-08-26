package com.julienhammer.go4lunch.viewmodel;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.RestaurantDetails;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantViewModel extends ViewModel {
    @NonNull
    InfoRestaurantRepository mInfoRestaurantRepository;
    public InfoRestaurantViewModel() {
        mInfoRestaurantRepository = InfoRestaurantRepository.getInstance();
    }

    public LiveData<RestaurantDetails> getInfoRestaurantLiveData() {
        return mInfoRestaurantRepository.getInfoRestaurantLiveData();
    }

    public void getInfoRestaurant(RestaurantDetails infoRestaurant){
        mInfoRestaurantRepository.getInfoRestaurant(infoRestaurant);

    }
}
