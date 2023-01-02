package com.julienhammer.go4lunch.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.ActivityMainNavHeaderBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.notification.NotificationBroadcast;
//import com.julienhammer.go4lunch.notification.NotificationHandler;
import com.julienhammer.go4lunch.ui.restaurant.InfoRestaurantFragment;
import com.julienhammer.go4lunch.ui.restaurantsAutoComplete.RecyclerViewRestaurantsAutoCompleteAdapter;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.*;
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
    private static final int SPEECH_REQUEST_CODE = 0;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawer;
    NavigationView navigationView;
    RecyclerViewRestaurantsAutoCompleteAdapter adapter;
//    RecyclerView recyclerView;
    Toolbar toolbar;
//    Toolbar searchToolbar;
    private static final String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static final String PLACE_ID = "placeId";
    private static final String USER_ID = "userId";
    private static final String RESTAURANT_NAME = "nameRes";
    private static final String RESTAURANT_ADDRESS = "addressRes";
    private static final String RESTAURANT_OPEN_NOW = "openNowRes";
    private static final String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static final String RESTAURANT_RATING = "ratingRes";
    private static final String RESTAURANT_LAT = "latRes";
    private static final String RESTAURANT_LNG = "lngRes";
    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    //    ExecutorService executor = Executors.newSingleThreadExecutor();
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


        // Add to the else if the checkSelfPermission isn't granted for this user
        ActivityCompat.requestPermissions(this,
                new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                },
                PackageManager.PERMISSION_GRANTED
        );

        // Add some directly to a function
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            boolean gpsEnabled = false;
            boolean networkEnabled = false;

            try {
                gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch(Exception ex) {}

            try {
                networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch(Exception ex) {}

            if(!gpsEnabled && !networkEnabled) {
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
                    SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
                    myEdit.putFloat(RESTAURANT_LAT, (float) location.getLatitude());
                    myEdit.putFloat(RESTAURANT_LNG, (float) location.getLongitude());
                    myEdit.apply();

                }
            });
            initBasicToolbar();
            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
                if (placeId != null && !Objects.equals(placeId, "")){
                    saveValueOfTheRestaurantChoicePlaceId(FirebaseAuth.getInstance().getCurrentUser(), placeId);
                    mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseAuth.getInstance().getCurrentUser(), placeId);

                    setNotificationAlarm();
                    Toast.makeText(this, R.string.notificationSet, Toast.LENGTH_SHORT).show();
                }
            });
            if (!Places.isInitialized()){
                Places.initialize(getApplicationContext(), String.valueOf(R.string.google_api_key));
            }
            PlacesClient placesClient = Places.createClient(this);




//            initStatusBar();
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

            binding.searchRestaurantImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    initSearchToolbar();
                    binding.voiceSearchViewBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            displaySpeechRecognizer();
                        }
                    });

                    binding.searchViewBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            initBasicToolbar();
                        }
                    });
//                    binding.restaurantSearchEditText.setVisibility(View.VISIBLE);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
                    binding.restaurantsRecyclerView.setLayoutManager(layoutManager);
                    adapter = new RecyclerViewRestaurantsAutoCompleteAdapter(context);
//                    recyclerView.setVisibility(View.GONE);
                    adapter.setClickListener(this);
                    binding.restaurantsRecyclerView.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
                    binding.restaurantSearchEditText.addTextChangedListener(new TextWatcher() {
                                                                                @Override
                                                                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                                }

                                                                                @Override
                                                                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                                                                }

                                                                                @Override
                                                                                public void afterTextChanged(Editable editable) {
                                                                                    if (!editable.toString().equals("") && editable.length() > 2) {
                                                                                        binding.restaurantsRecyclerView.setVisibility(View.VISIBLE);
                                                                                        adapter.getFilter().filter(editable.toString());
                                                                                        if (binding.restaurantsRecyclerView.getVisibility() == View.GONE) {
                                                                                            binding.restaurantsRecyclerView.setVisibility(View.VISIBLE);
                                                                                        }
                                                                                    } else {
                                                                                        if (binding.restaurantsRecyclerView.getVisibility() == View.VISIBLE) {
                                                                                            binding.restaurantsRecyclerView.setVisibility(View.GONE);
                                                                                            binding.restaurantSearchEditText.getText().clear();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                    );
                }
            });
        }
        EventBus.getDefault().register(this);

    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        else {
            View decorView = getWindow().getDecorView();
            // Show Status Bar.
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    private void initSearchToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.searchConstraint.setVisibility(View.VISIBLE);
        binding.searchRestaurantImage.setVisibility(View.GONE);
    }

    private void initBasicToolbar() {
//        toolbar.setTitle(R.string.hungry);
        binding.restaurantSearchEditText.getText().clear();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(false);
        binding.restaurantsRecyclerView.setVisibility(View.INVISIBLE);
        binding.searchConstraint.setVisibility(View.GONE);
        binding.searchRestaurantImage.setVisibility(View.VISIBLE);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


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
        if (calendar.get(Calendar.HOUR) < 12){
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH + 1));
            calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
            calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 12);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND,0);
            calendar.set(Calendar.MILLISECOND,0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

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

//    private void configureSearchToolbar() {
//        searchToolbar = binding.activityMainSearchToolbar;
////        toolbar = findViewById(R.id.activity_main_toolbar);
//        setSupportActionBar(searchToolbar);
////        ActionBar actionBar = getSupportActionBar();
////        if (actionBar != null){
////            actionBar.setDisplayHomeAsUpEnabled(false);
////            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
////        }
//
//    }

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
//        getMenuInflater().inflate(R.menu.search_autocomplete_menu, menu);
//        MenuItem menuItemSearch = menu.findItem(R.id.search_autocomplete);
//        SearchView searchView = (SearchView) menuItemSearch.getActionView();
//        searchView.setQueryHint("Search restaurants");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//
//                if (s.length() > 2) {
////                    searchAutoCompleteRestaurant();
//                }
//
//
//                return false;
//            }
//        });
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

    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
//            String spokenText = results.get(0);
            binding.restaurantSearchEditText.setText(results.get(0));

            if (!binding.restaurantSearchEditText.getText().toString().equals("") && binding.restaurantSearchEditText.getText().length() > 2) {
                binding.restaurantsRecyclerView.setVisibility(View.VISIBLE);
                adapter.getFilter().filter(binding.restaurantSearchEditText.getText().toString());
                if (binding.restaurantsRecyclerView.getVisibility() == View.GONE) {
                    binding.restaurantsRecyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (binding.restaurantsRecyclerView.getVisibility() == View.VISIBLE) {
                    binding.restaurantsRecyclerView.setVisibility(View.GONE);
                }
            }
            // Do something with spokenText
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}