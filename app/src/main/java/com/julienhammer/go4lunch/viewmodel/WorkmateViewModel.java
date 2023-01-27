package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;

import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateViewModel extends ViewModel {

    private static volatile WorkmateViewModel instance;
    private final WorkmateRepository workmateRepository;
    MutableLiveData<List<User>> mAllWorkmatesMutableLiveData;
    FirebaseFirestore mFirestore;

    public LiveData<List<User>> getWorkmateMutableLiveData() {
        return mAllWorkmatesMutableLiveData;
    }

    public WorkmateViewModel(){
        workmateRepository = WorkmateRepository.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mAllWorkmatesMutableLiveData = workmateRepository.getAllWorkmatesMutableLiveData();
    }

}
