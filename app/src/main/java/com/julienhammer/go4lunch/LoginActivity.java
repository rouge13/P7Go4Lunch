package com.julienhammer.go4lunch;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.appcompat.app.AppCompatActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.viewmodel.LoginViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN = 123;
    private LoginViewModel loginViewModel;
//    WorkmateViewModel workmateViewModel = WorkmateViewModel.getInstance();

    // 2 - Configuring ViewModel

    private void configureViewModel() {
        ViewModelFactory loginViewModelFactory = ViewModelFactory.getInstance();
        loginViewModel =
                new ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel.class);

        this.loginViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(LoginViewModel.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        loginViewModel = new ViewModelProviders(this, ViewModelFactory.getInstance()).get(MainViewModel.class);
        // With ViewModelFactory
//        ViewModelFactory loginViewModelFactory = ViewModelFactory.getInstance();
//        loginViewModel = new ViewModelProvider(this, loginViewModelFactory).get(LoginViewModel.class);
        configureViewModel();

//        user.getIdToken(true).isSuccessful();

        // Si erreur de Firebase concernant le token et l'identification redemander de se connecter.
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            onLoginSuccess();
        } else {
            configureLoginActivityInterface();
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

        Boolean userAddedInFirestore;
        if (requestCode == RC_SIGN_IN) {
            // SUCCESS
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),getString(R.string.connection_succeed), Toast.LENGTH_SHORT).show();
                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (!loginViewModel.getUserCaseAdded(FirebaseAuth.getInstance().getCurrentUser())){
                        loginViewModel.createUser(FirebaseAuth.getInstance().getCurrentUser());

                    }
                    onLoginSuccess();
                } else {
                    configureLoginActivityInterface();
                }

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
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (!loginViewModel.getUserCaseAdded(FirebaseAuth.getInstance().getCurrentUser())) {
                loginViewModel.createUser(FirebaseAuth.getInstance().getCurrentUser());
            }
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
//        else {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
//        user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if (user != null){
//            user.reload().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void unused) {
//
//                }
//            });
////        if (FirebaseAuth.getInstance().getCurrentUser() != null){
//
//        }
//        else {
//            configureLoginActivityInterface();
//        }

//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        startActivity(intent);


    }

//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        if (!user.getUid().isEmpty()) {
////            val intent = Intent(this@CurrentActivity, HomeActivity::class.java)
////            startActivity(intent)
////            finish() // Do not forget to finish the current activity; This will help in not letting the user to come back this the CurrentActivity(condition checking activity)
//            onLoginSuccess();
//        } else {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//            finish();
//        }
//    }

    private void configureLoginActivityInterface(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
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
