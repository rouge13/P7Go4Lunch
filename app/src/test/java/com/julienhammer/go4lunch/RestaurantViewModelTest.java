package com.julienhammer.go4lunch;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.location.Location;

import com.google.common.base.Verify;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.PlacesResponse;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class RestaurantViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    private RestaurantsRepository mRestaurantsRepository;
    @Mock
    private Observer<PlacesSearchResult[]> mRestaurantsLiveDataObserver;
    @Mock
    private Observer<Boolean> mEatingHereLiveDataObserver;
    @Mock
    private Observer<PlacesResponse.Root> mNearbyPlacesObserver;
    @Mock
    private Observer<ArrayList<String>> mAllSearchFilteredRestaurantObserver;
    @Mock
    private RestaurantsViewModel mRestaurantsViewModel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mRestaurantsViewModel = new RestaurantsViewModel();
        mRestaurantsViewModel.mRestaurantsRepository = mRestaurantsRepository;
    }

    // All Inits
    @Test
    public void testInitAllRestaurant() {
        // Given
        String apiKey = "test-api-key";
        Location userLocation = new Location("");
        userLocation.setLatitude(37.7749);
        userLocation.setLongitude(-122.4194);
        // When
        mRestaurantsViewModel.initAllRestaurant(apiKey, userLocation);
        // Then
        verify(mRestaurantsRepository).initAllRestaurant(eq(apiKey), eq(userLocation));
    }

    @Test
    public void initIsSomeoneEatingThere() {
        // Given
        String restaurantId = "zz11re";
        // When
        mRestaurantsViewModel.initIsSomeoneEatingThere(restaurantId);
        // Then
        verify(mRestaurantsRepository).initIsSomeoneEatingThere(eq(restaurantId));
    }

    @Test
    public void initAllSearchFilteredRestaurant(){
        // Given
        CharSequence query = "Na".toLowerCase();
        // When
        mRestaurantsViewModel.initAllSearchFilteredRestaurant(query);
        // Then
        verify(mRestaurantsRepository).initAllSearchFilteredRestaurant(eq(query));

    }



    // Given
    // When
    // Then



    // All gets
    @Test
    public void testGetRestaurantsLiveData() {
        // Given
        PlacesSearchResult[] expectedRestaurants = new PlacesSearchResult[10];
        MutableLiveData<PlacesSearchResult[]> restaurantsLiveData = new MutableLiveData<>();
        restaurantsLiveData.setValue(expectedRestaurants);
        given(mRestaurantsRepository.getRestaurantsLiveData()).willReturn(restaurantsLiveData);
        String apiKey = "test-api-key";
        Location userLocation = new Location("");
        userLocation.setLatitude(37.7749);
        userLocation.setLongitude(-122.4194);
        mRestaurantsViewModel.initAllRestaurant(apiKey, userLocation);
        // When
        mRestaurantsViewModel.getRestaurantsLiveData().observeForever(mRestaurantsLiveDataObserver);
        // Then
        verify(mRestaurantsRepository).getRestaurantsLiveData();
        verify(mRestaurantsLiveDataObserver).onChanged(expectedRestaurants);
    }

    @Test
    public void testGetNearbyPlaces() {

        // Given
        ArrayList<PlacesResponse.Result> results = new ArrayList<>();
        PlacesResponse.Result result1 = new PlacesResponse.Result();
        result1.name = "Restaurant 1";
        result1.place_id = "zz11re";
        result1.rating = 1;
        result1.formatted_address = "123 grand rue";
        result1.business_status = "OPERATIONAL";

        PlacesResponse.Result result2 = new PlacesResponse.Result();
        result2.name = "Restaurant 2";
        result2.place_id = "rr22tr";
        result2.rating = 4;
        result2.formatted_address = "12 avenue des vosges";
        result2.business_status = "OPERATIONAL";

        results.add(result1);
        results.add(result2);

        PlacesResponse.Root mockRoot = new PlacesResponse.Root("OK", results);

        // When
        MutableLiveData<PlacesResponse.Root> mockLiveData = new MutableLiveData<>();
        mockLiveData.setValue(mockRoot);
        when(mRestaurantsRepository.getNearbyPlaces()).thenReturn(mockLiveData);

        // Then
        mRestaurantsViewModel.getNearbyPlaces().observeForever(mNearbyPlacesObserver);
        verify(mRestaurantsRepository).getNearbyPlaces();
        verify(mNearbyPlacesObserver).onChanged(mockRoot);

    }

    @Test
    public void getIfEatingHere(){
        // Given
        Boolean expectedEatingHere = Boolean.TRUE;
        MutableLiveData<Boolean> isEatingHere = new MutableLiveData<>();
        isEatingHere.postValue(expectedEatingHere);
        given(mRestaurantsRepository.getIfEatingHere()).willReturn(isEatingHere);
        String restaurantId = "zz11re";
        mRestaurantsViewModel.initIsSomeoneEatingThere(restaurantId);

        // When
        mRestaurantsViewModel.getIfEatingHere().observeForever(mEatingHereLiveDataObserver);

        // Then
        verify(mRestaurantsRepository).getIfEatingHere();
        verify(mEatingHereLiveDataObserver).onChanged(expectedEatingHere);
    }

    @Test
    public void getAllSearchFilteredRestaurant(){
        // Given
        MutableLiveData<ArrayList<String>> allSearchFilteredRestaurant = new MutableLiveData<>();
        ArrayList<String> expectedAllSearchFilteredRestaurant = new ArrayList<>();
        ArrayList<String> allRestaurantToFilter = new ArrayList<>();
        String restaurant1 = "Napoli";
        String restaurant2 = "Neudorf";
        String restaurant3 = "Nako";
        String restaurant4 = "Raisina";
        allRestaurantToFilter.add(restaurant1);
        allRestaurantToFilter.add(restaurant2);
        allRestaurantToFilter.add(restaurant3);
        allRestaurantToFilter.add(restaurant4);
        CharSequence query = "Na".toLowerCase();
        for (String restaurantToFilter : allRestaurantToFilter){
            if(restaurantToFilter.toLowerCase().contains(query)){
                expectedAllSearchFilteredRestaurant.add(restaurantToFilter);
            }
        }
        allSearchFilteredRestaurant.postValue(expectedAllSearchFilteredRestaurant);
        given(mRestaurantsRepository.getAllSearchFilteredRestaurant()).willReturn(allSearchFilteredRestaurant);
        mRestaurantsViewModel.initAllSearchFilteredRestaurant(query);

        // When
        mRestaurantsViewModel.getAllSearchFilteredRestaurant().observeForever(mAllSearchFilteredRestaurantObserver);

        // Then
        verify(mRestaurantsRepository).getAllSearchFilteredRestaurant();
        verify(mAllSearchFilteredRestaurantObserver).onChanged(expectedAllSearchFilteredRestaurant);

    }


}
