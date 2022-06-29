package com.julienhammer.go4lunch.data.user;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.models.User;

import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserRepository {

    private static volatile UserRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String USER_ID_FIELD = "userID";
    private static final String USER_NAME_FIELD = "userName";
    private static final String USER_EMAIL_FIELD = "userEmail";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String USER_PHOTO_URL = "userPhotoUrl";
    private String uid;

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            uid = user.getUid();
            String userName = user.getDisplayName();
            String userEmail = user.getEmail();
            String userPlaceId = "Not set";
            String userPhotoUrl = Objects.requireNonNull(user.getPhotoUrl()).toString();



            User userToCreate = new User(uid, userName, userEmail, userPlaceId, userPhotoUrl);

            Task<DocumentSnapshot> userData = getUserData();
            // If the user already exist in Firestore, we get his data (isMentor)
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
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return this.getUsersCollection().document(user.getUid()).get();
        }else{
            return null;
        }
    }

    // Update User Username
    public Task<Void> updateUserName(String wkmName) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return this.getUsersCollection().document(user.getUid()).update(USER_NAME_FIELD, wkmName);
        }else{
            return null;
        }
    }

    // Update User isMentor
    public void updatePlaceId(String wkmPlaceId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            this.getUsersCollection().document(user.getUid()).update(USER_PLACE_ID, wkmPlaceId);
        }
    }

    // Delete the User from Firestore
    public void deleteWorkmateFromFirestore() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            this.getUsersCollection().document(user.getUid()).delete();
        }
    }



    private UserRepository(){}

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
