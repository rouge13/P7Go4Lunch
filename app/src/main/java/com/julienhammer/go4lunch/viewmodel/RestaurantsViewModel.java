package com.julienhammer.go4lunch.viewmodel;

import android.location.Location;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.RestaurantAutoComplete;

import java.util.Arrays;

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
