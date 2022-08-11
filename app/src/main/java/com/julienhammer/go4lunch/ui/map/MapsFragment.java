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

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // 1 - FOR DATA
    private MapsViewModel mapsViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;
    private GoogleMap mMap;
    private PlacesSearchResult mRestaurants;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel

    private void configureViewModel() {
        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
        mapsViewModel =
                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
    }

    private void initRestaurantsViewModel() {
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel =
                new ViewModelProvider(requireActivity(), restaurantsViewModelFactory).get(RestaurantsViewModel.class);
    }

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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            configureViewModel();
            mapsViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                mMap.clear();
                mMap.getUiSettings().setMapToolbarEnabled(false);
                Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
                executor.execute(() -> {


//                    PlacesSearchResult[] placesSearchResults = new NearbySearch().run(getString(R.string.google_map_key),location).results;





                    mainExecutor.execute(()->{
                        // MOVE THE CAMERA TO THE USER LOCATION
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 15));

                        // DISPLAY BLUE DOT FOR USER LOCATION
                        mMap.setMyLocationEnabled(true);

                        // ZOOM IN, ANIMATE CAMERA
                        mMap.animateCamera(CameraUpdateFactory.zoomIn());
                        // TO DO
                        //searchModelsList is the list of all markers
                        mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {
                        Marker[] allMarkers = new Marker[placesSearchResults.length];
                        for (int i = 0; i <= (placesSearchResults.length) -1; i++){
                            double latPlace = placesSearchResults[i].geometry.location.lat;
                            double longPlace = placesSearchResults[i].geometry.location.lng;
                            String placeName = placesSearchResults[i].name;
                            if (googleMap != null) {
                                allMarkers[i] = googleMap.addMarker(new MarkerOptions().position(new LatLng(latPlace, longPlace)).title(placeName));
                            }
                        }
                    });
                    });
                });
            });
        }
    }

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
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        initRestaurantsViewModel();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


}
