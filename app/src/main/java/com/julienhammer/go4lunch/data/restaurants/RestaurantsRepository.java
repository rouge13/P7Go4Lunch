package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.GooglePlaceApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantsRepository {
    private static final String COLLECTION_NAME = "users";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String TAG = "Value is egal to ";
    private static volatile RestaurantsRepository instance;
    private static MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData;
    private static FusedLocationProviderClient mFusedLocationProviderClient;
    MutableLiveData<List<String>> allChoosedRestaurants = new MutableLiveData<>();
    MutableLiveData<Boolean> alreadySomeone = new MutableLiveData<>();

    private MutableLiveData<PlacesSearchResponse> mNearbyPlaces = new MutableLiveData<>();

//    public void initIsSomeoneEatingHere(String resId){
//        alreadySomeone.postValue(resId);
//    }

    public RestaurantsRepository(MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData,
                                 @NonNull FusedLocationProviderClient mFusedLocationProviderClient){
        RestaurantsRepository.mFusedLocationProviderClient = mFusedLocationProviderClient;
        RestaurantsRepository.mRestaurantMutableLiveData = new MutableLiveData<>();
        allChoosedRestaurants = new MutableLiveData<>();
    }

    public LiveData<PlacesSearchResult[]> getRestaurantsLiveData() {
        return mRestaurantMutableLiveData;
    }

    public LiveData<Boolean> getIfEatingHere() {
        return alreadySomeone;
    }

    public LiveData<PlacesSearchResponse> getNearbyPlaces() {
        return mNearbyPlaces;
    }

    public void initIsSomeoneEatingThere(String resId){
        alreadySomeone.postValue(false);
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID, resId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                        alreadySomeone.postValue(document.exists());
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void initAllRestaurant(String apiKey, Location userLocation) {
//        PlacesSearchResult[] results;
        if (userLocation != null && apiKey != null){
//            results = new NearbySearch().run(apiKey,userLocation).results;
//            mRestaurantMutableLiveData.postValue(results);
//        }
//        if (mNearbyPlaces == null) {
//            mNearbyPlaces = new MutableLiveData<>();
            loadPlaces(apiKey, userLocation);
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

    public LiveData<List<String>> getAllRestaurantChoosed() {
            FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
                List<String> choosedRestaurant = new ArrayList<>();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        if (doc != null){
                            choosedRestaurant.add(doc.getString(USER_PLACE_ID));
                        }
                    }
                }
                allChoosedRestaurants.postValue(choosedRestaurant);
            });
            return allChoosedRestaurants;
    }

    public void initAllRestaurantChoosed() {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
            List<String> choosedRestaurant = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()){
                    if (doc != null){
                        choosedRestaurant.add(doc.getString(USER_PLACE_ID));
                    }
                }
            }
            allChoosedRestaurants.postValue(choosedRestaurant);
        });
    }

    private void loadPlaces(String apiKey, Location userLocation) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GooglePlaceApi googlePlaceApi = retrofit.create(GooglePlaceApi.class);

        String location = userLocation.getLatitude() + "," + userLocation.getLongitude();

        Call<PlacesSearchResponse> call = googlePlaceApi.searchPlaces(
                "restaurant",
                location,
                apiKey,
                2000
        );

        call.enqueue(new Callback<PlacesSearchResponse>() {
            @Override
            public void onResponse(Call<PlacesSearchResponse> call, Response<PlacesSearchResponse> response) {
                if (response.isSuccessful()) {
                    PlacesSearchResponse placesSearchResponse = response.body();
                    mNearbyPlaces.postValue(placesSearchResponse);
                } else {
                    // handle the error
                    mNearbyPlaces.postValue(null);
                }
            }

            @Override
            public void onFailure(retrofit2.Call<PlacesSearchResponse> call, Throwable t) {
                mNearbyPlaces.postValue(null);
            }
        });
    }

}
