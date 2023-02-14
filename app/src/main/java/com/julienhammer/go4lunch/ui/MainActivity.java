package com.julienhammer.go4lunch.ui;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
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


import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.ActivityMainNavHeaderBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.interfaces.OnInfoRestaurantSelectedListener;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.notification.NotificationBroadcast;
import com.julienhammer.go4lunch.ui.restaurant.InfoRestaurantFragment;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.SharedRestaurantSelectedViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import java.util.*;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnInfoRestaurantSelectedListener {
    private UserViewModel mUserViewModel;
    private LocationViewModel mLocationViewModel;
    RestaurantsViewModel mRestaurantsViewModel;
    InfoRestaurantViewModel mInfoRestaurantViewModel;
    ActivityMainBinding binding;
    ActivityMainNavHeaderBinding navHeaderBinding;
    private static final int RC_SIGN_IN = 123;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 125;
    private static final int SPEECH_REQUEST_CODE = 0;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawer;
    NavigationView navigationView;
    Toolbar toolbar;
    SharedRestaurantSelectedViewModel mSharedRestaurantSelectedViewModel;
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
    private static final String NOTIFICATION_STATE = "resNotificationState";

    private static final int AUTOCOMPLETE_REQUEST_CODE = 1;
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
        requestPermission();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
            checkGpsAndNetworkEnabled(lm);
            Context context = this.getApplicationContext();
            mLocationViewModel.refresh();
            mLocationViewModel.getLocationLiveData().observe(this, location -> {
                if (location != null){
                    mRestaurantsViewModel.initAllRestaurant(getString(R.string.google_map_key),location);
                    saveLocationInSharedPref(location);
                }
            });
            initBasicToolbar();
            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
                if (placeId != null && !Objects.equals(placeId, "")){
                    saveValueOfTheRestaurantChoicePlaceId(FirebaseAuth.getInstance().getCurrentUser(), placeId);
                    setNotificationAlarm();
                }
            });
            initPlacesClient();
            initViewPager();
            initSearchOnClickListener(context);
        } else {
            initPermissionIfMissing(MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }

    private void initPermissionIfMissing(int myPermissionsRequestLocation) {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                myPermissionsRequestLocation);
    }

    private void initPlacesClient() {
        if (!Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_api_key));
        }
        PlacesClient placesClient = Places.createClient(this);
    }

    private void requestPermission() {
        initPermissionIfMissing(PackageManager.PERMISSION_GRANTED);
    }

    private void checkGpsAndNetworkEnabled(LocationManager lm) {
        boolean gpsEnabled = false;
        boolean networkEnabled = false;
        try {
            gpsEnabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}
        try {
            networkEnabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        if(!gpsEnabled && !networkEnabled) {
            initAlertDialogForGPSAndNetworkEnabled();
        }
    }

    private void initSearchOnClickListener(Context context) {
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
                        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
                        initBasicToolbar();
                    }
                });
                initEditTextSearch(context, this);
            }
        });
    }

    private void initAlertDialogForGPSAndNetworkEnabled() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage(R.string.gps_network_not_enabled);
        alertDialog.setPositiveButton(R.string.open_location_settings, (paramDialogInterface, paramInt) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        });
        alertDialog.setNegativeButton(R.string.Cancel,null);
        alertDialog.show();
    }

    private void initAlertDialogForNotifications(Boolean stateOfButton) {
        int buttonColorPositive = (stateOfButton) ? Color.GREEN : Color.RED;
        int buttonColorNegative = (!stateOfButton) ? Color.GREEN : Color.RED;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setMessage(R.string.option_enable_disable_notification);
        builder.setPositiveButton(R.string.enable_notification, (dialog, id) -> {
            // do something here
            saveValueNotificationState(true);
            setNotificationAlarm();
        });
        builder.setNegativeButton(R.string.disable_notification, (dialog, id) -> {
            saveValueNotificationState(false);
            cancelNotification();
        });
        builder.setNeutralButton(R.string.cancel_dialog, (dialog, which) -> {
            Toast.makeText(getApplicationContext(),R.string.Cancel_notification_settings,Toast.LENGTH_LONG).show();
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(dialogInterface -> {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(buttonColorPositive);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(buttonColorNegative);
        });
        dialog.show();
    }

    private void cancelNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, BroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, R.string.notification_unset, Toast.LENGTH_SHORT).show();

    }

    private Boolean getValueNotificationState(){
        SharedPreferences pref = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        return pref.getBoolean(NOTIFICATION_STATE, false);
    }

    private void saveValueNotificationState(Boolean state){
        SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        myEdit.putBoolean(NOTIFICATION_STATE, state);
        myEdit.apply();
    }

    private void saveLocationInSharedPref(Location location) {
        SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        myEdit.putFloat(RESTAURANT_LAT, (float) location.getLatitude());
        myEdit.putFloat(RESTAURANT_LNG, (float) location.getLongitude());
        myEdit.apply();
    }

    private void initViewPager() {
        ViewPager viewPager = binding.viewPager;
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);
        binding.buttomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case android.R.id.home:
                    toggle();
                    return true;
                case R.id.maps_fragment:
                    viewPager.setCurrentItem(0);
                    toolbar.setTitle(getString(R.string.hungry));
                    binding.searchRestaurantImage.setVisibility(View.VISIBLE);
                    initSearchOnClickListener(this);
                    return true;
                case R.id.list_fragment:
                    viewPager.setCurrentItem(1);
                    toolbar.setTitle(getString(R.string.hungry));
                    binding.searchRestaurantImage.setVisibility(View.VISIBLE);
                    initSearchOnClickListener(this);
                    return true;
                case R.id.workmates_fragment:
                    viewPager.setCurrentItem(2);
                    toolbar.setTitle(getString(R.string.available_workmates));
                    binding.searchRestaurantImage.setVisibility(View.INVISIBLE);
                    return true;
                default:
                    return false;
            }
        });
    }

    private void initSearchToolbar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        binding.searchConstraint.setVisibility(View.VISIBLE);
        binding.searchRestaurantImage.setVisibility(View.GONE);
    }

    private void initBasicToolbar() {
        binding.restaurantSearchEditText.getText().clear();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.searchConstraint.setVisibility(View.GONE);
        binding.searchRestaurantImage.setVisibility(View.VISIBLE);
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
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

    private void setNotificationAlarm() {
        saveValueNotificationState(true);
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
        initCalendarForNotification(calendar);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(this, R.string.notification_set, Toast.LENGTH_SHORT).show();
    }

    private void initCalendarForNotification(Calendar calendar) {
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 50);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    private void saveValueOfTheRestaurantChoicePlaceId(FirebaseUser user, String placeId) {
        SharedPreferences shPlaceIdChoice = this.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shPlaceIdChoice.edit();
        myEdit.putString(PLACE_ID, placeId);
        myEdit.putString(USER_ID, user.getUid());
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
        ViewModelFactory sharedRestaurantSelectedViewModelFactory = ViewModelFactory.getInstance();
        mSharedRestaurantSelectedViewModel = new ViewModelProvider(this, sharedRestaurantSelectedViewModelFactory).get(SharedRestaurantSelectedViewModel.class);
    }

    // 1 - Configure the toolbar
    private void configureToolBar() {
        toolbar = binding.activityMainToolbar;
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
        }
    }

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
                    RestaurantDetails restaurantDetailsChoiced = initDetailRestaurant(prefs, resLocation);
                    mSharedRestaurantSelectedViewModel.initSelectedRestaurant(restaurantDetailsChoiced);
                    onInfoRestaurantSelected();
                } else {
                    Toast.makeText(this.getApplicationContext(), R.string.no_restaurant_choiced, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_settings:
                mUserViewModel.getSelectedRestaurantIsChoiced().observe(this, placeId -> {
                    if (placeId != null && !Objects.equals(placeId, "")){
                        initAlertDialogForNotifications(getValueNotificationState());
                    } else {
                        Toast.makeText(this.getApplicationContext(), R.string.no_restaurant_choiced, Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE).edit().clear().apply();
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

    @NonNull
    private RestaurantDetails initDetailRestaurant(SharedPreferences prefs, LatLng resLocation) {
        RestaurantDetails restaurantDetailsChoiced = new RestaurantDetails(
                prefs.getString(PLACE_ID,""),
                prefs.getString(RESTAURANT_NAME,""),
                prefs.getString(RESTAURANT_ADDRESS,""),
                prefs.getString(RESTAURANT_PHOTO_REF,""),
                prefs.getString(RESTAURANT_OPEN_NOW,""),
                prefs.getFloat(RESTAURANT_RATING,0),
                resLocation
        );
        return restaurantDetailsChoiced;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initEditTextSearch(Context context, View.OnClickListener onClick) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        // Show the soft keyboard
        inputMethodManager.showSoftInput(binding.restaurantSearchEditText, InputMethodManager.SHOW_IMPLICIT);
        binding.restaurantSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mRestaurantsViewModel.initAllSearchFilteredRestaurant(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    @Override
    public void onInfoRestaurantSelected() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_layout, InfoRestaurantFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty()) {
                String speechText = matches.get(0);
                binding.restaurantSearchEditText.setText(speechText);
            }
        }
    }
}