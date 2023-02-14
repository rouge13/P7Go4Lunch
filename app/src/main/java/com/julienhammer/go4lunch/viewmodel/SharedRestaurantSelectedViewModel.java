package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.models.RestaurantDetails;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class SharedRestaurantSelectedViewModel extends ViewModel {
    private MutableLiveData<RestaurantDetails> selectedRestaurant = new MutableLiveData<>();

    public void initSelectedRestaurant(RestaurantDetails restaurant) {
        selectedRestaurant.setValue(restaurant);
    }

    public LiveData<RestaurantDetails> getSelectedRestaurant() {
        return selectedRestaurant;
    }
}
