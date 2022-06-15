package com.julienhammer.go4lunch.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.os.Bundle;


import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123;
    private BottomNavigationView mBottomNavigation;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapsInstanceConfigure();
//        ListInstanceConfigure();
        setContentView(R.layout.activity_main);
        mBottomNavigation = findViewById(R.id.buttom_navigation_view);
        mBottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.mapsFragment:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.listFragment:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.workmatesFragment:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        });

        viewPager = findViewById(R.id.view_pager);
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapter);

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, MapsFragment.newInstance())
//                    .commitNow();
//        }
    }
    private void MapsInstanceConfigure() {
        ViewModelFactory mapsViewModelFactory = ViewModelFactory.getInstance();
        MapsViewModel mapsViewModel =
                new ViewModelProvider(this, mapsViewModelFactory).get(MapsViewModel.class);
        mapsViewModel.refresh();
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