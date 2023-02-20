package com.julienhammer.go4lunch.di;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;

import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.data.workmate.WorkmateRepository;
import com.julienhammer.go4lunch.ui.MainApplication;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;
import com.julienhammer.go4lunch.viewmodel.SharedRestaurantSelectedViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ViewModelFactory implements ViewModelProvider.Factory {
    private final LocationRepository locationDataSource;
    private final LoginRepository loginDataSource;
    private final WorkmateRepository workmateDataSource;
    private static ViewModelFactory factory;
    private static PermissionCheck permissionCheck;
    private ViewModelFactory(@NonNull PermissionCheck permissionCheck,
                             @NonNull LocationRepository locationDataSource,
                             @NonNull LoginRepository loginDataSource,
                             @NonNull WorkmateRepository workmateDataSource
                             ) {
        ViewModelFactory.permissionCheck = permissionCheck;
        this.locationDataSource = locationDataSource;
        this.loginDataSource = loginDataSource;
        this.workmateDataSource = workmateDataSource;

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
                            ),
                            LoginRepository.getInstance(),
                            WorkmateRepository.getInstance()
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
        } else if (modelClass.isAssignableFrom(WorkmateViewModel.class)) {
            return (T) new WorkmateViewModel(workmateDataSource);
        } else if (modelClass.isAssignableFrom(UserViewModel.class)) {
            return (T) new UserViewModel();
        } else if (modelClass.isAssignableFrom(LoginViewModel.class)) {
            return (T) new LoginViewModel(loginDataSource);
        } else if (modelClass.isAssignableFrom(RestaurantsViewModel.class)) {
            return (T) new RestaurantsViewModel();
        } else if (modelClass.isAssignableFrom(InfoRestaurantViewModel.class)) {
            return (T) new InfoRestaurantViewModel();
        } else if (modelClass.isAssignableFrom(SharedRestaurantSelectedViewModel.class)) {
            return (T) new SharedRestaurantSelectedViewModel();
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

}
