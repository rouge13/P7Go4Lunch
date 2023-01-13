package com.julienhammer.go4lunch.data.restaurants;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantRepository {
    private static final String COLLECTION_NAME = "users";
    private static final String USER_PLACE_ID_FIELD = "userPlaceId";
    private static final String TAG = "Value is egal to ";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection(COLLECTION_NAME);
    private static MutableLiveData<Integer> mCountWorkmatesInRestaurant;
    private static volatile InfoRestaurantRepository instance;
    //    FirebaseFirestore mFirestore;
    private static MutableLiveData<RestaurantDetails> mInfoRestaurantMutableLiveData;
    private PlacesClient placesClient;

    private static MutableLiveData<Place> restaurantDetailsInfoMutableLiveData = new MutableLiveData<>();
    private static MutableLiveData<Bitmap> restaurantPhotoBitmapMutableLiveData = new MutableLiveData<>();
    private static MutableLiveData<List<User>> mAllWorkmatesInThisRestaurantMutableLiveData = new MutableLiveData<>();

    public InfoRestaurantRepository() {
        InfoRestaurantRepository.mInfoRestaurantMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<RestaurantDetails> getInfoRestaurantLiveData() {
        return mInfoRestaurantMutableLiveData;
    }

    public void setInfoRestaurant(RestaurantDetails restaurantDetails) {
        if (restaurantDetails != null) {
            mInfoRestaurantMutableLiveData.postValue(restaurantDetails);
        }
    }

    public void initPlacesDetailsClientInfo(Context context) {
        placesClient = Places.createClient(context);
    }

    public LiveData<Place> getRestaurantDetailsInfoLiveData() {
        return restaurantDetailsInfoMutableLiveData;
    }

    public void initRestaurantsDetailsInfo(String placeId) {
        List<Place.Field> fields = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI, Place.Field.PHOTO_METADATAS);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);


        placeTask.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
//                restaurantDetails.add(fetchPlaceResponse);
                restaurantDetailsInfoMutableLiveData.postValue(fetchPlaceResponse.getPlace());

                final FetchPhotoRequest photoRequest = FetchPhotoRequest.newInstance(fetchPlaceResponse.getPlace().getPhotoMetadatas().get(0));
                Task<FetchPhotoResponse> photoTask = placesClient.fetchPhoto(photoRequest);
                photoTask.addOnSuccessListener(fetchPhotoResponse -> restaurantPhotoBitmapMutableLiveData.postValue(fetchPhotoResponse.getBitmap()));
            }
        });

    }

    public void initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseUser user, String placeId) {
        userRef.whereEqualTo(USER_PLACE_ID_FIELD, placeId).addSnapshotListener((value, error) -> {
            List<User> workmates = new ArrayList<>();
            if (value != null) {
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        workmates.add(doc.toObject(User.class));
                    }
                }
            }

            mAllWorkmatesInThisRestaurantMutableLiveData.postValue(workmates);
        });
    }

    public LiveData<List<User>> getAllWorkmatesInThisRestaurantLiveData() {
        return mAllWorkmatesInThisRestaurantMutableLiveData;
    }


    // Get the Collection Reference
    private CollectionReference getInfoRestaurantCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

//    public Task<DocumentSnapshot> getInfoRestaurantData(String restaurantPlaceId) {
//
//        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            return this.getInfoRestaurantCollection().document(restaurantPlaceId).get();
//        } else {
//            return null;
//        }
//    }

    public LiveData<Integer> casesOfStars(Double rating) {
        MutableLiveData<Integer> valuesOfStars = new MutableLiveData<>();
        valuesOfStars.postValue(checkStarsFromRating(rating));
        return valuesOfStars;
    }

    public Integer checkStarsFromRating(Double rating) {
        int percentOfRating = (int) Math.rint(rating * 3 / 5);
        return percentOfRating;
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
        db.collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID_FIELD, placeId).addSnapshotListener((value, error) -> {
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
