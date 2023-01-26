package com.julienhammer.go4lunch.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private LoginViewModel loginViewModel;
    private static final String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static final String PLACE_ID = "placeId";
    private static final String USER_ID = "userId";

    private void configureViewModel() {
        ViewModelFactory loginViewModelFactory = ViewModelFactory.getInstance();
        loginViewModel =
                new ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        configureViewModel();
        // Si erreur de Firebase concernant le token et l'identification redemander de se connecter.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
//            loginViewModel.isUserAddedInFirebase(FirebaseAuth.getInstance().getCurrentUser().getUid());
            onLoginSuccess(FirebaseAuth.getInstance().getCurrentUser());
        } else {
            configureLoginActivityInterface();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data) {

        IdpResponse response = IdpResponse.fromResultIntent(data);
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_succeed), Toast.LENGTH_SHORT).show();
                onLoginSuccess(FirebaseAuth.getInstance().getCurrentUser());
            } else {
                configureLoginActivityInterface();
            }

        } else {
            // ERRORS
            if (response == null) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
            } else if (response.getError() != null) {
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void onLoginSuccess(FirebaseUser userInfo) {
        if (userInfo != null) {
            loginViewModel.createUserOrNot(FirebaseAuth.getInstance().getCurrentUser());
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void configureLoginActivityInterface() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.TwitterBuilder().build()
        );

        // Launch the activity with theme and logo and the providers
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.logo9)
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false, true)
                        .build(),
                RC_SIGN_IN);
    }

}
