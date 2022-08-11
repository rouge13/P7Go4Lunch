package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.main.MainRepository;
import com.julienhammer.go4lunch.models.User;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserViewModel extends ViewModel {
    MutableLiveData<User> mUserData;

    public MutableLiveData<User> getmUserData() {
        if (mUserData == null){
            mUserData = MainRepository.getInstance().getUserMutableLiveData();
        }
        return mUserData;
    }

}
