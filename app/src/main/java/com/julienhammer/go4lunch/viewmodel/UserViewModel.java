package com.julienhammer.go4lunch.viewmodel;

import com.julienhammer.go4lunch.data.user.UserRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserViewModel {
    private static volatile UserViewModel instance;
    private UserRepository userRepository;

    private UserViewModel(){
        userRepository = UserRepository.getInstance();
    }

    public static UserViewModel getInstance(){
        UserViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(UserRepository.class) {
            if (instance == null) {
                instance = new UserViewModel();
            }
            return instance;
        }
    }
}
