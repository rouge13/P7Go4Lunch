package com.julienhammer.go4lunch.data.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.*;
import com.google.firebase.firestore.Query;
import com.julienhammer.go4lunch.models.User;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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

    private final MutableLiveData<List<String>> allUserRestaurantLikesMutableLiveData = new MutableLiveData<>();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection(COLLECTION_NAME);

    public LiveData<Boolean> getRestaurantIfLiked(){
        return userLikeRestaurantMutableLiveData;
    }

    public LiveData<List<String>> getAllTheRestaurantLikes(){
        return allUserRestaurantLikesMutableLiveData;
    }
//    public LiveData<Boolean> getIfRestaurantIsSet(){
//        return restaurantIsSet;
//    }
//
//    public LiveData<Boolean> getIfRestaurantIsUnSet(){
//        return restaurantIsUnSet;
//    }

    public LiveData<String> getSelectedRestaurantChoiced() {
        return userSelectedRestaurantMutableLiveData;
    }


//    public void userRestaurantIsChoiced(String userId, String placeId){
//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        DocumentReference userRef = rootRef.collection(COLLECTION_NAME).document(userId);
//        Query query = userRef.getParent().whereEqualTo(USER_PLACE_ID_FIELD, placeId);
//
//        query.get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                for (QueryDocumentSnapshot document : task.getResult()) {
////                        Log.d(TAG, document.getId() + " => " + document.getData());
////                    document.getData().get(USER_PLACE_ID_FIELD).equals(placeId);
//                    userHasSelectedThisRestaurantMutableLiveData.postValue(document.exists());
////                        userRestaurantChoiceAdded.set(Objects.equals(document.getData(), placeId));
////                        userRef.document().update(USER_PLACE_ID, "");
//                        }
//            } else {
//                Log.d(TAG, "Error getting documents: ", task.getException());
//
//            }
//        });
//
//    }

    public void userRestaurantSelected(String userId){
//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        CollectionReference userRef = rootRef.collection(COLLECTION_NAME);
        Query query = userRef.whereEqualTo(USER_ID_FIELD, userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
//                    document.getData().get(USER_PLACE_ID_FIELD).equals(placeId);
                    userSelectedRestaurantMutableLiveData.postValue(document.getString("userPlaceId"));
//                        userRestaurantChoiceAdded.set(Objects.equals(document.getData(), placeId));
//                        userRef.document().update(USER_PLACE_ID, "");
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());

            }
        });

    }

    public void allUserRestaurantLikes(FirebaseUser user){
        List<String> mUserRestaurantLikes = new ArrayList<>();
        userRef.whereEqualTo(USER_ID_FIELD, user.getUid()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    User userInformation = documentSnapshot.toObject(User.class);
                    for (String userRestaurantLikes : userInformation.getUserRestaurantLikes()){

                        mUserRestaurantLikes.add(userRestaurantLikes);
                    }
                }
            }
        });
        allUserRestaurantLikesMutableLiveData.postValue(mUserRestaurantLikes);
    }

    public void setUserRestaurantChoice(String userId, String placeId){
//        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
//        CollectionReference userRef = rootRef.collection(COLLECTION_NAME);
        userRef.document(userId).update(USER_PLACE_ID_FIELD, placeId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                userSelectedRestaurantMutableLiveData.postValue(placeId);
            }
        });

    }

    public void setUserRestaurantLikes(FirebaseUser user, String placeId){

        userRef.whereEqualTo(USER_ID_FIELD, user.getUid()).whereArrayContains(USER_RESTAURANT_LIKES_ARRAY, placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                boolean restaurantLiked;
                if (task.isSuccessful()) {

                    QuerySnapshot doc = task.getResult();
                    if (doc != null) {
                        restaurantLiked = doc.isEmpty();
                        if (restaurantLiked) {
                            DocumentReference docIdRef = userRef.document(user.getUid());
                            docIdRef.update(USER_RESTAURANT_LIKES_ARRAY, FieldValue.arrayUnion(placeId)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {

                                    userLikeRestaurantMutableLiveData.postValue(false);

                                }
                            });

                        } else {
                            DocumentReference docIdRef = userRef.document(user.getUid());
                            docIdRef.update(USER_RESTAURANT_LIKES_ARRAY, FieldValue.arrayRemove(placeId)).addOnCompleteListener(new OnCompleteListener<Void>() {
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
        userRef.whereEqualTo(USER_ID_FIELD, user.getUid()).whereArrayContains(USER_RESTAURANT_LIKES_ARRAY, placeId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>()
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
