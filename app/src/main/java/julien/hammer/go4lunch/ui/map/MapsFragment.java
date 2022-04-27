package julien.hammer.go4lunch.ui.map;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.BindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
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

import java.util.Objects;
import java.util.concurrent.Executor;

import julien.hammer.go4lunch.R;
import julien.hammer.go4lunch.data.location.LocationRepository;
import julien.hammer.go4lunch.data.permission_check.PermissionCheck;
import julien.hammer.go4lunch.di.ViewModelFactory;
import julien.hammer.go4lunch.viewmodel.LocationViewModel;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // 1 - FOR DATA
    private LocationViewModel locationViewModel;

    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

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


//    // -- Get all the Last Location
//    private void getTheLastLocation() {
//        this.locationViewModel.getLocationLiveData().observe(this,this::updateProjects);
//    }
//    private LocationViewModel mLocationViewModel;
//    private OnMapReadyCallback callback = new OnMapReadyCallback() {

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                googleMap.clear();

                // MOVE THE CAMERA TO THE USER LOCATION
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));

                // DISPLAY BLUE DOT FOR USER LOCATION
                googleMap.setMyLocationEnabled(true);

                // ZOOM IN, ANIMATE CAMERA
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
//            locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(),location -> );
//            LatLng userLocation = new LatLng( (Location)
//                    Objects.requireNonNull(
//                            locationViewModel.getLocationLiveData()
//            );
//            LatLng userLocation = new LatLng(
//                    Objects.requireNonNull(
//                        locationViewModel.getLocationLiveData().getValue()).getLatitude(),
//                    Objects.requireNonNull(
//                            locationViewModel.getLocationLiveData().getValue()).getLongitude()
//            );
//            googleMap.addMarker(new MarkerOptions().position(userLocation).title("Marker User Location Last Saved"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLocation));
//            LatLng sydney = new LatLng(-34, 151);
//            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            });
        }
    }

//    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureViewModel();
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );

        SupportMapFragment mapFragment =
                    (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
            assert mapFragment != null;
            mapFragment.getMapAsync(this);


//        ActivityCompat.requestPermissions(
//                requireActivity(),
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                0
//        );

//        if (locationViewModel == null) {
//            initMap();
//        }
//        getMapAsync(callback);
//         getMapAsync(this);

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
//
//    private void initMap() {
//        //Init map
//        SupportMapFragment mapFragment =
//                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        if (mapFragment != null) {
//            getMapAsync(callback);
//        }
//    }

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
////
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        locationViewModel.refresh();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationViewModel.refresh();
    }


}
