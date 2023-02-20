package com.julienhammer.go4lunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlusCode;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.data.restaurants.InfoRestaurantRepository;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;

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
import java.util.Objects;


/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
@RunWith(MockitoJUnitRunner.class)
public class InfoRestaurantViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private InfoRestaurantRepository mInfoRestaurantRepository;
    @Mock
    private Observer<Place> mRestaurantDetailsInfoLiveDataObserver;
    @Mock
    private Observer<Bitmap> mRestaurantPhotoBitmapLiveDataObserver;
    @Mock
    private Observer<List<User>> mAllWorkmatesInThisRestaurantLiveDataObserver;
    @Mock
    private Observer<Integer> mCountWorkmatesForRestaurantLiveDataObserver;
    @Mock
    private Observer<Integer> mCasesOfStarsLiveDataObserver;
    @Mock
    private Observer<Integer> mDistanceFromLocationLiveDataObserver;
    @Mock
    private InfoRestaurantViewModel mInfoRestaurantViewModel;
    @Mock
    private Context mockContext;

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
        Place expectedRestaurant = getPlace();
        MutableLiveData<Place> restaurantLiveData = new MutableLiveData<>();
        restaurantLiveData.setValue(expectedRestaurant);
        given(mInfoRestaurantRepository.getRestaurantDetailsInfoLiveData()).willReturn(restaurantLiveData);
        String restaurantId = "zz11re";
        mInfoRestaurantViewModel.initRestaurantsDetailsInfo(restaurantId);

        // When
        mInfoRestaurantViewModel.getRestaurantDetailsInfoLiveData().observeForever(mRestaurantDetailsInfoLiveDataObserver);

        // Then
        verify(mInfoRestaurantRepository).getRestaurantDetailsInfoLiveData();
        verify(mRestaurantDetailsInfoLiveDataObserver).onChanged(expectedRestaurant);
    }



    @Test
    public void testGetRestaurantPhotoBitmap(){
        // Required init LiveData<Bitmap>
        // Given
        Bitmap expectedBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ALPHA_8);
        MutableLiveData<Bitmap> restaurantBitmap = new MutableLiveData<>();
        restaurantBitmap.postValue(expectedBitmap);
        given(mInfoRestaurantRepository.getRestaurantPhotoBitmap()).willReturn(restaurantBitmap);
        String restaurantId = "zz11re";
        mInfoRestaurantViewModel.initRestaurantsDetailsInfo(restaurantId);

        // When
        mInfoRestaurantViewModel.getRestaurantPhotoBitmap().observeForever(mRestaurantPhotoBitmapLiveDataObserver);

        // Then
        verify(mInfoRestaurantRepository).getRestaurantPhotoBitmap();
        verify(mRestaurantPhotoBitmapLiveDataObserver).onChanged(expectedBitmap);
    }

    @Test
    public void testInitPlacesClientInfo(){
        // Required init Context context
        // Given
        Context expectedContext = mockContext;

        // When
        mInfoRestaurantViewModel.initPlacesClientInfo(expectedContext);

        // Then
        Assert.assertNotNull(mInfoRestaurantRepository);
        verify(mInfoRestaurantRepository).initPlacesClientInfo(expectedContext);
    }

    @Test
    public void testInitAllWorkmatesInThisRestaurantMutableLiveData(){
        // Required init String restaurantId
        // Given
        String restaurantId = "zz11re";
        // When
        mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(restaurantId);
        // Then
        verify(mInfoRestaurantRepository).initAllWorkmatesInThisRestaurantMutableLiveData(restaurantId);
    }

    @Test
    public void testGetAllWorkmatesInThisRestaurantLiveData(){
        // Required init LiveData<List<User>>
        // Given
        List<User> usersToAdd = new ArrayList<>();
        List<String> usersRestaurantLikes = new ArrayList<>();
        String userRestaurantLikes1 = "zz11re";
        String userRestaurantLikes2 = "rr22tr";
        String userSelectedToGetAllWorkmatesInThisRestaurant = "rr22tr";
        usersRestaurantLikes.add(userRestaurantLikes1);
        usersRestaurantLikes.add(userRestaurantLikes2);
        User user1 = new User("1", "coco", "coco@gmail.com", userRestaurantLikes1, null, usersRestaurantLikes);
        User user2 = new User("2", "juju", "juju@gmail.com", userRestaurantLikes2, null, usersRestaurantLikes);
        usersToAdd.add(user1);
        usersToAdd.add(user2);
        List<User> expectedWorkmates = new ArrayList<>();
        MutableLiveData<List<User>> workmatesToAddToExpectedWorkmates = new MutableLiveData<>();
        for (User user : usersToAdd){
            if (Objects.equals(user.getUserPlaceId(), userSelectedToGetAllWorkmatesInThisRestaurant)){
                expectedWorkmates.add(user);
            }
        }
        given(mInfoRestaurantRepository.getAllWorkmatesInThisRestaurantLiveData()).willReturn(workmatesToAddToExpectedWorkmates);
        workmatesToAddToExpectedWorkmates.postValue(expectedWorkmates);

        // When
        mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(userSelectedToGetAllWorkmatesInThisRestaurant);
        mInfoRestaurantViewModel.getAllWorkmatesInThisRestaurantLiveData().observeForever(mAllWorkmatesInThisRestaurantLiveDataObserver);

        // Then
        verify(mInfoRestaurantRepository).initAllWorkmatesInThisRestaurantMutableLiveData(userSelectedToGetAllWorkmatesInThisRestaurant);
        verify(mAllWorkmatesInThisRestaurantLiveDataObserver).onChanged(expectedWorkmates);
        Assert.assertEquals(expectedWorkmates, mInfoRestaurantViewModel.getAllWorkmatesInThisRestaurantLiveData().getValue());

    }


    @Test
    public void testGetCountWorkmatesForRestaurant(){
        // Required init String restaurantId
        // return LiveData<Integer>
        // Given
        MutableLiveData<Integer> countWorkmatesMutableLiveData = new MutableLiveData<>();
        List<User> usersToAdd = new ArrayList<>();
        List<String> usersRestaurantLikes = new ArrayList<>();
        String userRestaurantLikes1 = "zz11re";
        String userRestaurantLikes2 = "rr22tr";
        String userSelectedToGetAllWorkmatesInThisRestaurant = "rr22tr";
        usersRestaurantLikes.add(userRestaurantLikes1);
        usersRestaurantLikes.add(userRestaurantLikes2);
        User user1 = new User("1", "coco", "coco@gmail.com", userRestaurantLikes1, null, usersRestaurantLikes);
        User user2 = new User("2", "juju", "juju@gmail.com", userRestaurantLikes2, null, usersRestaurantLikes);
        User user3 = new User("3", "lulu", "lulu@gmail.com", userRestaurantLikes2, null, usersRestaurantLikes);
        User user4 = new User("4", "mimi", "mimi@gmail.com", userRestaurantLikes1, null, usersRestaurantLikes);
        usersToAdd.add(user1);
        usersToAdd.add(user2);
        usersToAdd.add(user3);
        usersToAdd.add(user4);
        List<User> expectedWorkmates = new ArrayList<>();
        int countWorkmateTotal = usersToAdd.size();
        for (User user : usersToAdd){
            if (Objects.equals(user.getUserPlaceId(), userSelectedToGetAllWorkmatesInThisRestaurant)){
                expectedWorkmates.add(user);
            }
        }
        countWorkmatesMutableLiveData.postValue(expectedWorkmates.size());
        given(mInfoRestaurantRepository.countWorkmatesForRestaurant(userSelectedToGetAllWorkmatesInThisRestaurant)).willReturn(countWorkmatesMutableLiveData);


        // When
        mInfoRestaurantViewModel.getCountWorkmatesForRestaurant(userSelectedToGetAllWorkmatesInThisRestaurant).observeForever(mCountWorkmatesForRestaurantLiveDataObserver);

        // Then
        verify(mCountWorkmatesForRestaurantLiveDataObserver).onChanged(countWorkmatesMutableLiveData.getValue());
        Assert.assertEquals(countWorkmatesMutableLiveData.getValue().intValue(), expectedWorkmates.size());
        Assert.assertNotEquals(countWorkmateTotal, expectedWorkmates.size());
        Assert.assertEquals(countWorkmateTotal, usersToAdd.size());

    }

    @Test
    public void testCasesOfStars(){
        // Required init Double rating
        // return LiveData<Integer>
        // Given
        MutableLiveData<Integer> caseOfStarsMutableLiveData = new MutableLiveData<>();
        Double rating = 3.2;
        Integer expectedCaseOfStars = (int) Math.rint(rating * 3 / 5);
        caseOfStarsMutableLiveData.postValue(expectedCaseOfStars);
        given(mInfoRestaurantRepository.casesOfStars(rating)).willReturn(caseOfStarsMutableLiveData);

        // When
        mInfoRestaurantViewModel.casesOfStars(rating).observeForever(mCasesOfStarsLiveDataObserver);

        // Then
        verify(mCasesOfStarsLiveDataObserver).onChanged(caseOfStarsMutableLiveData.getValue());
        Assert.assertEquals(caseOfStarsMutableLiveData.getValue(), expectedCaseOfStars);

    }

    @Test
    public void testDistanceFromLocation() {
        // Required init LatLng location, LatLng restaurantLocation
        // return LiveData<Integer>
        // Given
        MutableLiveData<Integer> distanceFromLocationMutableLiveData = new MutableLiveData<>();
        LatLng userLocation = new LatLng(48.5735, 7.7523);
        LatLng restaurantLocation = new LatLng(48.4535, 7.5523);
        double distance = SphericalUtil.computeDistanceBetween(userLocation, restaurantLocation);
        Integer expectedDistance = (int) Math.round(distance);
        distanceFromLocationMutableLiveData.postValue(expectedDistance);
        given(mInfoRestaurantRepository.distanceFromLocation(userLocation,restaurantLocation)).willReturn(distanceFromLocationMutableLiveData);

        // When
        mInfoRestaurantViewModel.distanceFromLocation(userLocation, restaurantLocation).observeForever(mDistanceFromLocationLiveDataObserver);

        // Then
        verify(mDistanceFromLocationLiveDataObserver).onChanged(distanceFromLocationMutableLiveData.getValue());
        Assert.assertEquals(distanceFromLocationMutableLiveData.getValue(), expectedDistance);
    }
@Nullable
private Place getPlace() {
    Place expectedRestaurant = new Place() {
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {

        }

        @Nullable
        @Override
        public Uri getWebsiteUri() {
            return null;
        }

        @Nullable
        @Override
        public LatLng getLatLng() {
            return null;
        }

        @Nullable
        @Override
        public LatLngBounds getViewport() {
            return null;
        }

        @Nullable
        @Override
        public AddressComponents getAddressComponents() {
            return null;
        }

        @Nullable
        @Override
        public OpeningHours getOpeningHours() {
            return null;
        }

        @NonNull
        @Override
        public BooleanPlaceAttributeValue getCurbsidePickup() {
            return null;
        }

        @NonNull
        @Override
        public BooleanPlaceAttributeValue getDelivery() {
            return null;
        }

        @NonNull
        @Override
        public BooleanPlaceAttributeValue getDineIn() {
            return null;
        }

        @NonNull
        @Override
        public BooleanPlaceAttributeValue getTakeout() {
            return null;
        }

        @Nullable
        @Override
        public BusinessStatus getBusinessStatus() {
            return null;
        }

        @Nullable
        @Override
        public PlusCode getPlusCode() {
            return null;
        }

        @Nullable
        @Override
        public Double getRating() {
            return null;
        }

        @Nullable
        @Override
        public Integer getIconBackgroundColor() {
            return null;
        }

        @Nullable
        @Override
        public Integer getPriceLevel() {
            return null;
        }

        @Nullable
        @Override
        public Integer getUserRatingsTotal() {
            return null;
        }

        @Nullable
        @Override
        public Integer getUtcOffsetMinutes() {
            return null;
        }

        @Nullable
        @Override
        public String getAddress() {
            return null;
        }

        @Nullable
        @Override
        public String getIconUrl() {
            return null;
        }

        @Nullable
        @Override
        public String getId() {
            return null;
        }

        @Nullable
        @Override
        public String getName() {
            return null;
        }

        @Nullable
        @Override
        public String getPhoneNumber() {
            return null;
        }

        @Nullable
        @Override
        public List<String> getAttributions() {
            return null;
        }

        @Nullable
        @Override
        public List<PhotoMetadata> getPhotoMetadatas() {
            return null;
        }

        @Nullable
        @Override
        public List<Type> getTypes() {
            return null;
        }
    };
    return expectedRestaurant;
}

}
