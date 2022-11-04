package com.julienhammer.go4lunch.ui;

import android.content.SharedPreferences;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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


import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.julienhammer.go4lunch.LoginActivity;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.ActivityMainNavHeaderBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.ui.list.restaurant.InfoRestaurantFragment;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Objects;
//import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
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
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String RESTAURANT_OPEN_NOW = "openNowRes";
    private static String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static String RESTAURANT_RATING = "ratingRes";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";
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

    @SuppressLint("NonConstantResourceId")
    @RequiresPermission(anyOf = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        configureViewModel();
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

            mLocationViewModel.refresh();
            mLocationViewModel.getLocationLiveData().observe(this, location -> {
                if (location != null){
                    mRestaurantsViewModel.getAllRestaurants(getString(R.string.google_map_key),location);
                }
            });
            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
                saveValueOfTheRestaurantChoicePlaceId(placeId);

            });
//            mUserViewModel.userRestaurantSelected(FirebaseAuth.getInstance().getCurrentUser().getUid());
//            mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
//                mRestaurantsViewModel.getRestaurantsLiveData().observe(this, placesSearchResults -> {
//                    String photoRef;
//                    String mMissingPhoto = "%20image%20missing%20reference";
//                    for (int i = 0; i <= placesSearchResults.length - 1 ; i++ ) {
//                        if (Objects.equals(placeId, placesSearchResults[i].placeId)) {
//                            String openNowCase = "";
//                            if (placesSearchResults[i].permanentlyClosed) {
//                                i++;
//                            } else {
//                                if (placesSearchResults[i].openingHours != null && placesSearchResults[i].openingHours.openNow != null) {
//                                    if (placesSearchResults[i].openingHours.openNow) {
//                                        openNowCase = "Open now";
//                                    } else {
//                                        openNowCase = "Closed now";
//                                    }
//                                } else {
//                                    openNowCase = "Doesn't show if it's open";
//                                }
//                                if (placesSearchResults[i].photos != null) {
//                                    photoRef = placesSearchResults[i].photos[0].photoReference;
//                                } else {
//                                    photoRef = mMissingPhoto;
//                                }
//                                LatLng resLocation = new LatLng(placesSearchResults[i].geometry.location.lat, placesSearchResults[i].geometry.location.lng);
//                                RestaurantDetails restaurantDetails = new RestaurantDetails(
//                                        placesSearchResults[i].placeId,
//                                        placesSearchResults[i].name,
//                                        placesSearchResults[i].vicinity,
//                                        photoRef,
//                                        openNowCase,
//                                        placesSearchResults[i].rating,
//                                        resLocation
//                                );
////                                                restaurantChoiced.add(restaurantDetails);
//                                mInfoRestaurantViewModel.setInfoRestaurant(restaurantDetails);
//
////                                // Storing data into SharedPreferences
////                                SharedPreferences shChoice = this.getSharedPreferences("MyRestaurantChoice",MODE_PRIVATE);
////
////                                // Creating an Editor object to edit(write to the file)
////                                SharedPreferences.Editor myEdit = shChoice.edit();
////
////                                // Storing the key and its value as the data fetched from edittext
////                                myEdit.putString("placeId", placeId);
////
////                                // Once the changes have been made,
////                                // we need to commit to apply those changes made,
////                                // otherwise, it will throw an error
////                                myEdit.apply();
//                            }
//
//                        }
//                    }
//
//
//                });
//            });

//            mUserViewModel.allUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser());
//
//            mUserViewModel.getAllTheRestaurantLikes().observe(this, userRestaurantLikes -> {
//                List<String> listUserRestaurantLikes = userRestaurantLikes;
//                for (int i = 0; i <= (listUserRestaurantLikes.size() - 1) ; i++){
//                    String placeId = listUserRestaurantLikes.get(i);
//                }
//            });

            ViewPager viewPager = binding.viewPager;
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mViewPagerAdapter);
            binding.buttomNavigationView.setOnItemSelectedListener(item -> {
                //        // Handle item selection
                switch (item.getItemId()) {
                    case android.R.id.home:
                        toggle();
                        return true;
                    case R.id.mapsFragment:
                        viewPager.setCurrentItem(0);
                        return true;
                    case R.id.listFragment:
                        viewPager.setCurrentItem(1);
                        return true;
                    case R.id.workmatesFragment:
                        viewPager.setCurrentItem(2);
                        return true;
                    default:
                        return false;
                }
            });

        }
        EventBus.getDefault().register(this);

    }

    private void saveValueOfTheRestaurantChoicePlaceId(String placeId) {
        // Storing data into SharedPreferences
        SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(PLACE_ID, placeId);
        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }


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
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);

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
        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
        getMenuInflater().inflate(R.menu.activity_main_info_menu, menu);
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

    // Store the data in the SharedPreference
    // in the onPause() method
    // When the user closes the application
    // onPause() will be called
    // and data will be stored
//    @Override
//    protected void onPause() {
//        super.onPause();
////        mInfoRestaurantViewModel.getInfoRestaurantLiveData().observe(this,restaurantDetails ->
////        {
//            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
//
//            mUserViewModel.getIfSelectedRestaurantIsChoiced().observe(this, placeId -> {
//                // Storing data into SharedPreferences
//                SharedPreferences shChoice = getSharedPreferences("MyRestaurantChoice",MODE_PRIVATE);
//
//                // Creating an Editor object to edit(write to the file)
//                SharedPreferences.Editor myEdit = shChoice.edit();
//
//                // Storing the key and its value as the data fetched from edittext
//                myEdit.putString("placeId", placeId);
//
//                // Once the changes have been made,
//                // we need to commit to apply those changes made,
//                // otherwise, it will throw an error
//                myEdit.apply();
//            });
//
////        });
//
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
//
//        mUserViewModel.getIfSelectedRestaurantIsChoiced().observe(this, placeId -> {
//            // Storing data into SharedPreferences
//            SharedPreferences shChoice = getSharedPreferences("MyRestaurantChoice",MODE_PRIVATE);
//
//            // Creating an Editor object to edit(write to the file)
//            SharedPreferences.Editor myEdit = shChoice.edit();
//
//            // Storing the key and its value as the data fetched from edittext
//            myEdit.putString("placeId", placeId);
//
//            // Once the changes have been made,
//            // we need to commit to apply those changes made,
//            // otherwise, it will throw an error
//            myEdit.apply();
//        });
//
//    }

}