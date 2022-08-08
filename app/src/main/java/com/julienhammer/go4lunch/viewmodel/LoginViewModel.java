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
//        return mMutableLiveDataCase = loginRepository.getUserCase(user);
        return loginRepository.getUserCase(user);
    }

    public LoginViewModel() {
        loginRepository = LoginRepository.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

    }

    public static LoginViewModel getInstance(){
        LoginViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(MainRepository.class) {
            if (instance == null) {
                instance = new LoginViewModel();
            }
            return instance;
        }
    }

    public void createUser(FirebaseUser user){
        loginRepository.createUser(user);
    }






}
