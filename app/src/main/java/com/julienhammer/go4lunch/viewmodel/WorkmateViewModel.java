package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.FirebaseFirestore;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;

import java.util.List;
import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateViewModel extends ViewModel {

    private static volatile WorkmateViewModel instance;
    private final WorkmateRepository workmateRepository;
    MutableLiveData<List<User>> mMutableLiveData;
    FirebaseFirestore mFirestore;

    public LiveData<List<User>> getWorkmateMutableLiveData() {
        return mMutableLiveData;
    }

    public WorkmateViewModel(){
        workmateRepository = WorkmateRepository.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mMutableLiveData = workmateRepository.getWorkmateMutableLiveData();
    }

    public static WorkmateViewModel getInstance(){
        WorkmateViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(WorkmateRepository.class) {
            if (instance == null) {
                instance = new WorkmateViewModel();
            }
            return instance;
        }
    }

//    public void createWorkmate(){
//        workmateRepository.createWorkmate();
//    }

//    public Task<User> getWorkmateData(){
//        // Get the workmate from Firestore and cast it to a Workmate model Object
//        return workmateRepository.getWorkmateData().continueWithTask(task -> {
//            synchronized (this) {
//                if (!task.isSuccessful()) {
//                    return Tasks.forException(Objects.requireNonNull(task.getException()));
//                } else if (task.isComplete()) {
//                    return Tasks.forResult(task.getResult().toObject(User.class));
//                }
//
//            }
//            return null;
//        });
//
//    }



}
