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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;

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
        mLocationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {
                userLocation = location;
                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                        .zoom(14).build();
                mMap.getUiSettings().setMapToolbarEnabled(false);
                mMap.setMyLocationEnabled(true);
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                ArrayList<Marker> allMarkers = new ArrayList<Marker>();
                for (int i = 0; i <= placesSearchResults.length -1; i++){
                    String openNowText = "";
                    String photoRef = "";
                    String mMissingPhoto = MISSING_PHOTO_REFERENCE;
                    if (placesSearchResults[i].permanentlyClosed){
                        i++;
                    } else {
                        openNowText = getString(getOpenHourTextId(placesSearchResults[i].openingHours != null
                                ? placesSearchResults[i].openingHours.openNow : null));
                        if (placesSearchResults[i].photos != null) {
                            photoRef = placesSearchResults[i].photos[0].photoReference;
                        } else {
                            photoRef = mMissingPhoto;
                        }
                        LatLng resLocation = new LatLng(placesSearchResults[i].geometry.location.lat , placesSearchResults[i].geometry.location.lng);
                        RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId, placesSearchResults[i].name, placesSearchResults[i].vicinity, photoRef, openNowText, placesSearchResults[i].rating, resLocation);
                        SharedPreferences prefs = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
                        String restaurantChoicedId = prefs.getString(PLACE_ID,"");
                        if (Objects.equals(restaurantChoicedId, placesSearchResults[i].placeId)) {
                            saveValueOfTheRestaurantChoiceAllDataNeeded(placesSearchResults[i].placeId, placesSearchResults[i].name, placesSearchResults[i].vicinity, photoRef, openNowText, placesSearchResults[i].rating, (float) placesSearchResults[i].geometry.location.lat, (float) placesSearchResults[i].geometry.location.lng);
                        }
                        allRestaurants.add(restaurantDetails);
                        MarkerOptions markerOptions = new MarkerOptions().position(
                                new LatLng(placesSearchResults[i].geometry.location.lat, placesSearchResults[i].geometry.location.lng)).
                                title(placesSearchResults[i].name);
                        allMarkers.add(mMap.addMarker(markerOptions));
                    }
                }
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(@NonNull Marker marker) {
                        String markerName = marker.getTitle();
                        for (int i = 0; i<allMarkers.size(); i++){
                            if (Objects.equals(allMarkers.get(i).getTitle(), markerName)){
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
            return R.string.openNowCaseNotShowing;
        }
        if (openNow) {
            return R.string.openNowCaseTrue;
        } else {
            return R.string.openNowCaseFalse;
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
        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
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
