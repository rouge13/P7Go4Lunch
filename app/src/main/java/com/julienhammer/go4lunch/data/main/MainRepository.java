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
    private static final String USER_PLACE_ID = "userPlaceId";
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

    public MutableLiveData<User> getUserMutableLiveData() {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        mFirestore.collection(COLLECTION_NAME).addSnapshotListener((value, error) -> {
            if (value != null){
                for (QueryDocumentSnapshot doc : value){
                    if (doc != null){
                        if (doc.toObject(User.class).getUserEmail().equals(currentUser.getEmail())){
                                mMutableLiveData.postValue(doc.toObject(User.class));
                        }
                    }
                }
            }
        });
        return mMutableLiveData;
    }

    // Get the Collection Reference
    private CollectionReference getUsersCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
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

    // Update User place id
    public void updatePlaceIdForUserAndWorkmate(String userPlaceId) {
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            this.getUsersCollection().document(currentUser.getUid()).update(USER_PLACE_ID, userPlaceId);
        }
    }

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
