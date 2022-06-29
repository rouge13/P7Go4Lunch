package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.data.user.UserRepository;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateViewModel extends ViewModel {

    private static volatile WorkmateViewModel instance;
    private static WorkmateRepository workmateRepository;

    private WorkmateViewModel(){
        workmateRepository = workmateRepository.getInstance();
    }

    public static UserViewModel getInstance(){
        WorkmateViewModel result = instance;
        if (result != null) {
            return result;
        }
        synchronized(workmateRepository.class) {
            if (instance == null) {
                instance = new WorkmateViewModel();
            }
            return instance;
        }
    }
}
