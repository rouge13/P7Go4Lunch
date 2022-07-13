package com.julienhammer.go4lunch.data.main;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainRepository {

    private static volatile MainRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String COLLECTION_WKM_NAME = "workmates";
    private static final String USER_ID_FIELD = "userID";
    private static final String USER_NAME_FIELD = "userName";
    private static final String USER_EMAIL_FIELD = "userEmail";
    private static final String USER_PLACE_ID = "userPlaceId";
    private static final String USER_PHOTO_URL = "userPhotoUrl";
    private String uid;
    MutableLiveData<User> mMutableLiveData;
    FirebaseFirestore mFirestore;
    FirebaseUser currentUser;

    public MainRepository() {
        // Define User
        mMutableLiveData = new MutableLiveData<>();
        // Define firestore
        mFirestore = FirebaseFirestore.getInstance();
    }

//    public MutableLiveData<List<User>> getUserMutableLiveData() {
//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        mFirestore.collection(COLLECTION_NAME).addSnapshotListener((value, error) -> {
//            List<User> users = new ArrayList<>();
//            if (value != null){
//                for (QueryDocumentSnapshot doc : value){
//                    if (doc != null){
////                    assert currentUser != null;
////                        if (doc.get(USER_ID_FIELD) == currentUser.getUid()){
////                            users.add(doc.toObject(User.class));
////                        }
//                        if (currentUser.getUid().equals(doc.toObject(User.class).getUserId())){
//                            users.add(doc.toObject(User.class));
//                        }
//                    }
//                }
//            }
//
//            mMutableLiveData.postValue(users);
//        });
//        return mMutableLiveData;
//    }

    public MutableLiveData<User> getUserMutableLiveData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore.collection(COLLECTION_NAME).addSnapshotListener((value, error) -> {
//            List<User> users = new ArrayList<>();
            if (value != null){
                for (QueryDocumentSnapshot doc : value){
                    if (doc != null){
//                    assert currentUser != null;
//                        if (doc.get(USER_ID_FIELD) == currentUser.getUid()){
//                            users.add(doc.toObject(User.class));
//                        }
                        assert currentUser != null;
                        if (doc.toObject(User.class).getUserEmail().equals(currentUser.getEmail())){
                                mMutableLiveData.postValue(doc.toObject(User.class));
                            }

                    }
                }
            }

//            mMutableLiveData.postValue(users);
        });
        return mMutableLiveData;
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // Get the Collection Reference
    private CollectionReference getWkmCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_WKM_NAME);
    }

    // Create User in Firestore
    public void createUser() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            uid = currentUser.getUid();
            String userName = currentUser.getDisplayName();
            String userEmail = currentUser.getEmail();
            String userPlaceId = "Not set";
            String userPhotoUrl;
            if (currentUser.getPhotoUrl() != null){
                userPhotoUrl = currentUser.getPhotoUrl().toString();
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
    public void updatePlaceIdForUserAndWorkmate(String userPlaceId) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            this.getUsersCollection().document(currentUser.getUid()).update(USER_PLACE_ID, userPlaceId);
            this.getWkmCollection().document(currentUser.getUid()).update(USER_PLACE_ID, userPlaceId);
        }
    }

//    // Delete the User from Firestore
//    public void deleteWorkmateFromFirestore() {
//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//        if(user != null){
//            this.getUsersCollection().document(user.getUid()).delete();
//        }
//    }



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
