package com.julienhammer.go4lunch.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Parcel;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.ActivityMainNavHeaderBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.notification.NotificationBroadcast;
//import com.julienhammer.go4lunch.notification.NotificationHandler;
import com.julienhammer.go4lunch.ui.restaurant.InfoRestaurantFragment;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static androidx.constraintlayout.widget.Constraints.TAG;
//import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String MY_SEARCH_ON_COMPLETE = "searchRestaurant";
    private UserViewModel mUserViewModel;
    private LocationViewModel mLocationViewModel;
    RestaurantsViewModel mRestaurantsViewModel;
    InfoRestaurantViewModel mInfoRestaurantViewModel;
    private FirebaseAuth firebaseAuth;
    ActivityMainBinding binding;
    ActivityMainNavHeaderBinding navHeaderBinding;
    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView mBottomNavigation;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    LocationManager lm;
    boolean gps_enabled = false;
    boolean network_enabled = false;
    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String PLACE_ID = "placeId";
    private static String USER_ID = "userId";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String RESTAURANT_OPEN_NOW = "openNowRes";
    private static String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static String RESTAURANT_RATING = "ratingRes";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    //    ExecutorService executor = Executors.newSingleThreadExecutor();
    RestaurantDetails restaurantChoiced;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    @RequiresApi(api = Build.VERSION_CODES.S)
    @SuppressLint("NonConstantResourceId")
    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureViewModel();
        createNotificationChannel();
        mInfoRestaurantViewModel.initPlacesClientInfo(this);
        navHeaderBinding = ActivityMainNavHeaderBinding.bind(binding.activityMainNavView.getHeaderView(0));
        configureToolBar();
        configureDrawerLayout();
        configureNavigationView();
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PackageManager.PERMISSION_GRANTED
        );
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gps_enabled && !network_enabled) {
                // notify user
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage(R.string.gps_network_not_enabled);
                alertDialog.setPositiveButton(R.string.open_location_settings, (paramDialogInterface, paramInt) -> {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                });
                alertDialog.setNegativeButton(R.string.Cancel,null);
                alertDialog.show();
            }
            Context context = this.getApplicationContext();


            mLocationViewModel.refresh();
            mLocationViewModel.getLocationLiveData().observe(this, location -> {
                if (location != null){
                    mRestaurantsViewModel.getAllRestaurants(getString(R.string.google_map_key),location);
//                    double distanceFromCenterToCorner = 5000 * Math.sqrt(2.0);
//
//                    LatLng southwestCorner = SphericalUtil.computeOffset(new LatLng(location.getLatitude(), location.getLongitude()), distanceFromCenterToCorner, 225.0);
//                    LatLng northeastCorner = SphericalUtil.computeOffset(new LatLng(location.getLatitude(), location.getLongitude()), distanceFromCenterToCorner, 45.0);
//                    RectangularBounds boundUserLocation = new RectangularBounds() {
//                        @Override
//                        public int describeContents() {
//                            return 0;
//                        }
//
//                        @Override
//                        public void writeToParcel(Parcel parcel, int i) {
//
//                        }
//
//                        @NonNull
//                        @NotNull
//                        @Override
//                        public LatLng getNortheast() {
//                            return northeastCorner;
//                        }
//
//                        @NonNull
//                        @NotNull
//                        @Override
//                        public LatLng getSouthwest() {
//                            return southwestCorner;
//                        }
//                    };
//                    initAutoCompleteSupportFragment(boundUserLocation);

                }
            });

            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
                if (placeId != null && !Objects.equals(placeId, "")){
                    saveValueOfTheRestaurantChoicePlaceId(FirebaseAuth.getInstance().getCurrentUser(), placeId);
                    mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseAuth.getInstance().getCurrentUser(), placeId);

                    setNotificationAlarm();
                    Toast.makeText(this, "Notification has been set", Toast.LENGTH_SHORT).show();
                }
            });
            if (!Places.isInitialized()){
                Places.initialize(getApplicationContext(), String.valueOf(R.string.google_api_key));
            }
            PlacesClient placesClient = Places.createClient(this);


            ViewPager viewPager = binding.viewPager;
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mViewPagerAdapter);
            binding.buttomNavigationView.setOnItemSelectedListener(item -> {
                //        // Handle item selection
                switch (item.getItemId()) {
                    case android.R.id.home:
                        toggle();
                        return true;
                    case R.id.maps_fragment:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.list_fragment:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.workmates_fragment:
                        viewPager.setCurrentItem(2);
                        return true;
                    default:
                        return false;
                }
            });

        }
        EventBus.getDefault().register(this);

    }

//    private void initAutoCompleteSupportFragment(RectangularBounds boundUserLocation) {
//        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//        if (autocompleteFragment != null){
//            autocompleteFragment.setTypeFilter(TypeFilter.ESTABLISHMENT);
//            autocompleteFragment.setLocationRestriction(RectangularBounds.newInstance(boundUserLocation.getSouthwest(), boundUserLocation.getNortheast()));
//            autocompleteFragment.setCountries("FR");
//
//            // Specify the types of place data to return.
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//            // Set up a PlaceSelectionListener to handle the response.
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(@NonNull Place place) {
//                    // TODO: Get info about the selected place.
//                    Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
//                }
//
//
//                @Override
//                public void onError(@NonNull Status status) {
//                    // TODO: Handle the error.
//                    Log.i(TAG, "An error occurred: " + status);
//                }
//            });
//        }
//
//    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.S)
    private void setNotificationAlarm() {
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.SCHEDULE_EXACT_ALARM) == PackageManager.PERMISSION_GRANTED){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationBroadcast.class);


        PendingIntent pendingIntent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        }

        // Set the alarm to start at 12:00 a.m.
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
//        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
//        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        calendar.set(Calendar.HOUR_OF_DAY, 15);
        calendar.set(Calendar.MINUTE, 38);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        // With setInexactRepeating(), you have to use one of the AlarmManager interval
// constants--in this case, AlarmManager.INTERVAL_DAY.
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

//        }
//        else {
//            Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
//            startActivity(intent);
//        }


    }

    //    private void createRestaurantNotificationChannel(){
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            CharSequence name = "RestaurantReminderChannel";
//            String description = "Channel for Go4Lunch application";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            NotificationChannel channel = new NotificationChannel("notifyRestaurant", name, importance);
//            channel.setDescription(description);
//
//            NotificationManager notificationManager = getSystemService(NotificationManager.class);
//            notificationManager.createNotificationChannel(channel);
//        }
//    }

    private void saveValueOfTheRestaurantChoicePlaceId(FirebaseUser user, String placeId) {
        // Storing data into SharedPreferences
        SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(PLACE_ID, placeId);
        myEdit.putString(USER_ID, user.getUid());
        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }

//    private void addWorkmatesToSharedPreferences(Context context, List<User> workmates) {
//        // Storing data into SharedPreferences
//        SharedPreferences shChoice = context.getSharedPreferences(context.getString(R.string.shared_workmates), MODE_PRIVATE);
//
//        // Creating an Editor object to edit(write to the file)
//        SharedPreferences.Editor myEdit = shChoice.edit();
//
//        ArrayList<String> workmatesUserNameAdded = new ArrayList<>();
//        // Storing the key and its value as the data fetched from edittext
//        for (int i = 0; i < workmates.size(); i++){
//            workmatesUserNameAdded.add(workmates.get(i).getUserName());
//        }
//
//        // create an object of StringBuilder class
//        StringBuilder builder = new StringBuilder();
////        if (workmatesUserNameAdded.iterator().hasNext()) {
//        for (String workmatesUserName : workmatesUserNameAdded) {
//
//            builder.append(workmatesUserName + " ");
//        }
//
////        }
////        else if (!workmatesUserNameAdded.iterator().hasNext()){
////            for (String workmatesUserName : workmatesUserNameAdded) {
////                builder.append(workmatesUserName);
////            }
////        }
//        // convert StringBuilder object into string
//        String stringAllWorkmates = builder.toString();
//        myEdit.putString(context.getString(R.string.shared_workmates), stringAllWorkmates);
//        myEdit.apply();
//    }


    private void configureViewModel() {
        ViewModelFactory mainViewModelFactory = ViewModelFactory.getInstance();
        mUserViewModel = new ViewModelProvider(this, mainViewModelFactory).get(UserViewModel.class);
        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
        mLocationViewModel = new ViewModelProvider(this, mapsViewModelFactory).get(LocationViewModel.class);
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel = new ViewModelProvider(this, restaurantsViewModelFactory).get(RestaurantsViewModel.class);
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel = new ViewModelProvider(this, infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);


    }

    // 1 - Configure the toolbar
    private void configureToolBar() {
        toolbar = binding.activityMainToolbar;
//        toolbar = findViewById(R.id.activity_main_toolbar);
        toolbar.setTitle(R.string.hungry);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }

    }

    // 2 - Configure the Drawer
    private void configureDrawerLayout() {
        drawer = binding.activityMainDrawerLayout;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    // 3 - Configure the navigationView
    private void configureNavigationView() {
        navigationView = binding.activityMainNavView;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        toggle.syncState();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

//        getMenuInflater().inflate(R.menu.activity_main_info_menu, menu);
        return true;
    }

    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        drawer = binding.activityMainDrawerLayout;
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mUserViewModel.getmUserData().observe(this, user -> {
            if (user.getUserName() != null) {
                navHeaderBinding.username.setText(user.getUserName());
                navHeaderBinding.userEmail.setText(user.getUserEmail());

            }
        });
    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.nav_your_lunch:
                SharedPreferences prefs = getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
                String restaurantChoicedId = prefs.getString(PLACE_ID,"");
                if (!Objects.equals(restaurantChoicedId, "")){
                    LatLng resLocation = new LatLng(prefs.getFloat(RESTAURANT_LAT,0) , prefs.getFloat(RESTAURANT_LNG,0));
                    RestaurantDetails restaurantDetailsChoiced = new RestaurantDetails(
                            prefs.getString(PLACE_ID,""),
                            prefs.getString(RESTAURANT_NAME,""),
                            prefs.getString(RESTAURANT_ADDRESS,""),
                            prefs.getString(RESTAURANT_PHOTO_REF,""),
                            prefs.getString(RESTAURANT_OPEN_NOW,""),
                            prefs.getFloat(RESTAURANT_RATING,0),
                            resLocation
                    );
                    mInfoRestaurantViewModel.setInfoRestaurant(restaurantDetailsChoiced);
                    EventBus.getDefault().post(new ShowInfoRestaurantDetailEvent(restaurantDetailsChoiced));
                } else {
                    Toast.makeText(this.getApplicationContext(), R.string.no_restaurant_choiced, Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.nav_settings:
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        drawer = binding.activityMainDrawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * Fired if the user clicks on a restaurant
     * @param event
     */
    @Subscribe
    public void showInfoRestaurantDetailEvent (ShowInfoRestaurantDetailEvent event) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, InfoRestaurantFragment.newInstance()).addToBackStack(null).commit();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                // Storing data into SharedPreferences
                SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_SEARCH_ON_COMPLETE,MODE_PRIVATE);
                SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
                if (!Objects.equals(place.getId(), "")){
                    myEdit.putString(PLACE_ID, place.getId());
                    myEdit.putString(RESTAURANT_NAME, place.getName());
                    if ( place.getPhotoMetadatas() != null && place.getPhotoMetadatas().size() > 0){
                        myEdit.putString(RESTAURANT_PHOTO_REF, place.getPhotoMetadatas().get(0).toString());
                    } else {
                        myEdit.putString(RESTAURANT_PHOTO_REF, "AW30NDyTtr4RxhXVXvp0Ls4NWt8l0VZG-zUl7n0wpOqMgfW9iWGaA6o55RE5AMkIlSRpTFlsaohDbXYiLNmik6xHPKkFJau2SH0TCLzEGS9Zobrx05SsqA_dxh5dJfKG55PU3UWS5jyzPo5KFarCFkasji1g8q6NReKuT9M2DjyWLy3fKwZo");
                    }
                    myEdit.apply();

                    showSearchResultInDetails(place);
                }


            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showSearchResultInDetails(Place place) {
        String placeId = place.getId();
        String placeName = place.getName();
        String placeAddress = "";
        if (place.getAddress() != null){
            placeAddress = place.getAddress();
        } else {
            placeAddress = "";
        }
        String placeOpenNow = "";
        String placePhotoRef = "";
        float placeRating = 0;
        LatLng placeLatLng = null;
        if (place.getOpeningHours() != null && place.getOpeningHours().getPeriods() != null){
            placeOpenNow = place.getOpeningHours().getPeriods().toString();
        }
        else {
            placeOpenNow = "";
        }
        if ( place.getPhotoMetadatas() != null && place.getPhotoMetadatas().size() > 0){
            placePhotoRef = place.getPhotoMetadatas().get(0).zza();
        } else {
            placePhotoRef = "AW30NDyTtr4RxhXVXvp0Ls4NWt8l0VZG-zUl7n0wpOqMgfW9iWGaA6o55RE5AMkIlSRpTFlsaohDbXYiLNmik6xHPKkFJau2SH0TCLzEGS9Zobrx05SsqA_dxh5dJfKG55PU3UWS5jyzPo5KFarCFkasji1g8q6NReKuT9M2DjyWLy3fKwZo";
        }
        if (place.getRating() != null){
            placeRating = place.getRating().floatValue();
        } else {
            placeRating = 0;
        }
        if (place.getLatLng() != null) {
            placeLatLng = new LatLng(place.getLatLng().latitude, place.getLatLng().longitude);
        }

        RestaurantDetails restaurantDetails = new RestaurantDetails(
                placeId,
                placeName,
                placeAddress,
                placePhotoRef,
                placeOpenNow,
                placeRating,
                placeLatLng
        );

        mInfoRestaurantViewModel.setInfoRestaurant(restaurantDetails);
        Toast.makeText(getApplicationContext(), "Clicked location is " + placeName, Toast.LENGTH_SHORT).show();
        EventBus.getDefault().post(new ShowInfoRestaurantDetailEvent(restaurantDetails));
    }

    public void searchAutoCompleteRestaurant(View view) {

        mLocationViewModel.getLocationLiveData().observe(this, location -> {
            if (location != null){
                double distanceFromCenterToCorner = 5000 * Math.sqrt(2.0);

                LatLng southwestCorner = SphericalUtil.computeOffset(new LatLng(location.getLatitude(), location.getLongitude()), distanceFromCenterToCorner, 225.0);
                LatLng northeastCorner = SphericalUtil.computeOffset(new LatLng(location.getLatitude(), location.getLongitude()), distanceFromCenterToCorner, 45.0);
                RectangularBounds boundUserLocation = new RectangularBounds() {
                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel parcel, int i) {

                    }

                    @NonNull
                    @NotNull
                    @Override
                    public LatLng getNortheast() {
                        return northeastCorner;
                    }

                    @NonNull
                    @NotNull
                    @Override
                    public LatLng getSouthwest() {
                        return southwestCorner;
                    }
                };

                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.OVERLAY,
                        Arrays.asList(
                                Place.Field.ID,
                                Place.Field.NAME,
                                Place.Field.PHOTO_METADATAS,
                                Place.Field.LAT_LNG)
                )
                        .setTypeFilter(TypeFilter.ESTABLISHMENT)
                        .setLocationRestriction(RectangularBounds.newInstance(boundUserLocation.getSouthwest(), boundUserLocation.getNortheast()))
                        .setCountries(Collections.singletonList("FR"))
                        .build(this);

                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);



            }
        });


    }
}