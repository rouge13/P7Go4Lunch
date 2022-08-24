package com.julienhammer.go4lunch.viewmodel;


import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.data.main.MainRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginViewModel extends ViewModel {
    private static volatile LoginViewModel instance;
    private static LoginRepository loginRepository;
    boolean mMutableLiveDataCase;
    FirebaseFirestore mFirestore;

    public boolean getUserCaseAdded(String userUID) {
        return loginRepository.getUserCase(userUID);
    }

    public LoginViewModel() {
        mFirestore = FirebaseFirestore.getInstance();
        loginRepository = LoginRepository.getInstance();
    }



    public void createUser(FirebaseUser user){
        loginRepository.createUser(user);
    }






}
