package com.julienhammer.go4lunch.data.restaurants;

import android.location.Location;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.NearbySearch;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantRepository {
    private static final String COLLECTION_NAME = "restaurants";
    private static final String RES_ID_FIELD = "restaurantId";
    private static final String RES_PLACE_ID_FIELD = "restaurantPlaceId";
    private static final String TAG = "Value is egal to ";
    private static final String FIELD_RESTAURANT_LIKES = "restaurantLikes";
    private static volatile InfoRestaurantRepository instance;
    private static MutableLiveData<RestaurantDetails> mInfoRestaurantMutableLiveData;

    public InfoRestaurantRepository(){
        InfoRestaurantRepository.mInfoRestaurantMutableLiveData = new MutableLiveData<>();

    }
    public LiveData<RestaurantDetails> getInfoRestaurantLiveData() {
        return mInfoRestaurantMutableLiveData;
    }

    public void setInfoRestaurant(RestaurantDetails restaurantDetails) {
        if (restaurantDetails != null){
            mInfoRestaurantMutableLiveData.postValue(restaurantDetails);
        }
    }

//    public void createRestaurantLiked(String restaurantPlaceId, FirebaseUser user){
//        if (restaurantPlaceId != null){
//
//            Map<String, Object> restaurantLiked = new HashMap<>();
//            restaurantLiked.put(RES_PLACE_ID_FIELD, restaurantPlaceId);
//            restaurantLiked.put(RES_ID_FIELD, Arrays.asList(user));
//
//
//            FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(restaurantPlaceId)
//                    .set(restaurantLiked)
//                    .addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Log.d(TAG, "DocumentSnapshot successfully written!");
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.w(TAG, "Error writing document", e);
//                        }
//                    });
//
//
////            Task<DocumentSnapshot> restaurantData = getRestaurantData();
////            // If the user already exist in Firestore, we get his data (USER_PLACE_ID)
////            restaurantData.addOnSuccessListener(documentSnapshot -> {
////                if (documentSnapshot.contains(USER_ID_FIELD)){
////                    userToCreate.setUserPlaceId((String) documentSnapshot.get(USER_PLACE_ID));
////                }
////                this.getUsersCollection().document(uid).set(userToCreate);
////            });
//
//
//        }
//    }


    // Get the Collection Reference
    private CollectionReference getInfoRestaurantCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }
    public Task<DocumentSnapshot> getInfoRestaurantData(String restaurantPlaceId){

        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            return this.getInfoRestaurantCollection().document(restaurantPlaceId).get();
        }else{
            return null;
        }
    }

    public static InfoRestaurantRepository getInstance(){

        InfoRestaurantRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (InfoRestaurantRepository.class){
            if (instance == null){
                instance = new InfoRestaurantRepository();
            }
            return instance;
        }
    }
}
