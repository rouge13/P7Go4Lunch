package com.julienhammer.go4lunch.viewmodel;

import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.Task;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.models.Workmate;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class WorkmateViewModel extends ViewModel {

    private static volatile WorkmateViewModel instance;
    private static WorkmateRepository workmateRepository;

    public WorkmateViewModel(){
        workmateRepository = WorkmateRepository.getInstance();
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

    public void createWorkmate(){
        workmateRepository.createWorkmate();
    }

    public Task<Workmate> getWorkmateData(){
        // Get the workmate from Firestore and cast it to a Workmate model Object
        return workmateRepository.getWorkmateData().continueWith(task -> task.getResult().toObject(Workmate.class)) ;
    }



}
