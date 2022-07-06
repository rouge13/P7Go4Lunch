package com.julienhammer.go4lunch.data.user;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.models.User;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainRepository {

    private static volatile MainRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String USER_ID_FIELD = "userID";
    private static final String USER_NAME_FIELD = "userName";
    private static final String USER_EMAIL_FIELD = "userEmail";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String USER_PHOTO_URL = "userPhotoUrl";
    private String uid;

    // Get the Collection Reference
    private CollectionReference getUsersCollection(String collectionName){
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
                this.getUsersCollection(COLLECTION_NAME).document(uid).set(userToCreate);
            });
        }
    }

    // Get User Data from Firestore
    public Task<DocumentSnapshot> getUserData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return this.getUsersCollection(COLLECTION_NAME).document(user.getUid()).get();
        }else{
            return null;
        }
    }

//    // Update User Username
//    public Task<Void> updateUserName(String wkmName) {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null){
//            return this.getUsersCollection().document(user.getUid()).update(USER_NAME_FIELD, wkmName);
//        }else{
//            return null;
//        }
//    }

    // Update User place id
    public void updatePlaceId(String wkmPlaceId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            this.getUsersCollection(COLLECTION_NAME).document(user.getUid()).update(USER_PLACE_ID, wkmPlaceId);
        }
    }

//    // Delete the User from Firestore
//    public void deleteWorkmateFromFirestore() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null){
//            this.getUsersCollection().document(user.getUid()).delete();
//        }
//    }



    private MainRepository(){}

    public static MainRepository getInstance() {
        MainRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (MainRepository.class){
            if (instance == null){
                instance = new MainRepository();
            }
            return instance;
        }
    }


}
