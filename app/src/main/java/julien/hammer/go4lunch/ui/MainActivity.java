package julien.hammer.go4lunch.ui;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.gms.maps.MapFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import julien.hammer.go4lunch.ListFragment;
import julien.hammer.go4lunch.R;
import julien.hammer.go4lunch.WorkmatesFragment;
import julien.hammer.go4lunch.ui.map.MapsFragment;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView mBottomNavigation;
    private ViewPager viewPager;
    private ViewPagerAdapter mViewPagerAdapter;
    private final int REQUEST_LOCATION_PERMISSION = 1;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        loadFragment(new MapsFragment());
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
        requestLocationPermission();
    }





//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        // Forward results to EasyPermissions
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    @AfterPermissionGranted(REQUEST_LOCATION_PERMISSION)
//    public void requestLocationPermission() {
//        String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION};
//        if(EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
//        }
//        else {
//            EasyPermissions.requestPermissions(this, "Please grant the location permission", REQUEST_LOCATION_PERMISSION, perms);
//        }
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