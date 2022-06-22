package com.julienhammer.go4lunch.data.user;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserRepository {
    private static volatile UserRepository instance;
    private UserRepository(){}

    public static UserRepository getInstance() {
        UserRepository result = instance;
        if (result != null){
            return result;
        }
        synchronized (UserRepository.class){
            if (instance == null){
                instance = new UserRepository();
            }
            return instance;
        }
    }
}
