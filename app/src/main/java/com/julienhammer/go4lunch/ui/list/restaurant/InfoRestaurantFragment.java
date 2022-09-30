package com.julienhammer.go4lunch.ui.list.restaurant;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.julienhammer.go4lunch.databinding.FragmentInfoRestaurantBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;

import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class InfoRestaurantFragment extends Fragment {
    FragmentInfoRestaurantBinding binding;
    private RestaurantDetails mRestaurantInfo;
    InfoRestaurantViewModel mInfoRestaurantViewModel;
    UserViewModel mUserViewModel;
    AppCompatActivity activity;
    public static InfoRestaurantFragment newInstance(){
        InfoRestaurantFragment fragment = new InfoRestaurantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRestaurant();
//        EventBus.getDefault().register(this);
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
        activity = (AppCompatActivity) view.getContext();


//            if (!EventBus.getDefault().isRegistered(this)) {
//                EventBus.getDefault().register(this);
//
//        }
        mInfoRestaurantViewModel.getInfoRestaurantLiveData().observe(getViewLifecycleOwner(),restaurantDetails ->
        {
            mRestaurantInfo = restaurantDetails;
            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            mUserViewModel.thisRestaurantIsLiked(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()), mRestaurantInfo.getIdRes());
            mUserViewModel.getIfSelectedRestaurantIsChoiced().observe(getViewLifecycleOwner(), placeId -> {
                if (!Objects.equals(mRestaurantInfo.getIdRes(), placeId)){
                    binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_block_48));
                } else {
                    binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
                }
                binding.restaurantInfoName.setText(mRestaurantInfo.getNameRes());
                ConvertToImage.loadGooglePhoto(view.getContext(), binding.itemRestaurantImage, restaurantDetails.getPhotoRefRes());
                binding.itemChoiceRestaurantButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        if (Objects.equals(mRestaurantInfo.getIdRes(), placeId)){
                            mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),"");
                            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_block_48));
                        } else {
                            mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),mRestaurantInfo.getIdRes());
                            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
                        }
                    }
                });


            });
            mUserViewModel.getIfRestaurantIsLiked().observe(getViewLifecycleOwner(), isLiked -> {
                if (isLiked){
                    binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                } else {
                    binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                }



            });

//            binding.cardLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (isLiked){
//                        mUserViewModel.setUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser(), "");
//                        binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_24));
//                    } else {
//                        mUserViewModel.setUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
//                        binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
//
//                    }
//                }
//            });

//            binding.cardLike.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mInfoRestaurantViewModel.createRestaurantLiked(mRestaurantInfo.getIdRes(), FirebaseAuth.getInstance().getCurrentUser());
//                }
//            });


//            binding.itemBackwardButton.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    finish();
//                }
//            });

        });

//        binding.restaurantInfoName.setText(mRestaurantInfo.getNameRes());

    }

    private void initRestaurant(){
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel = new ViewModelProvider(requireActivity(), infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
        ViewModelFactory userViewModelFactory = ViewModelFactory.getInstance();
        mUserViewModel = new ViewModelProvider(requireActivity(), userViewModelFactory).get(UserViewModel.class);

    }

//    private void changeValueOfSharedPreferences(String restaurantId){
//        // Storing data into SharedPreferences
//        SharedPreferences shChoice = activity.getSharedPreferences("MyRestaurantChoice",MODE_PRIVATE);
//        SharedPreferences.Editor myEdit = shChoice.edit();
//        myEdit.putString("placeId", restaurantId);
//        myEdit.apply();
//    }




}
