package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.main.MainRepository;
import com.julienhammer.go4lunch.data.user.UserRepository;
import com.julienhammer.go4lunch.models.User;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserViewModel extends ViewModel {
    private static UserRepository mUserRepository;
    FirebaseFirestore mFirestore;
    MutableLiveData<User> mUserData;
    public MutableLiveData<User> getmUserData() {
        if (mUserData == null){
            mUserData = MainRepository.getInstance().getUserMutableLiveData();
        }
        return mUserData;
    }
    public void userRestaurantSelected(String userUID) {
        mUserRepository.userRestaurantSelected(userUID);
    }
    public void setUserRestaurantChoice(String userUID, String placeId){
        mUserRepository.setUserRestaurantChoice(userUID, placeId);
    }
    public UserViewModel() {
        mFirestore = FirebaseFirestore.getInstance();
        mUserRepository = UserRepository.getInstance();
    }
    public LiveData<String> getSelectedRestaurantIsChoiced() {
        return mUserRepository.getSelectedRestaurantChoiced();
    }
    public void setUserRestaurantLikes(FirebaseUser user, String placeId){
        mUserRepository.setUserRestaurantLikes(user, placeId);
    }
    public void thisRestaurantIsLiked(FirebaseUser user, String placeId){
        mUserRepository.thisRestaurantIsLiked(user,placeId);
    }
    public LiveData<Boolean> getIfRestaurantIsLiked(){
        return mUserRepository.getRestaurantIfLiked();
    }

}
