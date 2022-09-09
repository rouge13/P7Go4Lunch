package com.julienhammer.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
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

    public void setInfoRestaurant(RestaurantDetails infoRestaurant){
        mInfoRestaurantRepository.setInfoRestaurant(infoRestaurant);

    }
}
