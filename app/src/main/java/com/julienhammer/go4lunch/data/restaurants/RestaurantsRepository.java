package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.utils.NearbySearch;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsRepository {
    private static final String COLLECTION_NAME = "users";
    public static final String USER_PLACE_FIELD = "userPlaceId";
    FirebaseFirestore mFirestore;
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
