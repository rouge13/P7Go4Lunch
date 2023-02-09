package com.julienhammer.go4lunch.data;


import com.google.maps.model.PlacesSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public interface GooglePlaceApi {

    @GET("https://maps.googleapis.com/maps/api/place/textsearch/json")
    Call<PlacesSearchResponse> searchPlaces(@Query("type") String type,
                                            @Query("location") String location,
                                            @Query("key") String key,
                                            @Query("radius") int radius

                                            );

}
