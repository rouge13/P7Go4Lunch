package julien.hammer.go4lunch.data.location;

import android.app.Application;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import julien.hammer.go4lunch.ui.map.MapsFragment;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LocationRepository {
    private static final int LOCATION_REQUEST_INTERVAL_MS = 10_000;
    private static final float SMALLEST_DISPLACEMENT_THRESHOLD_METER = 25;

    @NonNull
    private final FusedLocationProviderClient fusedLocationProviderClient;

    @NonNull
    private final MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>(null);

    private LocationCallback callback;

    public LocationRepository(@NonNull FusedLocationProviderClient fusedLocationProviderClient) {
        this.fusedLocationProviderClient = fusedLocationProviderClient;
    }

    public LiveData<Location> getLocationLiveData() {
        return locationMutableLiveData;
    }

    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    public void startLocationRequest() {
        if (callback == null) {
            callback = new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    Location location = locationResult.getLastLocation();

                    locationMutableLiveData.setValue(location);
                }
            };
        }

        fusedLocationProviderClient.removeLocationUpdates(callback);

        fusedLocationProviderClient.requestLocationUpdates(
                LocationRequest.create()
                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
                callback,
                Looper.getMainLooper()
        );



//        locationMutableLiveData.setValue(fusedLocationProviderClient.getLastLocation().getResult());
//        if (callback == null) {
//            callback = new LocationCallback() {
//                @Override
//                public void onLocationResult(@NonNull LocationResult locationResult) {
//                    Location location = locationResult.getLastLocation();
//                    if (locationResult != null && !locationResult.getLocations().isEmpty()) {
//                        Location newLocation = locationResult.getLocations().get(0);
//                        locationMutableLiveData.setValue(newLocation);
////                                    callback(Status.RESULT_SUCCESS, newLocation);
//                    } else {
////                                    callback.onCallback(Status.ERROR_LOCATION, null);
//                    }
//                }
//            };
//
//        }
//
//        fusedLocationProviderClient.removeLocationUpdates(callback);
//
//        fusedLocationProviderClient.requestLocationUpdates(
//                LocationRequest.create()
//                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
//                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
//                callback,
//                Looper.getMainLooper()
//        );

//        // Location settings successful
//        fusedLocationProviderClient.getLastLocation()
//                .addOnSuccessListener(Location) {
//            location ->
//            if (location == null || location.accuracy > 100) {
//                mLocationCallback = object : LocationCallback() {
//                    override fun onLocationResult(locationResult: LocationResult?) {
//                        stopLocationUpdates()
//                        if (locationResult != null && locationResult.locations.isNotEmpty()) {
//                            val newLocation = locationResult.locations[0]
//                            callback.onCallback(Status.SUCCESS, newLocation)
//                        } else {
//                            callback.onCallback(Status.ERROR_LOCATION, null)
//                        }
//                    }
//                }
//
//                mFusedLocationProviderClient!!.requestLocationUpdates(getLocationRequest(),
//                        mLocationCallback, null)
//            } else {
//                callback.onCallback(Status.SUCCESS, location)
//            }
//        }
//                        .addOnFailureListener {
//            callback.onCallback(Status.ERROR_UNKNOWN, null)
//        }
//    }



//        if (callback == null) {
//            callback = new LocationCallback() {
//                @Override
//                public void onLocationResult(@NonNull LocationResult locationResult) {
//                    for (Location location : locationResult.getLocations()) {
//                        if (location != null) {
//                            locationMutableLiveData.setValue(location);
//                        }
//                    }
////                    Location location = locationResult.getLastLocation();
//////                    locationMutableLiveData.setValue(fusedLocationProviderClient.getLastLocation().getResult());
////                    locationMutableLiveData.setValue(location);
//                }
//            };
//        }
//
//        fusedLocationProviderClient.removeLocationUpdates(callback);
//
//        fusedLocationProviderClient.requestLocationUpdates(
//                LocationRequest.create()
//                        .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//                        .setSmallestDisplacement(SMALLEST_DISPLACEMENT_THRESHOLD_METER)
//                        .setInterval(LOCATION_REQUEST_INTERVAL_MS),
//                callback,
//                Looper.getMainLooper()
//        );
    }

    public void stopLocationRequest() {
        if (callback != null) {
            fusedLocationProviderClient.removeLocationUpdates(callback);
        }
    }



}
