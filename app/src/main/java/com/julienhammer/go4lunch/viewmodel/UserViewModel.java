package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.data.user.UserRepository;
import com.julienhammer.go4lunch.models.User;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserViewModel extends ViewModel {
    public UserRepository mUserRepository;

    public LiveData<User> getUserData() {
        return mUserRepository.getUserData();
    }
    public void userRestaurantSelected(String userUID) {
        mUserRepository.userRestaurantSelected(userUID);
    }
    public void setUserRestaurantChoice(String userUID, String placeId){
        mUserRepository.setUserRestaurantChoice(userUID, placeId);
    }
    public UserViewModel() {
        mUserRepository = UserRepository.getInstance();
    }
    public LiveData<String> getSelectedRestaurantIsChoosed() {
        return mUserRepository.getSelectedRestaurantIsChoosed();
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
