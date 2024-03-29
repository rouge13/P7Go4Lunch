package com.julienhammer.go4lunch.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.models.User;

import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantViewModel extends ViewModel {
    @NonNull
    public
    InfoRestaurantRepository mInfoRestaurantRepository;
    public InfoRestaurantViewModel() {
        mInfoRestaurantRepository = InfoRestaurantRepository.getInstance();
    }

    public void initRestaurantsDetailsInfo(String placeId){
        mInfoRestaurantRepository.initRestaurantsDetailsInfo(placeId);
    }

    public LiveData<Place> getRestaurantDetailsInfoLiveData(){
        return mInfoRestaurantRepository.getRestaurantDetailsInfoLiveData();
    }

    public LiveData<Bitmap> getRestaurantPhotoBitmap(){
        return mInfoRestaurantRepository.getRestaurantPhotoBitmap();
    }

    public void initPlacesClientInfo(Context context){
        mInfoRestaurantRepository.initPlacesClientInfo(context);
    }

    public void initAllWorkmatesInThisRestaurantMutableLiveData(String restaurantId){
        mInfoRestaurantRepository.initAllWorkmatesInThisRestaurantMutableLiveData(restaurantId);
    }

    public LiveData<List<User>> getAllWorkmatesInThisRestaurantLiveData(){
        return mInfoRestaurantRepository.getAllWorkmatesInThisRestaurantLiveData();
    }

    public  LiveData<Integer>  getCountWorkmatesForRestaurant(String placeId){
        return mInfoRestaurantRepository.countWorkmatesForRestaurant(placeId);
    }

    public LiveData<Integer> casesOfStars(Double rating){
        return mInfoRestaurantRepository.casesOfStars(rating);
    }

    public LiveData<Integer> distanceFromLocation(LatLng location, LatLng restaurantLocation) {
        return mInfoRestaurantRepository.distanceFromLocation(location, restaurantLocation);
    }

}
