package com.julienhammer.go4lunch;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.databinding.ActivityLoginBinding;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.ui.MainActivity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginActivity extends Activity {
    private static final int RC_SIGN_IN = 123;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            onLoginSuccess();
        } else {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.GoogleBuilder().build(),
                    new AuthUI.IdpConfig.FacebookBuilder().build(),
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.handleResponseAfterSignIn(requestCode, resultCode, data);
    }
//
//    // Show Snack Bar with a message
//    private void showSnackBar( String message){
//        Snackbar.make(binding.loginLayout, message, Snackbar.LENGTH_SHORT).show();
//    }

    // Method that handles response after SignIn Activity close
    private void handleResponseAfterSignIn(int requestCode, int resultCode, Intent data){

        IdpResponse response = IdpResponse.fromResultIntent(data);

        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),getString(R.string.connection_succeed), Toast.LENGTH_SHORT).show();
                onLoginSuccess();

            } else {
                // ERRORS
                if (response == null) {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_authentication_canceled), Toast.LENGTH_SHORT).show();
                } else if (response.getError()!= null) {
                    if(response.getError().getErrorCode() == ErrorCodes.NO_NETWORK){
                        Toast.makeText(getApplicationContext(),getString(R.string.error_no_internet), Toast.LENGTH_SHORT).show();
                    } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                        Toast.makeText(getApplicationContext(),getString(R.string.error_unknown_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    public void onLoginSuccess(){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

}
