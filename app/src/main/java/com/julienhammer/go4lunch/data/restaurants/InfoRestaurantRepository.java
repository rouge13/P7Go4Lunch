package com.julienhammer.go4lunch.data.restaurants;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.firestore.*;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantRepository {
    private static final String COLLECTION_NAME = "users";
    private static final String USER_PLACE_ID_FIELD = "userPlaceId";
    private static volatile InfoRestaurantRepository instance;
    private PlacesClient placesClient;
    private static MutableLiveData<Place> restaurantDetailsInfoMutableLiveData = new MutableLiveData<>();
    private static MutableLiveData<Bitmap> restaurantPhotoBitmapMutableLiveData = new MutableLiveData<>();
    private static MutableLiveData<List<User>> mAllWorkmatesInThisRestaurantMutableLiveData = new MutableLiveData<>();

    public InfoRestaurantRepository() {
    }

    public void initPlacesClientInfo(Context context) {
        placesClient = Places.createClient(context);
    }

    public LiveData<Place> getRestaurantDetailsInfoLiveData() {
        return restaurantDetailsInfoMutableLiveData;
    }

    public void initRestaurantsDetailsInfo(String placeId) {
        List<Place.Field> fields = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.PHOTO_METADATAS);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);
        placeTask.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                restaurantDetailsInfoMutableLiveData.postValue(fetchPlaceResponse.getPlace());
                final FetchPhotoRequest photoRequest = FetchPhotoRequest.newInstance(fetchPlaceResponse.getPlace().getPhotoMetadatas().get(0));
                Task<FetchPhotoResponse> photoTask = placesClient.fetchPhoto(photoRequest);
                photoTask.addOnSuccessListener(fetchPhotoResponse -> restaurantPhotoBitmapMutableLiveData.postValue(fetchPhotoResponse.getBitmap()));
            }
        });
    }
    public void initAllWorkmatesInThisRestaurantMutableLiveData(String restaurantId) {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID_FIELD, restaurantId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<User> workmates = new ArrayList<>();
                if (task.isSuccessful()) {
                    List<User> doc = task.getResult().toObjects(User.class);
                    workmates.addAll(doc);
                    mAllWorkmatesInThisRestaurantMutableLiveData.postValue(workmates);
                }
            }
        });
    }

    public LiveData<List<User>> getAllWorkmatesInThisRestaurantLiveData() {
        return mAllWorkmatesInThisRestaurantMutableLiveData;
    }

    public LiveData<Integer> casesOfStars(Double rating) {
        MutableLiveData<Integer> valuesOfStars = new MutableLiveData<>();
        valuesOfStars.postValue(checkStarsFromRating(rating));
        return valuesOfStars;
    }

    public Integer checkStarsFromRating(Double rating) {
        int countOfStars = (int) Math.rint(rating * 3 / 5);
        return countOfStars;
    }

    public LiveData<Integer> distanceFromLocation(LatLng location, LatLng restaurantLocation) {
        MutableLiveData<Integer> distance = new MutableLiveData<>();
        distance.postValue(countDistance(location, restaurantLocation));
        return distance;
    }

    private Integer countDistance(LatLng location, LatLng restaurantLocation) {
        double distance = SphericalUtil.computeDistanceBetween(location, restaurantLocation);
        return (int) Math.round(distance);
    }

    public LiveData<Integer> countWorkmatesForRestaurant(String placeId) {
        MutableLiveData<Integer> countWorkmatesLiveData = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID_FIELD, placeId).addSnapshotListener((value, error) -> {
            countWorkmatesLiveData.setValue(value != null ? value.size() : 0);
        });
        return countWorkmatesLiveData;
    }

    public LiveData<Bitmap> getRestaurantPhotoBitmap() {
        return restaurantPhotoBitmapMutableLiveData;
    }

    public static InfoRestaurantRepository getInstance() {

        InfoRestaurantRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (InfoRestaurantRepository.class) {
            if (instance == null) {
                instance = new InfoRestaurantRepository();
            }
            return instance;
        }
    }
}
