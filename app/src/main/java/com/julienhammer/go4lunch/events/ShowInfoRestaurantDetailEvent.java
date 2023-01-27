package com.julienhammer.go4lunch.events;

import com.julienhammer.go4lunch.models.RestaurantDetails;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ShowInfoRestaurantDetailEvent {
    private RestaurantDetails mRestaurant;
    public ShowInfoRestaurantDetailEvent(RestaurantDetails restaurantDetails) {
    }

    public RestaurantDetails getRestaurant() {
        return mRestaurant;
    }

    public void setRestaurant(RestaurantDetails mRestaurant) {
        this.mRestaurant = mRestaurant;
    }

}
