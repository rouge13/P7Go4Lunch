package com.julienhammer.go4lunch.data.workmate;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateRepository {
    private static volatile WorkmateRepository instance;
    private static final String COLLECTION_NAME = "users";
    MutableLiveData<List<User>> mAllWorkmatesMutableLiveData;

    public WorkmateRepository() {
        mAllWorkmatesMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<User>> getWorkmates() {
        FirebaseFirestore.getInstance().collection(COLLECTION_NAME).addSnapshotListener((value, error) -> {
            List<User> workmates = new ArrayList<>();
            if (value != null){
                for (QueryDocumentSnapshot doc : value){
                    if (doc != null){
                        workmates.add(doc.toObject(User.class));
                    }
                }
            }
            mAllWorkmatesMutableLiveData.postValue(workmates);
        });
        return mAllWorkmatesMutableLiveData;
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
