package com.julienhammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.main.MainRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
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
