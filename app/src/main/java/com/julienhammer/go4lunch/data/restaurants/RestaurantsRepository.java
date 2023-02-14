package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.android.SphericalUtil;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.interfaces.GooglePlaceApi;
import com.julienhammer.go4lunch.models.PlacesResponse;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    MutableLiveData <ArrayList<String>> resultOfSearch = new MutableLiveData<>();

    private MutableLiveData<PlacesResponse.Root> mNearbyPlaces = new MutableLiveData<>();

    public RestaurantsRepository(MutableLiveData<PlacesSearchResult[]> mRestaurantMutableLiveData,
                                 @NonNull FusedLocationProviderClient mFusedLocationProviderClient) {
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

    public LiveData<PlacesResponse.Root> getNearbyPlaces() {
        return mNearbyPlaces;
    }

    public void initAllSearchFilteredRestaurant(CharSequence query){
        ArrayList<String> filteredResults = new ArrayList<>();
        if (mNearbyPlaces.getValue() != null){
            for (PlacesResponse.Result result : mNearbyPlaces.getValue().results){
                if (!query.toString().equals("") && result.name.toLowerCase().contains(query.toString().toLowerCase())){
                    filteredResults.add(result.name);
                } else {
                    filteredResults.remove(result.name);
                }
            }
            resultOfSearch.postValue(filteredResults);
        }
    }

    public LiveData<ArrayList<String>> getAllSearchFilteredRestaurant(){
        return resultOfSearch;
    }

    public void initIsSomeoneEatingThere(String resId) {
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
        if (userLocation != null && apiKey != null) {
            loadPlaces(apiKey, userLocation);
        }
    }

    public static RestaurantsRepository getInstance() {
        RestaurantsRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (RestaurantsRepository.class) {
            if (instance == null) {
                instance = new RestaurantsRepository(mRestaurantMutableLiveData, mFusedLocationProviderClient);
            }
            return instance;
        }
    }

    public LiveData<List<String>> getAllRestaurantChoosed() {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).get().addOnCompleteListener(task -> {
            List<String> choosedRestaurant = new ArrayList<>();
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc != null) {
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
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    if (doc != null) {
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
        Call<PlacesResponse.Root> call = googlePlaceApi.getNearbyPlaces(location, 2000, "restaurant", apiKey);
        call.enqueue(new Callback<PlacesResponse.Root>() {
            @Override
            public void onResponse(Call<PlacesResponse.Root> call, Response<PlacesResponse.Root> response) {
                if (response.isSuccessful()) {
                    LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        List<PlacesResponse.Result> places = response.body().results.stream()
                                .sorted(Comparator.comparingDouble(place -> SphericalUtil.computeDistanceBetween(userLatLng,new LatLng(place.geometry.location.lat, place.geometry.location.lng))))
                                .collect(Collectors.toList());

                        ArrayList<PlacesResponse.Result> placesResults = new ArrayList<>(places);
                        mNearbyPlaces.postValue(new PlacesResponse.Root(response.body().html_attributions, response.body().next_page_token, placesResults, response.body().status));
                    }
                } else {
                    mNearbyPlaces.postValue(null);
                }
            }
            @Override
            public void onFailure(retrofit2.Call<PlacesResponse.Root> call, Throwable t) {
                mNearbyPlaces.postValue(null);
            }
        });
    }
}
