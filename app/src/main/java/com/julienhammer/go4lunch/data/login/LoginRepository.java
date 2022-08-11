package com.julienhammer.go4lunch.data.login;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.julienhammer.go4lunch.models.User;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginRepository {
    private static volatile LoginRepository instance;
    private static final String COLLECTION_NAME = "users";
//    private static final String COLLECTION_WKM_NAME = "workmates";
    private static final String USER_ID_FIELD = "userID";
    private static final String USER_NAME_FIELD = "userName";
    private static final String USER_EMAIL_FIELD = "userEmail";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String USER_PHOTO_URL = "userPhotoUrl";
    private static final String TAG = "Value is egal to ";
    private String uid;
    Boolean userCaseAdded;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;


    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser(FirebaseUser user) {
//        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userPlaceId = "";
            String userPhotoUrl;
            if (user.getPhotoUrl() != null){
                userPhotoUrl = user.getPhotoUrl().toString();
            } else {
                userPhotoUrl = "https://abs.twimg.com/sticky/default_profile_images/default_profile_normal.png";
            }
            User userToCreate = new User(uid, userName, userEmail, userPlaceId, userPhotoUrl);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (USER_PLACE_ID)
            userData.addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.contains(USER_PLACE_ID)){
                    userToCreate.setUserPlaceId((String) documentSnapshot.get(USER_PLACE_ID));
                }
                this.getUsersCollection().document(uid).set(userToCreate);
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            return this.getUsersCollection().document(currentUser.getUid()).get();
        }else{
            return null;
        }
    }

    public Boolean getUserCase(FirebaseUser userAdded) {
        userCaseAdded = false;
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        DocumentReference docIdRef = rootRef.collection(COLLECTION_NAME).document(userAdded.getUid());

        docIdRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) { DocumentSnapshot document = task.getResult();
                if (document.exists()) {
//                    Log.d(TAG, "Document exists!");
                    userCaseAdded = true;
                } else {
                    userCaseAdded = false;
//                    Log.d(TAG, "Document does not exist!");
                }
            } else { Log.d(TAG, "Failed with: ", task.getException());
            }
        });
        return userCaseAdded;
    }

}
