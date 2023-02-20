package com.julienhammer.go4lunch;


import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.location.Location;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.data.restaurants.RestaurantsRepository;
import com.julienhammer.go4lunch.models.PlacesResponse;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;
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
@RunWith(AndroidJUnit4.class)
public class LoginViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Mock
    private LoginViewModel mLoginViewModel;
    @Mock
    private LoginRepository mLoginRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        Context context = ApplicationProvider.getApplicationContext();
        FirebaseApp.initializeApp(context);
        mLoginRepository = mock(LoginRepository.class);
        mLoginViewModel = new LoginViewModel(mLoginRepository);
    }

    @Test
    public void constructor(){
        assertNotNull(mLoginRepository);
        assertNotNull(mLoginViewModel);
    }

    @Test
    public void createUserOrNot() {
        // Given
        FirebaseUser firebaseUser = mock(FirebaseUser.class);
        when(firebaseUser.getUid()).thenReturn("user1");
        when(firebaseUser.getDisplayName()).thenReturn("userName");
        when(firebaseUser.getEmail()).thenReturn("user1@gmail.com");
        when(firebaseUser.getPhotoUrl()).thenReturn(null);

        // When
        mLoginViewModel.createUserOrNot(firebaseUser);

        // Then
        verify(mLoginRepository).createUserOrNot(firebaseUser);

    }



}
