package com.julienhammer.go4lunch.ui;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.julienhammer.go4lunch.databinding.ActivityUserBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.viewmodel.MapsViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;

import com.julienhammer.go4lunch.R;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class UserActivity extends Activity {

    private UserViewModel userViewModel = UserViewModel.getInstance();


    ActivityUserBinding getViewBinding(){
        return ActivityUserBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getUserData();

    }

    private void getUserData(){
        userViewModel.getUserData().addOnSuccessListener(user -> {
            // Set the data with the user information
            String username = TextUtils.isEmpty(user.getUserName()) ? getString(R.string.info_no_username_found) : user.getUserName();
            getViewBinding().username.setText(username);
            getViewBinding().userEmail.setText(user.getUserEmail());

        });
    }


}
