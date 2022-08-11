package com.julienhammer.go4lunch.data.workmate;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.ui.MainApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateRepository {

    Context context = MainApplication.getApplication();

    private static volatile WorkmateRepository instance;
    private static final String COLLECTION_NAME = "users";
    private static final String WKM_PLACE_ID = "userPlaceId";
    private String uid;
    FirebaseFirestore mFirestore;
    MutableLiveData<List<User>> mMutableLiveData;

    // Get the Collection Reference
    private CollectionReference getWkmCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    public WorkmateRepository() {
        // Define Workmates
        mMutableLiveData = new MutableLiveData<>();
        // Define firestore
        mFirestore = FirebaseFirestore.getInstance();
    }

    // Get Workmate Data from Firestore
    public Task<DocumentSnapshot> getWorkmateData(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            return this.getWkmCollection().document(user.getUid()).get();
        }else{
            return null;
        }
    }

    public MutableLiveData<List<User>> getWorkmateMutableLiveData() {
        mFirestore.collection(COLLECTION_NAME).addSnapshotListener((value, error) -> {
            List<User> workmates = new ArrayList<>();
            if (value != null){
                for (QueryDocumentSnapshot doc : value){
                    if (doc != null){
                        workmates.add(doc.toObject(User.class));
                    }
                }
            }
            mMutableLiveData.setValue(workmates);
        });
        return mMutableLiveData;
    }

    public static WorkmateRepository getInstance() {
        WorkmateRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (WorkmateRepository.class){
            if (instance == null){
                instance = new WorkmateRepository();
            }
            return instance;
        }
    }
}
