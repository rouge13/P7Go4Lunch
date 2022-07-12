package com.julienhammer.go4lunch.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.main.MainRepository;
import com.julienhammer.go4lunch.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class MainViewModel extends ViewModel {
    private static volatile MainViewModel instance;
    private static MainRepository mainRepository;
    MutableLiveData<User> mMutableLiveData;
    FirebaseFirestore mFirestore;

    public MutableLiveData<User> getmMutableLiveData() {
        return mMutableLiveData;
    }

    public MainViewModel() {
        mainRepository = MainRepository.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mMutableLiveData = mainRepository.getUserMutableLiveData();
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

//         Get the user from Firestore and cast it to a User model Object
        //                mMutableLiveData.setValue(user);
        return mainRepository.getUserData().continueWithTask(task -> {
//        return mainRepository.getUserData().continueWithTask(task -> {
//                return task.getResult().toObject(User.class);
            synchronized (this) {
                if (!task.isSuccessful()) {
                    return Tasks.forException(Objects.requireNonNull(task.getException()));
                } else if (task.isComplete()) {
                    return Tasks.forResult(task.getResult().toObject(User.class));
                }

            }
//        }).addOnCompleteListener(Task::getResult);

            return null;
        });
//                .addOnCompleteListener(new OnCompleteListener<User>() {
//            @Override
//            public void onComplete(@NonNull Task<User> task) {
//                if (task.isSuccessful()){
//                    User user = task.getResult();
//                    mMutableLiveData.setValue(user);
//                }
//            }
//        });

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
