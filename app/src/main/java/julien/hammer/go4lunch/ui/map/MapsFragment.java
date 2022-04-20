package julien.hammer.go4lunch.ui.map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.app.Application;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.concurrent.Executor;

import julien.hammer.go4lunch.R;
import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;
import julien.hammer.go4lunch.di.ViewModelFactory;
import julien.hammer.go4lunch.viewmodel.LocationViewModel;

public class MapsFragment extends SupportMapFragment {
    // 1 - FOR DATA
    private LocationViewModel locationViewModel;


    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel

//    private void configureViewModel() {
//        this.locationViewModel = new ViewModelProvider(
//                this,
//                ViewModelFactory.getInstance(
//                        new PermissionCheck(getActivity().getContext()),
//                        new LocationRepository(
//                                LocationServices.getFusedLocationProviderClient(getActivity())
//                        )
//                )
//        ).get(LocationViewModel.class);
//        //this.locationViewModel.init();
//    }

    private void configureViewModel() {
        this.locationViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(LocationViewModel.class);
        //this.locationViewModel.init();
    }

//    private void configureViewModel() {
//        this.locationViewModel = new ViewModelProvider(
//                this,
//                ViewModelFactory.getInstance(
//                        new LocationRepository(
//                                LocationServices.getFusedLocationProviderClient(getActivity())
//                        ),
//
//                        new Executor() {
//                            @Override
//                            public void execute(Runnable command) {
//
//                            }
//                        }
//
//                )
//        ).get(LocationViewModel.class);
//        //this.locationViewModel.init();
//    }


    // -- Get all the Last Location
    private void getTheLastLocation() {
        this.locationViewModel.getLocationLiveData().observe(this,this::updateProjects);
    }
    private LocationViewModel mLocationViewModel;
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    };

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             @Nullable ViewGroup container,
//                             @Nullable Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_maps, container, false);
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
        getMapAsync(callback);

//        clientLocation = LocationServices.getFusedLocationProviderClient(getActivity());
//        clientLocation.getLastLocation()
//                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
//                    @Override
//                    public void onSuccess(Location location) {
//                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            // Logic to handle location object
//                        }
//                    }
//                });

//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            mapFragment.getMapAsync(callback);
//        }
    }

//    @BindingAdapter("initMap")
//    public static void initMap(final MapView mapView, final LatLng latLng) {
//
//        if (mapView != null) {
//            mapView.onCreate(new Bundle());
//            mapView.getMapAsync(new OnMapReadyCallback() {
//                @Override
//                public void onMapReady(GoogleMap googleMap) {
//                    // Add a marker
//                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker in India"));
//                }
//            });
//        }
//    }

}
