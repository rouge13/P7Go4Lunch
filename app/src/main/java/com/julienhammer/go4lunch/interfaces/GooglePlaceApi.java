package com.julienhammer.go4lunch.interfaces;

import com.julienhammer.go4lunch.models.PlacesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public interface GooglePlaceApi {

    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Call<PlacesResponse.Root> getNearbyPlaces(
            @Query("location") String location,
            @Query("radius") int radius,
            @Query("type") String type,
            @Query("key") String key
    );
}
