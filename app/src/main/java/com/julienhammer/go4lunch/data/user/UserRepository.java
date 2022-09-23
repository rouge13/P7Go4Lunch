package com.julienhammer.go4lunch.data.user;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
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
    private final MutableLiveData<String> userSelectedRestaurantMutableLiveData = new MutableLiveData<>();

    public LiveData <String> getSelectedRestaurantChoiced() {
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
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference userRef = rootRef.collection(COLLECTION_NAME);
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

    public void setUserRestaurantChoice(String userId, String placeId){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference userRef = rootRef.collection(COLLECTION_NAME);
        userRef.document(userId).update(USER_PLACE_ID_FIELD, placeId).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                
            }
        });

    }

    public void unSetUserRestaurantChoice(String userId){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference userRef = rootRef.collection(COLLECTION_NAME);
        userRef.document(userId).update(USER_PLACE_ID_FIELD, "");
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
}
