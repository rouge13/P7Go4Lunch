package com.julienhammer.go4lunch.data.restaurants;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.julienhammer.go4lunch.models.RestaurantDetails;
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
    private static final String TAG = "Value is egal to ";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection(COLLECTION_NAME);
    private static MutableLiveData<Integer> mCountWorkmatesInRestaurant;
    private static volatile InfoRestaurantRepository instance;
//    FirebaseFirestore mFirestore;
    private static MutableLiveData<RestaurantDetails> mInfoRestaurantMutableLiveData;
    private PlacesClient placesClient;
    private static MutableLiveData<Place> restaurantDetailsInfo = new MutableLiveData<>();
    private static MutableLiveData<List<User>> mAllWorkmatesInThisRestaurantMutableLiveData = new MutableLiveData<>();
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

    public void initPlacesDetailsClientInfo(Context context){
        placesClient = Places.createClient(context);
    }

    public LiveData<Place> getRestaurantDetailsInfoLiveData(){
        return restaurantDetailsInfo;
    }

    public void initRestaurantsDetailsInfo(String placeId){
        List<Place.Field> fields = Arrays.asList(Place.Field.PHONE_NUMBER, Place.Field.WEBSITE_URI);

        // Construct a request object, passing the place ID and fields array.
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(placeId, fields);
        Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);

        placeTask.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
//                restaurantDetails.add(fetchPlaceResponse);
                restaurantDetailsInfo.postValue(fetchPlaceResponse.getPlace());
            }
        });
    }

    public void initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseUser user, String placeId) {
        userRef.whereEqualTo(USER_PLACE_ID_FIELD, placeId).addSnapshotListener((value, error) -> {
            List<User> workmates = new ArrayList<>();
            if (value != null){
                for (QueryDocumentSnapshot doc : value){
                    if (doc != null){
                        workmates.add(doc.toObject(User.class));
                    }
                }
            }
            mAllWorkmatesInThisRestaurantMutableLiveData.setValue(workmates);
        });
    }

    public LiveData<List<User>> getAllWorkmatesInThisRestaurantLiveData(){
        return mAllWorkmatesInThisRestaurantMutableLiveData;
    }


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

    public LiveData<Integer> countWorkmatesForRestaurant(String placeId) {
        MutableLiveData<Integer> countWorkmatesLiveData = new MutableLiveData<>();
        db.collection(COLLECTION_NAME).whereEqualTo(USER_PLACE_ID_FIELD, placeId).addSnapshotListener((value, error) -> {
            int countWorkmates = 0;
            if (value != null){
                countWorkmates = value.size();
            }
            countWorkmatesLiveData.setValue(countWorkmates);
        });
        return countWorkmatesLiveData;
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
