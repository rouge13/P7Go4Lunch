package com.julienhammer.go4lunch.data.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginRepository {
    private static volatile LoginRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String USER_ID_FIELD = "userId";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String TAG = "Value is egal to ";
    private final MutableLiveData<Boolean> userCaseAddedMutableLiveData = new MutableLiveData<>();
    FirebaseUser currentUser;
    public LiveData<Boolean> getIfUserAlreadyAdded() {
        return userCaseAddedMutableLiveData;
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUserOrNot(FirebaseUser user) {
        String uid = user.getUid();
        String userName = user.getDisplayName();
        String userEmail = user.getEmail();
        String userPlaceId = "";
        String userPhotoUrl;
        List<String> userRestaurantLikes = new ArrayList<>();
        if (user.getPhotoUrl() != null) {
            userPhotoUrl = user.getPhotoUrl().toString();
        } else {
            userPhotoUrl = "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
        }
        User userToCreate = new User(uid, userName, userEmail, userPlaceId, userPhotoUrl, userRestaurantLikes);
        Task<DocumentSnapshot> userData = getUserData();
        // If the user already exist in Firestore, we get his data (USER_PLACE_ID)
        userData.addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.contains(USER_PLACE_ID)) {
                userToCreate.setUserPlaceId((String) documentSnapshot.get(USER_PLACE_ID));
            } else {
                this.getUsersCollection().document(uid).set(userToCreate);
            }
        });
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            return this.getUsersCollection().document(currentUser.getUid()).get();
        } else {
            return null;
        }
    }

    public void isUserAddedInFirebase(String userUIDAdded) {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME)
                .whereEqualTo(USER_ID_FIELD, userUIDAdded)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.exists()) {
                                userCaseAddedMutableLiveData.postValue(document.exists());

                            } else {
                                userCaseAddedMutableLiveData.postValue(false);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }).addOnFailureListener(e -> Log.d(TAG, "Error getting documents: ", e));
    }

    @NonNull
    public LiveData<String> getUserSelectedRestaurantChoice(String userId) {
        MutableLiveData<String> userPlaceIdMutableLiveData = new MutableLiveData<>();
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).whereEqualTo(USER_ID_FIELD, userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    userPlaceIdMutableLiveData.postValue(document.getString("userPlaceId"));
                }
            } else {
                Log.d(TAG, "Error getting documents: ", task.getException());
            }
        });
        return userPlaceIdMutableLiveData;
    }

    public static LoginRepository getInstance() {
        LoginRepository result = instance;
        if (result != null) {
            return result;
        }
        synchronized (LoginRepository.class) {
            if (instance == null) {
                instance = new LoginRepository();
            }
            return instance;
        }
    }
}
