package julien.hammer.go4lunch.di;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.location.LocationServices;

import julien.hammer.go4lunch.ui.MainApplication;
import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;
import julien.hammer.go4lunch.viewmodel.MapsViewModel;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final LocationRepository locationDataSource;
//    private final Executor executor;
    private static ViewModelFactory factory;
    private static PermissionCheck permissionCheck;
//    private final Executor ioExecutor = Executors.newFixedThreadPool(4);

    private ViewModelFactory(@NonNull PermissionCheck permissionCheck,
                             @NonNull LocationRepository locationDataSource) {
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
        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
            return (T) new MapsViewModel(
                    permissionCheck,
                    locationDataSource
            );
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
//        if (modelClass.isAssignableFrom(MapsViewModel.class)) {
//            return (T) new MapsViewModel(permissionChecker,locationDataSource);
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
