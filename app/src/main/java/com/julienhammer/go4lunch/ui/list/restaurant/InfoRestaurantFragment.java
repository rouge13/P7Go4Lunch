package com.julienhammer.go4lunch.ui.list.restaurant;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.julienhammer.go4lunch.databinding.FragmentInfoRestaurantBinding;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantFragment extends Fragment {
    FragmentInfoRestaurantBinding binding;
    private RestaurantDetails mRestaurants;
    InfoRestaurantViewModel mInfoRestaurantViewModel;

    public static InfoRestaurantFragment newInstance(){
        InfoRestaurantFragment fragment = new InfoRestaurantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRestaurant();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInfoRestaurantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mInfoRestaurantViewModel.getInfoRestaurantLiveData().observe(getViewLifecycleOwner(),restaurantDetails ->
        {
            mRestaurants = restaurantDetails;
            binding.restaurantInfoName.setText(mRestaurants.getNameRes());

        });
    }

    private void initRestaurant(){
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel = new ViewModelProvider(requireActivity(), infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
    }



}
