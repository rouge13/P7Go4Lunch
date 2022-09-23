package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.utils.NearbySearch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsRepository {

    private static final String COLLECTION_NAME = "restaurants";
    private static final String RES_ID_FIELD = "restaurantId";
    private static final String RES_PLACE_ID_FIELD = "restaurantPlaceId";
    private static final String TAG = "Value is egal to ";

    private static volatile RestaurantsRepository instance;
    private static MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData;
    private static FusedLocationProviderClient mFusedLocationProviderClient;

    public RestaurantsRepository(MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData,
                                 @NonNull FusedLocationProviderClient mFusedLocationProviderClient){
        RestaurantsRepository.mFusedLocationProviderClient = mFusedLocationProviderClient;
        RestaurantsRepository.mRestaurantMutableLiveData = new MutableLiveData<>();

    }


    // Get the Collection Reference
    private CollectionReference getRestaurantsCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }



    public LiveData<PlacesSearchResult[]> getRestaurantsLiveData() {
        return mRestaurantMutableLiveData;
    }

    public void getAllRestaurants(String apiKey, Location userLocation) {
        PlacesSearchResult[] results;
        if (userLocation != null && apiKey != null){
            results = new NearbySearch().run(apiKey,userLocation).results;
            mRestaurantMutableLiveData.postValue(results);
        }
    }


    public void getAllRestaurantsDetails(String placeId){



        List<Place.Field> fields = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        // Fetch Place by ID
        FetchPlaceRequest detailsRequest = FetchPlaceRequest.builder(placeId, fields).build();
//        // Find User's Current Place
//        FindCurrentPlaceRequest currentPlaceRequest = FindCurrentPlaceRequest.builder(fields).build();


//
//
//// Specify the fields to return.
//        final List<Place.Field> placeFields = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);
//
//// Construct a request object, passing the place ID and fields array.
//        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, placeFields);
//        PlacesClient placesClient = new PlacesClient() {
//            @NonNull
//            @Override
//            public Task<FetchPhotoResponse> fetchPhoto(@NonNull FetchPhotoRequest fetchPhotoRequest) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<FetchPlaceResponse> fetchPlace(@NonNull FetchPlaceRequest fetchPlaceRequest) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<FindAutocompletePredictionsResponse> findAutocompletePredictions(@NonNull FindAutocompletePredictionsRequest findAutocompletePredictionsRequest) {
//                return null;
//            }
//
//            @NonNull
//            @Override
//            public Task<FindCurrentPlaceResponse> findCurrentPlace(@NonNull FindCurrentPlaceRequest findCurrentPlaceRequest) {
//                return null;
//            }
//        };
//        placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
//            Place place = response.getPlace();
//            Log.i(TAG, "Place found: " + place.getName());
//        }).addOnFailureListener((exception) -> {
//            if (exception instanceof ApiException) {
//                final ApiException apiException = (ApiException) exception;
//                Log.e(TAG, "Place not found: " + exception.getMessage());
//                final int statusCode = apiException.getStatusCode();
//                // TODO: Handle error with given status code.
//            }
//        });
    }

    public static RestaurantsRepository getInstance(){

        RestaurantsRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (RestaurantsRepository.class){
            if (instance == null){
                instance = new RestaurantsRepository(mRestaurantMutableLiveData,mFusedLocationProviderClient);
            }
            return instance;
        }
    }


}
