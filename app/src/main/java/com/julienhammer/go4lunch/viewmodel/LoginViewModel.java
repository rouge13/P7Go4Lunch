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

    public boolean getUserCaseAdded(FirebaseUser user) {
        return loginRepository.getUserCase(user);
    }

    public LoginViewModel() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    public void createUser(FirebaseUser user){
        loginRepository.createUser(user);
    }






}
