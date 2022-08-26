package com.julienhammer.go4lunch.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;

import com.julienhammer.go4lunch.ui.MainApplication;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository locationDataSource;
    private static ViewModelFactory factory;
    private static PermissionCheck permissionCheck;
    private ViewModelFactory(@NonNull PermissionCheck permissionCheck,
                             @NonNull LocationRepository locationDataSource
                             ) {
        ViewModelFactory.permissionCheck = permissionCheck;
        this.locationDataSource = locationDataSource;
    }

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    Application application = MainApplication.getApplication();
                    factory = new ViewModelFactory(
                            new PermissionCheck(
                                    application
                            ),
                            new LocationRepository(
                                    LocationServices.getFusedLocationProviderClient(
                                            application
                                    )
                            )
                    );
                }
            }
        }
        return factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
            return (T) new LocationViewModel(
                    permissionCheck,
                    locationDataSource
            );
        } else if (modelClass.isAssignableFrom(ListViewModel.class)) {
            return (T) new ListViewModel(
                    permissionCheck,
                    locationDataSource
            );
        } else if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel();
        } else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel();
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel();
        } else if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel();
        } else if (modelClass.isAssignableFrom(InfoRestaurantViewModel.class)) {
            return (T) new InfoRestaurantViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
