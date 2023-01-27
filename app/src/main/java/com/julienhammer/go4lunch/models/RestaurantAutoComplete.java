package com.julienhammer.go4lunch.models;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantAutoComplete {
        public String placeId;
        public CharSequence address, name;

        public RestaurantAutoComplete(String placeId, CharSequence name, CharSequence address) {
            this.placeId = placeId;
            this.name = name;
            this.address = address;
        }

}

