package com.julienhammer.go4lunch;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.data.login.LoginRepository;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;



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
