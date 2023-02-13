package com.julienhammer.go4lunch.ui.map;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;


import android.Manifest;
import android.annotation.SuppressLint;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.PlacesResponse;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

import static android.content.Context.MODE_PRIVATE;

public class RestaurantMapsFragment extends Fragment implements OnMapReadyCallback {
    // 1 - FOR DATA
    private LocationViewModel mLocationViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;
    private InfoRestaurantViewModel mInfoRestaurantViewModel;
    private GoogleMap mMap;
    private static final String MISSING_PHOTO_REFERENCE = "%20image%20missing%20reference";
    private Location userLocation = null;

    public static RestaurantMapsFragment newInstance() {
        return new RestaurantMapsFragment();
    }

    SupportMapFragment mapFragment = null;
    CameraPosition cameraPosition = null;
    private static String PLACE_ID = "placeId";
    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String RESTAURANT_OPEN_NOW = "openNowRes";
    private static String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static String RESTAURANT_RATING = "ratingRes";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";
    ArrayList<Marker> allMarkers = new ArrayList<>();
    BitmapDescriptor bitmapDescriptor;

    private void configureViewModel() {
        ViewModelFactory locationViewModelFactory = ViewModelFactory.getInstance();
        mLocationViewModel =
                new ViewModelProvider(requireActivity(), locationViewModelFactory).get(LocationViewModel.class);
    }

    private void initRestaurantsViewModel() {
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel =
                new ViewModelProvider(requireActivity(), restaurantsViewModelFactory).get(RestaurantsViewModel.class);
    }

    private void initInfoRestaurantViewModel() {
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel =
                new ViewModelProvider(requireActivity(), infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
    }

    @SuppressLint({"MissingPermission", "PotentialBehaviorOverride"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        configureViewModel();
        initRestaurantsViewModel();
        initInfoRestaurantViewModel();
        mRestaurantsViewModel.getAllRestaurantChoosed().observe(this, list -> {
            mLocationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                userLocation = location;
                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()))
                        .zoom(14).build();
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mRestaurantsViewModel.getNearbyPlaces().observe(getViewLifecycleOwner(), places -> {
                    ArrayList<RestaurantDetails> allRestaurants = new ArrayList<>();
                    for (int i = 0; i <= places.results.size() - 1; i++) {
                        i = initPlacesSearchResult(places.results, allRestaurants, allMarkers, i, list);
                    }
                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            String markerName = marker.getTitle();
                                    mRestaurantsViewModel.getIfEatingHere().observe(getViewLifecycleOwner(), isEatingHere -> {
                                        if (isEatingHere) {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                        } else {
                                            marker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                        }
                                    });
                            for (int i = 0; i < allMarkers.size(); i++) {
                                if (Objects.equals(allMarkers.get(i).getTitle(), markerName)) {
                                    mInfoRestaurantViewModel.setInfoRestaurant(allRestaurants.get(i));
                                    EventBus.getDefault().post(new ShowInfoRestaurantDetailEvent(allRestaurants.get(i)));
                                    break;
                                }
                            }
                            return false;
                        }
                    });
                });
            });
        });
    }

    private int initPlacesSearchResult(ArrayList<PlacesResponse.Result> results, ArrayList<RestaurantDetails> allRestaurants, ArrayList<Marker> allMarkers, int i, List<String> listOfRestaurantsChoosed) {
        String openNowText = "";
        String photoRef = "";
        String mMissingPhoto = MISSING_PHOTO_REFERENCE;
        openNowText = getString(getOpenHourTextId(results.get(i).opening_hours != null
                ? results.get(i).opening_hours.open_now : null));
        if (results.get(i).photos != null) {
            photoRef = results.get(i).photos.get(0).photo_reference;
        } else {
            photoRef = mMissingPhoto;
        }
        LatLng resLocation = new LatLng(results.get(i).geometry.location.lat, results.get(i).geometry.location.lng);
        RestaurantDetails restaurantDetails = new RestaurantDetails(results.get(i).place_id, results.get(i).name, results.get(i).formatted_address, photoRef, openNowText, (float) results.get(i).rating, resLocation);
        SharedPreferences prefs = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        String restaurantChoicedId = prefs.getString(PLACE_ID, "");
        if (Objects.equals(restaurantChoicedId, results.get(i).place_id)) {
            saveValueOfTheRestaurantChoiceAllDataNeeded(results.get(i).place_id, results.get(i).name, results.get(i).formatted_address, photoRef, openNowText, (float) results.get(i).rating, (float) results.get(i).geometry.location.lat, (float) results.get(i).geometry.location.lng);
        }
        allRestaurants.add(restaurantDetails);
        AtomicReference<ArrayList<String>> filteredResName  = new AtomicReference<>(new ArrayList<>());
        initMarker(allMarkers, results.get(i), listOfRestaurantsChoosed, filteredResName);
        mRestaurantsViewModel.getAllSearchFilteredRestaurant().observe(getViewLifecycleOwner(), resultFilteredRestaurantName -> {
            filteredResName.set(resultFilteredRestaurantName);
            initMarker(allMarkers, results.get(i), listOfRestaurantsChoosed, filteredResName);
        });
        return i;
    }

    private void initMarker(ArrayList<Marker> allMarkers, PlacesResponse.Result result, List<String> listOfRestaurantsChoosed, AtomicReference<ArrayList<String>> resultFilteredRestaurantName) {
        if (listOfRestaurantsChoosed.contains(result.place_id) && !resultFilteredRestaurantName.get().contains(result.name)) {
            bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
        } else if (resultFilteredRestaurantName.get().contains(result.name)){
            bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
        } else {
            bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
        }
        initMarkerColor(allMarkers, result);
    }

    private void initMarkerColor(ArrayList<Marker> allMarkers, PlacesResponse.Result result) {
        MarkerOptions markerOptions = new MarkerOptions().position(
                        new LatLng(result.geometry.location.lat, result.geometry.location.lng)).
                title(result.name).icon(bitmapDescriptor);
        allMarkers.add(mMap.addMarker(markerOptions));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @NonNull
    public int getOpenHourTextId(Boolean openNow) {
        if (openNow == null) {
            return R.string.open_now_case_not_showing;
        }
        if (openNow) {
            return R.string.open_now_case_true;
        } else {
            return R.string.open_now_case_false;
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                0
        );
        mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void saveValueOfTheRestaurantChoiceAllDataNeeded(String placeId, String nameRes, String addressRes, String photoRefRes, String openNowRes, float ratingRes, float latRes, float lngRes) {
        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shChoice.edit();
        myEdit.putString(PLACE_ID, placeId);
        myEdit.putString(RESTAURANT_NAME, nameRes);
        myEdit.putString(RESTAURANT_ADDRESS, addressRes);
        myEdit.putString(RESTAURANT_PHOTO_REF, photoRefRes);
        myEdit.putString(RESTAURANT_OPEN_NOW, openNowRes);
        myEdit.putFloat(RESTAURANT_RATING, ratingRes);
        myEdit.putFloat(RESTAURANT_LAT, latRes);
        myEdit.putFloat(RESTAURANT_LNG, lngRes);
        myEdit.apply();
    }

}
