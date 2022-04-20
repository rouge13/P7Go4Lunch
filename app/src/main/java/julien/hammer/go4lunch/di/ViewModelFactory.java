package julien.hammer.go4lunch.di;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;
import julien.hammer.go4lunch.viewmodel.LocationViewModel;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository locationDataSource;
//    private final Executor executor;
    private static ViewModelFactory factory;
    private static PermissionCheck permissionChecker;
    private final Executor ioExecutor = Executors.newFixedThreadPool(4);
    private FusedLocationProviderClient mFusedLocationClient;

    private ViewModelFactory() {
        locationDataSource = new LocationRepository(LocationServices.getFusedLocationProviderClient());
    }

    public static ViewModelFactory getInstance() {
        if (factory == null) {
            synchronized (ViewModelFactory.class) {
                if (factory == null) {
                    factory = new ViewModelFactory();

                }
            }
        }
        return factory;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
            return (T) new LocationViewModel(permissionChecker,locationDataSource);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }

//    private ViewModelFactory(LocationRepository locationDataSource, Executor executor) {
//        this.locationDataSource = locationDataSource;
//        this.executor = executor;
//    }
//
//    public static ViewModelFactory getInstance(LocationRepository locationDataSource, Executor executor) {
//        if (factory == null) {
//            synchronized (ViewModelFactory.class) {
//                if (factory == null) {
//                    factory = new ViewModelFactory(locationDataSource, executor);
//                }
//            }
//        }
//        return factory;
//    }

//    @NonNull
//    @Override
//    public <T extends ViewModel> T create(Class<T> modelClass) {
//        if (modelClass.isAssignableFrom(LocationViewModel.class)) {
//            return (T) new LocationViewModel(permissionChecker,locationDataSource);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }

//    @NotNull
//    @Override
//    public <T extends ViewModel>  T create(Class<T> modelClass) {
//
//        if (modelClass.isAssignableFrom(TaskViewModel.class)) {
//            return (T) new TaskViewModel(projectDataSource, taskDataSource, executor);
//        }
//        throw new IllegalArgumentException("Unknown ViewModel class");
//    }
}
