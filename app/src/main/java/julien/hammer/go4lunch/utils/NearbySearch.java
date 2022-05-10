package julien.hammer.go4lunch.utils;

import android.location.Location;

import com.google.maps.GeoApiContext;
import com.google.maps.PlacesApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.PlaceType;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.RankBy;
import com.google.maps.model.LatLng;
import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import julien.hammer.go4lunch.R;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class NearbySearch {

    public PlacesSearchResponse run(String apiKey, Location userLocation){
//        LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
//        LatLng location = new LatLng(48.5735, 7.7523);
        LatLng location = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
        PlacesSearchResponse request = new PlacesSearchResponse();

        try {
            request = PlacesApi.nearbySearchQuery(context, location)
                    .radius(5000)
                    .rankby(RankBy.PROMINENCE)
                    .keyword("cruise")
                    .language("en")
                    .type(PlaceType.RESTAURANT)
                    .await();
//                    .await();
        } catch (ApiException | IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            return request;
        }
    }
}
