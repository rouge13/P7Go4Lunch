package com.julienhammer.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.LoginActivity;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.ActivityMainNavHeaderBinding;
import com.julienhammer.go4lunch.databinding.ActivityUserBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.viewmodel.MainViewModel;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private MainViewModel mainviewModel;
    private FirebaseAuth firebaseAuth;
    ActivityMainBinding binding;

    ActivityMainNavHeaderBinding navHeaderBinding;
    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView mBottomNavigation;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    public ActionBarDrawerToggle toggle;
    public DrawerLayout drawer;
//    ActivityMainNavHeaderBinding navHeaderMainBinding;
    NavigationView navigationView;
    Toolbar toolbar;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        ActivityMainBinding activityMainBinding = null;
//        activityMainBinding = activityMainBinding.setContentView(R.layout.activity_main);

            binding = ActivityMainBinding.inflate(getLayoutInflater());
            View view = binding.getRoot();
            setContentView(view);
            navHeaderBinding = ActivityMainNavHeaderBinding.bind(binding.activityMainNavView.getHeaderView(0));
            mainviewModel = ViewModelProviders.of(this).get(MainViewModel.class);
//        navBinding = ActivityMainNavHeaderBinding.inflate(getLayoutInflater());
            ViewPager viewPager = binding.viewPager;
            ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(mViewPagerAdapter);
            configureToolBar();
            configureDrawerLayout();
            configureNavigationView();



//        Toolbar toolbar = binding.activityMainToolbar;
//        binding.setHandlers(handlers);
//        activityMainBinding.setViewmodel(mainViewmodel);
//        Toolbar toolbar = findViewById(R.id.activity_main_toolbar);
//        binding.activityMainToolbar.setTitle(R.string.hungry);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        drawer = binding.activityMainDrawerLayout;
////        navBinding = ActivityMainNavHeaderBinding.inflate(getLayoutInflater());
//        toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView = binding.activityMainNavView;
//        navigationView.setNavigationItemSelectedListener(this);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




//        User user = mainviewModel.getUserData().getResult();
//        user = mainviewModel.getmMutableLiveData().getValue();
//        navHeaderMainBinding.username.setText(Objects.requireNonNull(mainviewModel.getmMutableLiveData().getValue()).getUserName());
//        navHeaderMainBinding.userEmail.setText(mainviewModel.getmMutableLiveData().getValue().getUserEmail());

        // 6 - Configure all views
//        this.toolbar = ( Toolbar) binding.activityMainToolbar;
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        configureToolBar();
//        configureDrawerLayout();

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.activityMainToolbar, R.string.drawer_open, R.string.drawer_close);
//        binding.activityMainDrawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                binding.activityMainDrawerLayout.openDrawer(GravityCompat.START);
//            }
//        });

//        ViewPager viewPager = binding.viewPager;
//        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(mViewPagerAdapter);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MapsFragment.newInstance())
//                    .commitNow();
//        }
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
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });
        // Display icon in the toolbar
//        getSupportActionBar().setLogo(R.drawable.ic_baseline_menu_24);
//        getSupportActionBar().setDisplayUseLogoEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    // 2 - Configure the Drawer
    private void configureDrawerLayout() {
        drawer = binding.activityMainDrawerLayout;
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
//        {
//            public void onDrawerClosed(View view) {
//                super.onDrawerClosed(view);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//            public void onDrawerOpened(View drawerView) {
//                super.onDrawerOpened(drawerView);
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
//            }
//        };

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
        getMenuInflater().inflate(R.menu.activity_main_info_menu, menu);
        return true;
    }

//    private void getUserData(){
////        mainviewModel.getUserData().addOnSuccessListener(user -> {
//        // Set the data with the user information
////            String username = TextUtils.isEmpty(user.getUserName()) ? getString(R.string.info_no_username_found) : user.getUserName();
//
//
//        navBinding.username.setText(mainviewModel.getUserData().getResult().getUserName());
//        navBinding.userEmail.setText(mainviewModel.getUserData().getResult().getUserEmail());
////            getViewBinding().activityMainNavView. username.setText(username);
////            getViewBinding().userEmail.setText(user.getUserEmail());
//
////        });
//    }

    //    @SuppressLint("NonConstantResourceId")
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.nav_your_lunch:
////                Intent i = new Intent(this, UserActivity.class);
////                startActivity(i);
//                break;
//            case R.id.nav_settings:
//                break;
//            case R.id.nav_logout:
//                firebaseAuth.signOut();
//                Intent i = new Intent(this, LoginActivity.class);
//                startActivity(i);
//                break;
//
//            default:
//                return super.onOptionsItemSelected(item);
//
//        }
//        return false;
//    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

//        if (toggle.onOptionsItemSelected(item)){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
        if (item.getItemId() == android.R.id.home) {
            toggle();

        }
        return super.onOptionsItemSelected(item);
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
//        FirebaseUser mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (mFirebaseUser != null) {
            mainviewModel.getmMutableLiveData().observe(this, user -> {
                if (user.getUserName() != null){
                    navHeaderBinding.username.setText(user.getUserName());
                    navHeaderBinding.userEmail.setText(user.getUserEmail());

                }
            });
//        }
//        else {
////            Intent i = new Intent(this, LoginActivity.class);
//            startActivity(new Intent(this, LoginActivity.class));
//            finish();
//        }

    }
//    private void MapsInstanceConfigure() {
//        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
//        MapsViewModel mapsViewModel =
//                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
//        mapsViewModel.refresh();
//    }

    // ---------------------
    // CONFIGURATION
    // ---------------------

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        // Handle item selection
        switch (id) {
            case R.id.nav_your_lunch:
//                Intent i = new Intent(this, UserActivity.class);
//                startActivity(i);
                break;
            case R.id.nav_settings:
                break;
            case R.id.nav_logout:
                firebaseAuth.getInstance().signOut();
                finish();
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        drawer = binding.activityMainDrawerLayout;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



//    private void ListInstanceConfigure() {
//        ViewModelFactory listViewModelFactory = ViewModelFactory.getInstance();
//        ListViewModel listViewModel =
//                new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);
//        listViewModel.refresh();
//    }
















//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation_menu_view);
//////        NavController mNavController = Navigation.findNavController(this, R.id.fragmentContainerView);
//////
////
////        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
////                .findFragmentById(R.id.fragmentContainerView);
////        NavController navCo = navHostFragment.getNavController();
////        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
////                R.id.mapsFragment, R.id.listFragment, R.id.workmatesFragment)
////                .build();
////        // Set ActionBar to follow linkage
////        NavigationUI.setupActionBarWithNavController(this, navCo, appBarConfiguration);
//////        // Set Nav to follow linkage
//////        NavigationUI.setupWithNavController(mBottomNavigationView, mNavController);
////
////
//////        // Set login status callback
//////        SocketManager.getInstance().addUserStateCallback(this);
////
////
////        // Set Nav to follow linkage
////        NavigationUI.setupWithNavController(mBottomNavigationView, navCo);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.bottom_navigation_menu, menu);
//        return true;
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        switch (item.getItemId()) {
//            case R.id.mapsFragment:
//                // Initialize fragment
//                Fragment mapsFragment = new Fragment();
//
//                // Open fragment
//                getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.mapsFragment,mapsFragment)
//                        .commit();
//                return true;
//            case R.id.listFragment:
//                // Initialize fragment
//                Fragment listFragment = new Fragment();
//
//                // Open fragment
//                getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.listFragment,listFragment)
//                        .commit();
//                return true;
//            case R.id.workmatesFragment:
//                // Initialize fragment
//                Fragment workmatesFragment = new Fragment();
//
//                // Open fragment
//                getSupportFragmentManager()
//                        .beginTransaction().replace(R.id.workmatesFragment,workmatesFragment)
//                        .commit();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}