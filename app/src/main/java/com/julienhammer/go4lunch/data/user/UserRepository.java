package com.julienhammer.go4lunch.data.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserRepository {
    private static volatile UserRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String USER_ID_FIELD = "userId";
    private static final String USER_PLACE_ID_FIELD = "userPlaceId";
    private static final String TAG = "Value is egal to ";
    public static final String USER_RESTAURANT_LIKES_ARRAY = "userRestaurantLikes";
    private final MutableLiveData<String> userSelectedRestaurantMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> userLikeRestaurantMutableLiveData = new MutableLiveData<>();

    public LiveData<Boolean> getRestaurantIfLiked(){
        return userLikeRestaurantMutableLiveData;
    }

    public LiveData<String> getSelectedRestaurantChoiced() {
        return userSelectedRestaurantMutableLiveData;
    }

    public void userRestaurantSelected(String userId){
        Query query = FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_ID_FIELD, userId);
        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    userSelectedRestaurantMutableLiveData.postValue(document.getString("userPlaceId"));
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
    }

    public void setUserRestaurantChoice(String userId, String placeId){
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(userId).update(USER_PLACE_ID_FIELD, placeId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                userSelectedRestaurantMutableLiveData.postValue(placeId);
            }
        });
    }

    public void setUserRestaurantLikes(FirebaseUser user, String placeId){
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_ID_FIELD, user.getUid()).whereArrayContains(USER_RESTAURANT_LIKES_ARRAY, placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                boolean restaurantLiked;
                if (task.isSuccessful()) {
                    QuerySnapshot doc = task.getResult();
                    if (doc != null) {
                        restaurantLiked = doc.isEmpty();
                        if (restaurantLiked) {
                            FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(user.getUid()).update(USER_RESTAURANT_LIKES_ARRAY, FieldValue.arrayUnion(placeId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    userLikeRestaurantMutableLiveData.postValue(false);
                                }
                            });
                        } else {
                            FirebaseFirestore.getInstance().collection(COLLECTION_NAME).document(user.getUid()).update(USER_RESTAURANT_LIKES_ARRAY, FieldValue.arrayRemove(placeId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    userLikeRestaurantMutableLiveData.postValue(true);
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if (instance == null){
                instance = new UserRepository();
            }
            return instance;
        }
    }
    public void thisRestaurantIsLiked(FirebaseUser user, String placeId){
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_ID_FIELD, user.getUid()).whereArrayContains(USER_RESTAURANT_LIKES_ARRAY, placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
        {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                boolean restaurantLiked;
                if (task.isSuccessful()){
                    QuerySnapshot doc = task.getResult();
                    if (doc != null) {
                        restaurantLiked = doc.isEmpty();
                    } else {
                        restaurantLiked = true;
                    }
                    userLikeRestaurantMutableLiveData.postValue(restaurantLiked);
                }
            }
        });
    }

}
