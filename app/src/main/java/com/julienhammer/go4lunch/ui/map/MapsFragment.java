package com.julienhammer.go4lunch.ui.map;

import android.content.SharedPreferences;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.model.PlacesSearchResult;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class MapsFragment extends Fragment implements OnMapReadyCallback {
    // 1 - FOR DATA
    private LocationViewModel mLocationViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;
    private InfoRestaurantViewModel mInfoRestaurantViewModel;
    private GoogleMap mMap;
    private PlacesSearchResult mRestaurants;
    //    ExecutorService executor = Executors.newSingleThreadExecutor();
    private Location userLocation = null;
    public static MapsFragment newInstance() {
        return new MapsFragment();
    }
    SupportMapFragment mapFragment = null;
    CameraPosition cameraPosition = null;
    private static String PLACE_ID = "placeId";
    private static final String MY_SEARCH_ON_COMPLETE = "searchRestaurant";
    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String RESTAURANT_OPEN_NOW = "openNowRes";
    private static String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static String RESTAURANT_RATING = "ratingRes";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";

//    final CameraPosition currentCameraPosition = null;
    //    MapView mapView = null;

    // -------------------
    // DATA
    // -------------------

    // 2 - Configuring ViewModel

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */

    @SuppressLint({"MissingPermission", "PotentialBehaviorOverride"})
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        configureViewModel();
        initRestaurantsViewModel();
        initInfoRestaurantViewModel();
//            mMap.clear();

//            Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
//            executor.execute(() -> {


//                mainExecutor.execute(()->{
        mLocationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {

            mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults ->
            {
                userLocation = location;

                cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(userLocation.getLatitude(),userLocation.getLongitude()))
                        .zoom(14).build();

                mMap.getUiSettings().setMapToolbarEnabled(false);
                // MOVE THE CAMERA TO THE USER LOCATION
//                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 14));

                // DISPLAY BLUE DOT FOR USER LOCATION
                mMap.setMyLocationEnabled(true);

                // ZOOM IN, ANIMATE CAMERA
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // TO DO
                //searchModelsList is the list of all markers
//                int permanentlyClosed = 0;
                ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                ArrayList<Marker> allMarkers = new ArrayList<Marker>();
//                RestaurantDetails[] allRestaurants = new RestaurantDetails[placesSearchResults.length - permanentlyClosed];
//                Marker[] allMarkers = new Marker[allRestaurants.length];
                for (int i = 0; i <= placesSearchResults.length -1; i++){
                    String placeId = placesSearchResults[i].placeId;
                    double latPlace = placesSearchResults[i].geometry.location.lat;
                    double longPlace = placesSearchResults[i].geometry.location.lng;
                    String placeName = placesSearchResults[i].name;
                    String openNowCase = "";
                    String photoRef = "";
                    String mMissingPhoto = "%20image%20missing%20reference";

                    if (placesSearchResults[i].permanentlyClosed){
                        i++;
                    } else {
                        if (placesSearchResults[i].openingHours != null && placesSearchResults[i].openingHours.openNow != null) {
                            if (placesSearchResults[i].openingHours.openNow) {
                                openNowCase = "Open now";
                            } else {
                                openNowCase = "Closed now";
                            }
                        } else {
                            openNowCase = "Doesn't show if it's open";
                        }
                        if (placesSearchResults[i].photos != null) {
                            photoRef = placesSearchResults[i].photos[0].photoReference;
                        } else {
                            photoRef = mMissingPhoto;
                        }
                        LatLng resLocation = new LatLng(latPlace , longPlace);
                        RestaurantDetails restaurantDetails = new RestaurantDetails(
                                placesSearchResults[i].placeId,
                                placesSearchResults[i].name,
                                placesSearchResults[i].vicinity,
                                photoRef,
                                openNowCase,
                                placesSearchResults[i].rating,
                                resLocation
                                );
                        SharedPreferences prefs = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
                        String restaurantChoicedId = prefs.getString(PLACE_ID,"");
                        if (Objects.equals(restaurantChoicedId, placeId)) {

                            saveValueOfTheRestaurantChoiceAllDataNeeded(
                                    placesSearchResults[i].name,
                                    placesSearchResults[i].vicinity,
                                    photoRef,
                                    openNowCase,
                                    placesSearchResults[i].rating,
                                    (float) latPlace,
                                    (float) longPlace);
                        } else {
                            deleteValueForPlaceId();
                        }
                        allRestaurants.add(restaurantDetails);

                        MarkerOptions markerOptions = new MarkerOptions().position(
                                new LatLng(latPlace, longPlace)).
                                title(placeName);
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
                                Toast.makeText(getContext(), "Clicked location is " + markerName, Toast.LENGTH_SHORT).show();
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

    private void deleteValueForPlaceId() {
        SharedPreferences shPlaceIdChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        myEdit.putString(PLACE_ID, "");
        myEdit.apply();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_maps, container, false);
//        mapView = (MapView) view.findViewById(R.id.map);
        return inflater.inflate(R.layout.fragment_maps, container, false);
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

    private void saveValueOfTheRestaurantChoiceAllDataNeeded(String nameRes, String addressRes, String photoRefRes, String openNowRes, float ratingRes, float latRes, float lngRes) {
        // Storing data into SharedPreferences
        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = shChoice.edit();
        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(RESTAURANT_NAME, nameRes);
        myEdit.putString(RESTAURANT_ADDRESS, addressRes);
        myEdit.putString(RESTAURANT_PHOTO_REF, photoRefRes);
        myEdit.putString(RESTAURANT_OPEN_NOW, openNowRes);
        myEdit.putFloat(RESTAURANT_RATING, ratingRes);
        myEdit.putFloat(RESTAURANT_LAT, latRes);
        myEdit.putFloat(RESTAURANT_LNG, lngRes);
        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }

}
