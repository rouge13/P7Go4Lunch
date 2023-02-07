package com.julienhammer.go4lunch.data;

import com.google.maps.model.PlacesSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public interface GooglePlaceApi {

    @GET("nearbysearch/json")
    Call<PlacesSearchResponse> getNearbyPlaces(@Query("location") String location,
                                               @Query("radius") int radius,
                                               @Query("type") String type,
                                               @Query("key") String apiKey);
}
