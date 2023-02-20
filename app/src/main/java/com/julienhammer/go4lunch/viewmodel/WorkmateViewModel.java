package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;

import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateViewModel extends ViewModel {

//    private static volatile WorkmateViewModel instance;
    WorkmateRepository workmateRepository;
//    MutableLiveData<List<User>> mAllWorkmatesMutableLiveData;
//    FirebaseFirestore mFirestore;

    public LiveData<List<User>> getWorkmates() {
        return workmateRepository.getWorkmates();
    }

    public WorkmateViewModel( WorkmateRepository repository){
//        mFirestore = FirebaseFirestore.getInstance();
        workmateRepository = repository;
//        mAllWorkmatesMutableLiveData = workmateRepository.getAllWorkmatesMutableLiveData();
    }

}
