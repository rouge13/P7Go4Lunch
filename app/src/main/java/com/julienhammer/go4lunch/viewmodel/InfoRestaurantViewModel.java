package com.julienhammer.go4lunch.viewmodel;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;

import java.util.List;

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
//    public void createRestaurantLiked(String restaurantPlaceId, FirebaseUser userId){
//        mInfoRestaurantRepository.createRestaurantLiked(restaurantPlaceId, userId);
//    }

//    public void setRestaurantLikes()

    public void initRestaurantsDetailsInfo(String placeId){
        mInfoRestaurantRepository.initRestaurantsDetailsInfo(placeId);
    }

    public LiveData<Place> getRestaurantDetailsInfoLiveData(){
        return mInfoRestaurantRepository.getRestaurantDetailsInfoLiveData();
    }

    public void initPlacesClientInfo(Context context){
        mInfoRestaurantRepository.initPlacesDetailsClientInfo(context);
    }

    public void initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseUser user, String placeId){
        mInfoRestaurantRepository.initAllWorkmatesInThisRestaurantMutableLiveData(user, placeId);
    }

    public LiveData<List<User>> getAllWorkmatesInThisRestaurantLiveData(){
        return mInfoRestaurantRepository.getAllWorkmatesInThisRestaurantLiveData();
    }

    public  LiveData<Integer>  getCountWorkmatesForRestaurant(String placeId){
        return mInfoRestaurantRepository.countWorkmatesForRestaurant(placeId);
    }

//    public LiveData<Integer> getCountWorkmatesInRestaurantLiveData(){
//        return mInfoRestaurantRepository.getCountWorkmatesInRestaurantLiveData();
//    }
}
