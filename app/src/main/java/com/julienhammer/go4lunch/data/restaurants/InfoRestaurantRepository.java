package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.NearbySearch;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantRepository {
    private static volatile InfoRestaurantRepository instance;
    private static MutableLiveData<RestaurantDetails> mInfoRestaurantMutableLiveData;

    public InfoRestaurantRepository(){
        InfoRestaurantRepository.mInfoRestaurantMutableLiveData = new MutableLiveData<>();

    }
    public LiveData<RestaurantDetails> getInfoRestaurantLiveData() {
        return mInfoRestaurantMutableLiveData;
    }

    public void getInfoRestaurant(RestaurantDetails infoRestaurant) {
        if (infoRestaurant != null){
            mInfoRestaurantMutableLiveData.postValue(infoRestaurant);
        }
    }

    public static InfoRestaurantRepository getInstance(){

        InfoRestaurantRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (InfoRestaurantRepository.class){
            if (instance == null){
                instance = new InfoRestaurantRepository();
            }
            return instance;
        }
    }
}
