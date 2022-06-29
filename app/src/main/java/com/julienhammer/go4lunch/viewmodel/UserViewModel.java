package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.julienhammer.go4lunch.data.user.UserRepository;
import com.julienhammer.go4lunch.models.User;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserViewModel extends ViewModel {
    private static volatile UserViewModel instance;
    private static UserRepository userRepository;

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

    public void createUser(){
        userRepository.createUser();
    }

    public Task<User> getUserData(){
        // Get the workmate from Firestore and cast it to a User model Object
        return userRepository.getUserData().continueWith(task -> task.getResult().toObject(User.class)) ;
    }

    public Task<Void> updateUserName(String userName){
        return userRepository.updateUserName(userName);
    }

    public void updatePlaceId(String wkmPlaceId){
        userRepository.updatePlaceId(wkmPlaceId);
    }

//    public Task<Void> deleteWorkmate(Context context){
//        // Delete the workmate account from the Auth
//        return workmateRepository.deleteWorkmate(context).addOnCompleteListener(task -> {
//            // Once done, delete the user datas from Firestore
//            workmateRepository.deleteWorkmateFromFirestore();
//        });
//    }

}
