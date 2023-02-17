package com.julienhammer.go4lunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.PlacesResponse;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import com.google.firebase.testing.FirebaseAppRule;


/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class InfoRestaurantViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private InfoRestaurantRepository mInfoRestaurantRepository;
    private InfoRestaurantViewModel mInfoRestaurantViewModel;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mInfoRestaurantRepository = mock(InfoRestaurantRepository.class);
        mInfoRestaurantViewModel = new InfoRestaurantViewModel();
        mInfoRestaurantViewModel.mInfoRestaurantRepository = mInfoRestaurantRepository;


    }

    // Test the constructor
    @Test
    public void testConstructor() {
        assertNotNull(mInfoRestaurantViewModel);
        assertNotNull(mInfoRestaurantViewModel.mInfoRestaurantRepository);
    }

    @Test
    public void testInitRestaurantsDetailsInfo(){
        // Required init String restaurantId
        // Given
        String restaurantId = "zz11re";
        // When
        mInfoRestaurantViewModel.initRestaurantsDetailsInfo(restaurantId);
        // Then
        Assert.assertNotNull(mInfoRestaurantRepository);
        verify(mInfoRestaurantRepository).initRestaurantsDetailsInfo(eq(restaurantId));

    }

    @Test
    public void testGetRestaurantDetailsInfoLiveData(){
        // Required init LiveData<Place>
        // Given

        // When
        // Then
        mInfoRestaurantRepository.getRestaurantDetailsInfoLiveData();
    }

//    @Test
//    public void testGetRestaurantPhotoBitmap(){
//        // Required init LiveData<Bitmap>
//        // Given
//        // When
//        // Then
//        mInfoRestaurantRepository.getRestaurantPhotoBitmap();
//    }

//    @Test
//    public void testInitPlacesClientInfo(){
//        // Required init Context context
//        // Given
//        // When
//        // Then
////        mInfoRestaurantRepository.initPlacesDetailsClientInfo(context);
//    }

//    @Test
//    public void testInitAllWorkmatesInThisRestaurantMutableLiveData(){
//        // Required init String restaurantId
//        // Given
//        // When
//        // Then
////        mInfoRestaurantRepository.initAllWorkmatesInThisRestaurantMutableLiveData(restaurantId);
//    }

//    @Test
//    public void testGetAllWorkmatesInThisRestaurantLiveData(){
//        // Required init LiveData<List<User>>
//        // Given
//        // When
//        // Then
//        mInfoRestaurantRepository.getAllWorkmatesInThisRestaurantLiveData();
//    }

//    @Test
//    public void testGetCountWorkmatesForRestaurant(String placeId){
//        // Required init String restaurantId
//        // return LiveData<Integer>
//        // Given
//        // When
//        // Then
//        mInfoRestaurantRepository.countWorkmatesForRestaurant(placeId);
//    }

//    @Test
//    public void testCasesOfStars(){
//        // Required init Double rating
//        // return LiveData<Integer>
//        // Given
//        // When
//        // Then
////        mInfoRestaurantRepository.casesOfStars(rating);
//    }

//    @Test
//    public void testDistanceFromLocation() {
//        // Required init LatLng location, LatLng restaurantLocation
//        // return LiveData<Integer>
//        // Given
//        // When
//        // Then
////        mInfoRestaurantRepository.distanceFromLocation(location, restaurantLocation);
//    }






}
