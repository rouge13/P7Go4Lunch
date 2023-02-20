package com.julienhammer.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.viewmodel.SharedRestaurantSelectedViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class SharedRestaurantSelectedViewModelTest {
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Mock
    SharedRestaurantSelectedViewModel mSharedRestaurantSelectedViewModel;



    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        mSharedRestaurantSelectedViewModel = new SharedRestaurantSelectedViewModel();

    }

    @Test
    public void testInitSelectedRestaurant(){
        // Given
        RestaurantDetails restaurantDetails = mock(RestaurantDetails.class);

        // When
        mSharedRestaurantSelectedViewModel.initSelectedRestaurant(restaurantDetails);

        // Then
        assertEquals(mSharedRestaurantSelectedViewModel.getSelectedRestaurant().getValue(), restaurantDetails);

    }

    @Test
    public void testGetSelectedRestaurant(){
        // Before the initialization of the value
        assertNull(mSharedRestaurantSelectedViewModel.getSelectedRestaurant().getValue());

        // Given
        RestaurantDetails restaurantDetails = mock(RestaurantDetails.class);

        // When
        mSharedRestaurantSelectedViewModel.initSelectedRestaurant(restaurantDetails);

        // Then
        assertEquals(mSharedRestaurantSelectedViewModel.getSelectedRestaurant().getValue(), restaurantDetails);

    }
}
