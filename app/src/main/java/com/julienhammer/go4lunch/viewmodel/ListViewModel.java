package com.julienhammer.go4lunch.viewmodel;

import android.annotation.SuppressLint;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
import com.julienhammer.go4lunch.data.places.ListRepository;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ListViewModel extends ViewModel {
    @NonNull
    private final PermissionCheck permissionCheck;

    @NonNull
    private final ListRepository listRepository;

    public ListViewModel(
            @NonNull PermissionCheck permissionChecker,
            @NonNull ListRepository listRepository
    ) {
        this.permissionCheck = permissionChecker;
        this.listRepository = listRepository;
        LiveData<Location> locationForPlacesLiveData = listRepository.getLocationForPlacesLiveData();
    }

//    private final MutableLiveData<Boolean> hasGpsPermissionLiveData = new MutableLiveData<>();
//    public void onActivityCreated(@Nullable final Bundle SavedInstanceState){
//
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider((ViewModelStoreOwner) this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//    }

    public LiveData<Location> getLocationForPlacesLiveData() {
        return listRepository.getLocationForPlacesLiveData();
    }

    @SuppressLint("MissingPermission")
    public void refresh() {
        boolean hasGpsPermission = permissionCheck.hasLocationPermission();
//        hasGpsPermissionLiveData.setValue(hasGpsPermission);

        if (hasGpsPermission) {
            listRepository.startLocationRequest();
        } else {
            listRepository.stopLocationRequest();
        }
    }
}
