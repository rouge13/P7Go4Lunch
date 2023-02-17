package com.julienhammer.go4lunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.PlacesResponse;
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
    private Observer<List<String>> mAllRestaurantChoosedLiveDataObserver;
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

    // Test the constructor
    @Test
    public void testConstructor() {
        assertNotNull(mRestaurantsViewModel);
        assertNotNull(mRestaurantsViewModel.mRestaurantsRepository);
    }

    // All Inits
    @Test
    public void testInitAllRestaurant() {
        // Given
        String apiKey = "test_apiKey";
        Location userLocation = new Location("");
        userLocation.setLatitude(48.5735);
        userLocation.setLongitude(7.7523);
        // When
        mRestaurantsViewModel.initAllRestaurant(apiKey, userLocation);
        // Then
        verify(mRestaurantsRepository).initAllRestaurant(eq(apiKey), eq(userLocation));
    }

    @Test
    public void testInitIsSomeoneEatingThere() {
        // Given
        String restaurantId = "zz11re";
        // When
        mRestaurantsViewModel.initIsSomeoneEatingThere(restaurantId);
        // Then
        verify(mRestaurantsRepository).initIsSomeoneEatingThere(eq(restaurantId));
    }

    @Test
    public void testInitAllSearchFilteredRestaurant() {
        // Given
        CharSequence query = "Na".toLowerCase();
        CharSequence queryModified = "na";

        // When
        mRestaurantsViewModel.initAllSearchFilteredRestaurant(query);
        // Then
        verify(mRestaurantsRepository).initAllSearchFilteredRestaurant(eq(query));
        Assert.assertEquals(query, queryModified);

    }

    @Test
    public void testInitAllRestaurantChoosed() {
        // Given
        List<String> expectedChoosedRestaurants = new ArrayList<>();
        List<String> allrestaurants = new ArrayList<>();
        String restaurant1 = "Napoli";
        String restaurant2 = "Neudorf";
        String restaurant3 = "Nako";
        String restaurant4 = "Raisina";
        allrestaurants.add(restaurant1);
        allrestaurants.add(restaurant2);
        allrestaurants.add(restaurant3);
        allrestaurants.add(restaurant4);
        expectedChoosedRestaurants.add(restaurant2);
        expectedChoosedRestaurants.add(restaurant4);

        // When
        mRestaurantsViewModel.initAllRestaurantChoosed();
        // Then
        verify(mRestaurantsRepository).initAllRestaurantChoosed();
        Assert.assertNotEquals(expectedChoosedRestaurants, allrestaurants);

    }

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
        mRestaurantsViewModel.getNearbyPlaces().removeObserver(mNearbyPlacesObserver);

    }

    @Test
    public void testGetIfEatingHere() {
        // Given
        Boolean expectedEatingHere = Boolean.TRUE;
        MutableLiveData<Boolean> isEatingHereMutableLiveData = new MutableLiveData<>();
        isEatingHereMutableLiveData.postValue(expectedEatingHere);
        given(mRestaurantsRepository.getIfEatingHere()).willReturn(isEatingHereMutableLiveData);
        String restaurantId = "zz11re";
        mRestaurantsViewModel.initIsSomeoneEatingThere(restaurantId);

        // When
        mRestaurantsViewModel.getIfEatingHere().observeForever(mEatingHereLiveDataObserver);

        // Then
        verify(mRestaurantsRepository).getIfEatingHere();
        verify(mEatingHereLiveDataObserver).onChanged(expectedEatingHere);
        Assert.assertEquals(Boolean.TRUE, mRestaurantsViewModel.getIfEatingHere().getValue());
    }

    @Test
    public void testGetAllSearchFilteredRestaurant() {
        // Given
        MutableLiveData<ArrayList<String>> allSearchFilteredRestaurantMutableLiveData = new MutableLiveData<>();
        ArrayList<String> expectedAllSearchFilteredRestaurant = new ArrayList<>();
        ArrayList<String> allRestaurantToFilter = new ArrayList<>();
        ArrayList<String> allRestaurantToFilterWaitedInAssertTrue = new ArrayList<>();
        String restaurant1 = "Napoli";
        String restaurant2 = "Neudorf";
        String restaurant3 = "Nako";
        String restaurant4 = "Raisina";
        allRestaurantToFilter.add(restaurant1);
        allRestaurantToFilter.add(restaurant2);
        allRestaurantToFilter.add(restaurant3);
        allRestaurantToFilter.add(restaurant4);
        allRestaurantToFilterWaitedInAssertTrue.add(restaurant1);
        allRestaurantToFilterWaitedInAssertTrue.add(restaurant3);
        allRestaurantToFilterWaitedInAssertTrue.add(restaurant4);
        CharSequence query = "Na".toLowerCase();
        for (String restaurantToFilter : allRestaurantToFilter) {
            if (restaurantToFilter.toLowerCase().contains(query)) {
                expectedAllSearchFilteredRestaurant.add(restaurantToFilter);
            }
        }
        allSearchFilteredRestaurantMutableLiveData.postValue(expectedAllSearchFilteredRestaurant);
        given(mRestaurantsRepository.getAllSearchFilteredRestaurant()).willReturn(allSearchFilteredRestaurantMutableLiveData);
        mRestaurantsViewModel.initAllSearchFilteredRestaurant(query);

        // When
        mRestaurantsViewModel.getAllSearchFilteredRestaurant().observeForever(mAllSearchFilteredRestaurantObserver);

        // Then
        verify(mRestaurantsRepository).getAllSearchFilteredRestaurant();
        verify(mAllSearchFilteredRestaurantObserver).onChanged(expectedAllSearchFilteredRestaurant);
        boolean haveSameValues = expectedAllSearchFilteredRestaurant.containsAll(allRestaurantToFilterWaitedInAssertTrue)
                && allRestaurantToFilterWaitedInAssertTrue.containsAll(expectedAllSearchFilteredRestaurant);
        Assert.assertTrue(haveSameValues);

    }

    @Test
    public void testGetAllRestaurantChoosed() {
        // Given
        MutableLiveData<List<String>> allChoosedRestaurantsMutableLiveData = new MutableLiveData<>();
        List<String> expectedChoosedRestaurants = new ArrayList<>();
        List<String> allrestaurants = new ArrayList<>();
        String restaurant1 = "Napoli";
        String restaurant2 = "Neudorf";
        String restaurant3 = "Nako";
        String restaurant4 = "Raisina";
        allrestaurants.add(restaurant1);
        allrestaurants.add(restaurant2);
        allrestaurants.add(restaurant3);
        allrestaurants.add(restaurant4);
        expectedChoosedRestaurants.add(restaurant2);
        expectedChoosedRestaurants.add(restaurant4);
        given(mRestaurantsRepository.getAllRestaurantChoosed()).willReturn(allChoosedRestaurantsMutableLiveData);
        allChoosedRestaurantsMutableLiveData.postValue(expectedChoosedRestaurants);

        // When
        mRestaurantsViewModel.initAllRestaurantChoosed();
        mRestaurantsViewModel.getAllRestaurantChoosed().observeForever(mAllRestaurantChoosedLiveDataObserver);

        // Then
        verify(mRestaurantsRepository).initAllRestaurantChoosed();
        verify(mAllRestaurantChoosedLiveDataObserver).onChanged(expectedChoosedRestaurants);
        Assert.assertEquals(expectedChoosedRestaurants, mRestaurantsViewModel.getAllRestaurantChoosed().getValue());

    }




}
