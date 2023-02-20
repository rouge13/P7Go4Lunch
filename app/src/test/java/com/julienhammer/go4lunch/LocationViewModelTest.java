package com.julienhammer.go4lunch;

import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.data.permission_check.PermissionCheck;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mock;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class LocationViewModelTest {
    @Mock
    private LocationViewModel mLocationViewModel;
    @Mock
    private PermissionCheck mPermissionCheck;
    @Mock
    private LocationRepository mLocationRepository;

    @Before
    public void setup(){
        mLocationRepository = mock(LocationRepository.class);
        mPermissionCheck = mock(PermissionCheck.class);
        mLocationViewModel = new LocationViewModel(mPermissionCheck, mLocationRepository);
    }

    @Test
    public void testConstructor() {
        assertNotNull(mLocationRepository);
        assertNotNull(mPermissionCheck);
        assertNotNull(mLocationViewModel);
    }

    @Test
    public void testGetLocationLiveData(){
        // Given
        Location expectedUserLocation = new Location("");
        expectedUserLocation.setLatitude(48.5735);
        expectedUserLocation.setLongitude(7.7523);
        MutableLiveData<Location> userLocationMutableLiveData = new MutableLiveData<>();
        userLocationMutableLiveData.postValue(expectedUserLocation);
        given(mLocationRepository.getLocationLiveData()).willReturn(userLocationMutableLiveData);

        // When
        LiveData<Location> actualLocationLiveData = mLocationViewModel.getLocationLiveData();

        // Then
        verify(mLocationRepository).getLocationLiveData();
        Assert.assertSame(userLocationMutableLiveData, actualLocationLiveData);
    }

    @Test
    public void testRefresh(){
        // Case Start
        Boolean appHasPermissionToAccessToLocation = true;
        given(mPermissionCheck.hasLocationPermission()).willReturn(appHasPermissionToAccessToLocation);

        // When
        mLocationViewModel.refresh();

        // Then
        CheckStateOfLocation(appHasPermissionToAccessToLocation);
        Assert.assertTrue(CheckStateOfLocation(appHasPermissionToAccessToLocation));

        // Case Stop
        Boolean appHasNotPermissionToAccessToLocation = false;
        given(mPermissionCheck.hasLocationPermission()).willReturn(appHasNotPermissionToAccessToLocation);

        // When
        mLocationViewModel.refresh();

        // Then
        CheckStateOfLocation(appHasNotPermissionToAccessToLocation);
        Assert.assertFalse(CheckStateOfLocation(appHasNotPermissionToAccessToLocation));
    }

    private boolean CheckStateOfLocation(Boolean appHasPermissionToAccessToLocation) {
        Boolean statePermission;
        if(appHasPermissionToAccessToLocation){
            verify(mLocationRepository).startLocationRequest();
            statePermission = true;
        } else {
            verify(mLocationRepository).stopLocationRequest();
            statePermission = false;
        }
        return statePermission;
    }


}
