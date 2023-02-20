package com.julienhammer.go4lunch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.data.user.UserRepository;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */


@RunWith(AndroidJUnit4.class)
public class UserViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private UserRepository mUserRepository;
    @Mock
    private UserViewModel mUserViewModel;
    @Mock
    private Observer<String> mPlaceIdObserver;
    @Mock
    private Observer<User> mUserObserver;
    @Mock
    private Observer<Boolean> mIfRestaurantIsLikedObserver;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(context);
        mUserViewModel = new UserViewModel();
        mUserViewModel.mUserRepository = mUserRepository;
    }

    @Test
    public void constructor(){
        assertNotNull(mUserRepository);
        assertNotNull(mUserViewModel);
    }

    @Test
    public void testGetUserData() {
        // Given
        MutableLiveData<User> mUserData = new MutableLiveData<>();
        ArrayList<String> listOfRestaurantsLikes = new ArrayList<>();
        listOfRestaurantsLikes.add("rr22re");
        listOfRestaurantsLikes.add("zz33te");
        User expectedUser = new User("123","user1","user1@gmail.com","zz11re",null,listOfRestaurantsLikes);
        mUserData.setValue(expectedUser);
        given(mUserRepository.getUserData()).willReturn(mUserData);

        // When
        mUserViewModel.getUserData().observeForever(mUserObserver);

        // Then
        verify(mUserRepository).getUserData();
        verify(mUserObserver).onChanged(expectedUser);
        assertEquals(expectedUser, mUserRepository.getUserData().getValue());
    }

    @Test
    public void testUserRestaurantSelected(){
        // Given
        String userUID = "user1";

        // When
        mUserViewModel.userRestaurantSelected(userUID);

        // Then
        verify(mUserRepository).userRestaurantSelected(userUID);
    }

    @Test
    public void testSetUserRestaurantChoice(){
        // Given
        String userUID = "user1";
        String placeId = "zz11re";

        // When
        mUserViewModel.setUserRestaurantChoice(userUID, placeId);

        // Then
        verify(mUserRepository).setUserRestaurantChoice(userUID, placeId);
    }

    @Test
    public void getSelectedRestaurantIsChoosed(){
        // Given
        MutableLiveData<String> mPlaceIdMutableLiveData = new MutableLiveData<>();
        String expectedPlaceId = "zz11re";
        mPlaceIdMutableLiveData.setValue(expectedPlaceId);
        given(mUserRepository.getSelectedRestaurantIsChoosed()).willReturn(mPlaceIdMutableLiveData);

        // When
        mUserViewModel.getSelectedRestaurantIsChoosed().observeForever(mPlaceIdObserver);

        // Then
        verify(mUserRepository).getSelectedRestaurantIsChoosed();
        verify(mPlaceIdObserver).onChanged(expectedPlaceId);
        assertEquals(expectedPlaceId, mUserRepository.getSelectedRestaurantIsChoosed().getValue());
    }

    @Test
    public void testSetUserRestaurantLikes(){
        // Given
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getUid()).thenReturn("user1");
        when(firebaseUser.getDisplayName()).thenReturn("userName");
        when(firebaseUser.getEmail()).thenReturn("user1@gmail.com");
        when(firebaseUser.getPhotoUrl()).thenReturn(null);
        String placeId = "zz22re";

        // When
        mUserViewModel.setUserRestaurantLikes(firebaseUser, placeId);

        // THen
        verify(mUserRepository).setUserRestaurantLikes(firebaseUser, placeId);
    }

    @Test
    public void testThisRestaurantIsLiked(){
        // Given
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getUid()).thenReturn("user1");
        when(firebaseUser.getDisplayName()).thenReturn("userName");
        when(firebaseUser.getEmail()).thenReturn("user1@gmail.com");
        when(firebaseUser.getPhotoUrl()).thenReturn(null);
        String placeId = "zz22re";

        // When
        mUserViewModel.thisRestaurantIsLiked(firebaseUser, placeId);

        // THen
        verify(mUserRepository).thisRestaurantIsLiked(firebaseUser, placeId);
    }

    @Test
    public void testGetIfRestaurantIsLiked(){
        // Given
        MutableLiveData<Boolean> mIfRestaurantIsLikedMutableLiveData = new MutableLiveData<>();
        Boolean expectedIsLiked = false;
        mIfRestaurantIsLikedMutableLiveData.setValue(expectedIsLiked);
        given(mUserRepository.getRestaurantIfLiked()).willReturn(mIfRestaurantIsLikedMutableLiveData);

        // When
        mUserViewModel.getIfRestaurantIsLiked().observeForever(mIfRestaurantIsLikedObserver);

        // Then
        verify(mUserRepository).getRestaurantIfLiked();
        verify(mIfRestaurantIsLikedObserver).onChanged(expectedIsLiked);
        assertEquals(expectedIsLiked, mUserRepository.getRestaurantIfLiked().getValue());
    }

}
