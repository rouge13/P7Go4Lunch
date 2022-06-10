package com.julienhammer.go4lunch.ui.map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.Photo;
import com.google.maps.model.PlacesSearchResult;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // 1 - FOR DATA
    private MapsViewModel mapsViewModel;
    private GoogleMap mMap;
//    ExecutorService executor = Executors.newFixedThreadPool(2);

    ExecutorService executor = Executors.newSingleThreadExecutor();
//    Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel

//    private void configureViewModel() {
//        this.mapsViewModel = new ViewModelProvider(
//                this,
//                ViewModelFactory.getInstance(
//                        new PermissionCheck(getActivity().getContext()),
//                        new LocationRepository(
//                                LocationServices.getFusedLocationProviderClient(getActivity())
//                        )
//                )
//        ).get(MapsViewModel.class);
//        //this.mapsViewModel.init();
//    }


//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//    }

    private void configureViewModel() {
        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
        MapsViewModel mapsViewModel =
                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
        mapsViewModel.refresh();
        this.mapsViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(MapsViewModel.class);
        //this.mapsViewModel.init();
    }

//    private void configureViewModel() {
//        this.mapsViewModel = new ViewModelProvider(
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
//        ).get(MapsViewModel.class);
//        //this.mapsViewModel.init();
//    }


//    // -- Get all the Last Location
//    private void getTheLastLocation() {
//        this.mapsViewModel.getLocationLiveData().observe(this,this::updateProjects);
//    }
//    private MapsViewModel mMapsViewModel;
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
        mMap = googleMap;
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider(this, MapsViewModelFactory).get(MapsViewModel.class);

        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            configureViewModel();

//            mapsViewModel.refresh();
//            ViewModelFactory MapsViewModelFactory = ViewModelFactory.getInstance();
//            MapsViewModel mapsViewModel =
//                    new ViewModelProvider(this, MapsViewModelFactory).get(MapsViewModel.class);
//            mapsViewModel.refresh();
//            mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                    mMap.clear();
                    mMap.getUiSettings().setMapToolbarEnabled(false);
//                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
//                LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
                    executor.execute(() -> {
                        PlacesSearchResult[] placesSearchResults = new NearbySearch().run(getString(R.string.google_map_key),location).results;
                        mainExecutor.execute(()->{
                            // MOVE THE CAMERA TO THE USER LOCATION
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

                            // DISPLAY BLUE DOT FOR USER LOCATION
                            mMap.setMyLocationEnabled(true);

                            // ZOOM IN, ANIMATE CAMERA
                            mMap.animateCamera(CameraUpdateFactory.zoomIn());
                            // TO DO
                            //searchModelsList is the list of all markers
                            Marker[] allMarkers = new Marker[placesSearchResults.length];
                            for (int i = 0; i <= (placesSearchResults.length) -1; i++){
                                double latPlace = placesSearchResults[i].geometry.location.lat;
                                double longPlace = placesSearchResults[i].geometry.location.lng;
                                String placeName = placesSearchResults[i].name;
//                                String placeAddress = placesSearchResults[i].vicinity.toLowerCase();
//                                float placeRating = placesSearchResults[i].rating;
//                                Photo placePhoto = placesSearchResults[i].photos[0];
//                                Integer placeUserRatingsTotal = placesSearchResults[i].userRatingsTotal;

                                if (googleMap != null) {
//                                    googleMap.setOnMarkerClickListener(this);
                                    allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(new LatLng(latPlace, longPlace)).title(placeName));
//                                    allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(new LatLng(latPlace, longPlace)).title(placeName));
//                                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latPlace, longPlace), 17.0f));
//                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latPlace, longPlace), 17));
//                                    mMap.addMarker(new MarkerOptions().position(new LatLng(latPlace, longPlace)).title(placeLoc));
                                }
                            }
//                            double lat2 = placesSearchResults[1].geometry.location.lat;
//                    double lng2 = placesSearchResults[1].geometry.location.lng;
//                    googleMap.addMarker(new MarkerOptions().position(new LatLng(lat1, lng1)));

                        });
                    });



//                // ZOOM IN, ANIMATE CAMERA
//                googleMap.animateCamera(CameraUpdateFactory.zoomIn());




//            mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(),location -> );
//            LatLng userLocation = new LatLng( (Location)
//                    Objects.requireNonNull(
//                            mapsViewModel.getLocationLiveData()
//            );
//            LatLng userLocation = new LatLng(
//                    Objects.requireNonNull(
//                        mapsViewModel.getLocationLiveData().getValue()).getLatitude(),
//                    Objects.requireNonNull(
//                            mapsViewModel.getLocationLiveData().getValue()).getLongitude()
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
//        super.onCreate(savedInstanceState);
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//        configureViewModel();
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

//        if (mapsViewModel == null) {
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
//        mapsViewModel.refresh();
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        mapsViewModel.refresh();
    }


}
