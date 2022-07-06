package com.julienhammer.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.julienhammer.go4lunch.data.user.MainRepository;
import com.julienhammer.go4lunch.models.User;

import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainViewModel extends ViewModel {
    private static volatile MainViewModel instance;
    private static MainRepository mainRepository;

    public MainViewModel(){
        mainRepository = MainRepository.getInstance();
    }

    public static MainViewModel getInstance(){
        MainViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(MainRepository.class) {
            if (instance == null) {
                instance = new MainViewModel();
            }
            return instance;
        }
    }

    public void createUser(){
        mainRepository.createUser();
    }

    public Task<User> getUserData(){

        // Get the user from Firestore and cast it to a User model Object

        return mainRepository.getUserData().continueWithTask(Executors.newSingleThreadExecutor(), task -> {
//                return task.getResult().toObject(User.class);
                    synchronized (this) {
                        if (task.isSuccessful()) {
                            return Tasks.forResult(task.getResult().toObject(User.class));
                        } else {
                            return Tasks.forException(Objects.requireNonNull(task.getException()));
                        }
                    }
        });



    }

//
//    public void updatePlaceId(String wkmPlaceId){
//        mainRepository.updatePlaceId(wkmPlaceId);
//    }

//    public Task<Void> deleteWorkmate(Context context){
//        // Delete the workmate account from the Auth
//        return workmateRepository.deleteWorkmate(context).addOnCompleteListener(task -> {
//            // Once done, delete the user datas from Firestore
//            workmateRepository.deleteWorkmateFromFirestore();
//        });
//    }

}
