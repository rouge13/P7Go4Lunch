package com.julienhammer.go4lunch.utils;

import android.location.Location;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;

import java.io.IOException;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class NearbySearch {

    public PlacesSearchResponse run(String apiKey, Location userLocation){
        PlacesSearchResponse request = new PlacesSearchResponse();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(2000)
                    .rankby(RankBy.PROMINENCE)
                    .language("en")
                    .type(PlaceType.RESTAURANT)
                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }

}
