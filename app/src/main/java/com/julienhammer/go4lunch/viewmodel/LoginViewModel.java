package com.julienhammer.go4lunch.viewmodel;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.login.LoginRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginViewModel extends ViewModel {
    private static LoginRepository loginRepository;
    FirebaseFirestore mFirestore;

    public void isUserAddedInFirebase(String userUIDAdded) {
        loginRepository.isUserAddedInFirebase(userUIDAdded);
    }

    public LiveData<Boolean> getIfUserAlreadyAdded(){
        return loginRepository.getIfUserAlreadyAdded();
    }

    public LiveData<String> getUserSelectedRestaurantChoice(String userId){
        return loginRepository.getUserSelectedRestaurantChoice(userId);
    }

    public LoginViewModel() {
        mFirestore = FirebaseFirestore.getInstance();
        loginRepository = LoginRepository.getInstance();
    }

    public void createUserOrNot(FirebaseUser user){
        loginRepository.createUserOrNot(user);
    }

}
