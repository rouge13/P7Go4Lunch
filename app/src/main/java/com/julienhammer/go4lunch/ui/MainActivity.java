package com.julienhammer.go4lunch.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.julienhammer.go4lunch.LoginActivity;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    ActivityMainBinding binding;
    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView mBottomNavigation;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private final int REQUEST_LOCATION_PERMISSION = 1;

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 6 - Configure all views
//        this.toolbar = ( Toolbar) binding.activityMainToolbar;
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        configureToolBar();
//        configureDrawerLayout();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.activityMainToolbar, R.string.drawer_open, R.string.drawer_close);
        binding.activityMainDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

//        ListInstanceConfigure();
//        setContentView(R.layout.activity_main);


        setContentView(view);
//        MapsInstanceConfigure();
//        binding.buttomNavigationView.setOnItemSelectedListener(item -> {
//                    switch (item.getItemId()) {
//                        case R.id.mapsFragment:
//                            viewPager.setCurrentItem(0);
//                            return true;
//                        case R.id.listFragment:
//                            viewPager.setCurrentItem(1);
//                            return true;
//                        case R.id.workmatesFragment:
//                            viewPager.setCurrentItem(2);
//                            return true;
//                    }
//                    return false;
//                }
//        );

        viewPager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MapsFragment.newInstance())
//                    .commitNow();
//        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_info_menu, menu);
        return true;
    }

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
    public void onBackPressed() {
        // 5 - Handle back click to close menu
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    // 1 - Configure Toolbar
    private void configureToolBar(){
        setSupportActionBar(binding.activityMainToolbar);
    }

    // 2 - Configure Drawer Layout
    private void configureDrawerLayout(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, binding.activityMainDrawerLayout, binding.activityMainToolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

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
                firebaseAuth.signOut();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                break;

            default:
                return super.onOptionsItemSelected(item);

        }
        this.drawerLayout.closeDrawer(GravityCompat.START);
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