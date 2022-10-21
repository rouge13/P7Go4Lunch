package com.julienhammer.go4lunch.ui.list.restaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import static android.Manifest.permission.CALL_PHONE;

import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.julienhammer.go4lunch.databinding.FragmentInfoRestaurantBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.ui.workmates.RecyclerViewWorkmateAdapter;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;
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
    RecyclerViewWorkmateAdapter adapter;


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
            binding.restaurantInfoCall.setText(R.string.restaurant_call);
            binding.restaurantInfoLike.setText(R.string.restaurant_like);
            binding.restaurantInfoWebsite.setText(R.string.restaurant_website);
            mRestaurantInfo = restaurantDetails;
            mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
            mUserViewModel.userRestaurantSelected(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
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
            mUserViewModel.thisRestaurantIsLiked(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()), mRestaurantInfo.getIdRes());

            mUserViewModel.getIfRestaurantIsLiked().observe(getViewLifecycleOwner(), isNotInListOfLikes -> {
                if (!isNotInListOfLikes){
                    binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                } else {
                    binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                }
                binding.cardLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserViewModel.setUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
                        if (isNotInListOfLikes){
                            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_24));
                        } else {
                            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
                        }
                    }
                });


            });

            mInfoRestaurantViewModel.initRestaurantsDetailsInfo(mRestaurantInfo.getIdRes());
            mInfoRestaurantViewModel.getRestaurantDetailsInfoLiveData().observe(getViewLifecycleOwner(), place -> {
//                String phoneNumber = place.getPhoneNumber();
                String websiteRestaurant = place.getWebsiteUri().toString();
                binding.cardCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(place.getPhoneNumber()!= null){
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:"+place.getPhoneNumber()));//change the number
                            if (ContextCompat.checkSelfPermission(requireActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(callIntent);
                            } else {
                                requestPermissions(new String[]{CALL_PHONE}, 1);
                            }

                        } else {
                            Toast.makeText(getContext(), R.string.no_restaurant_phone_number + mRestaurantInfo.getNameRes(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                binding.cardWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(place.getWebsiteUri() != null){
                            String url = "";
                            if (!place.getWebsiteUri().toString().startsWith("http://") && !place.getWebsiteUri().toString().startsWith("https://")){
                                url = "http://" + place.getWebsiteUri().toString();
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                startActivity(browserIntent);
                            } else {
                                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(place.getWebsiteUri().toString()));
                                startActivity(browserIntent);
                            }

                        } else {
                            Toast.makeText(getContext(), R.string.no_restaurant_website + mRestaurantInfo.getNameRes(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            });


            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.workmatesView.setLayoutManager(layoutManager);
            adapter = new RecyclerViewWorkmateAdapter(true);
            ArrayList<User> allWorkmatesInThisRestaurantList = new ArrayList<User>();
            mInfoRestaurantViewModel.getAllWorkmatesInThisRestaurantLiveData().observe(getViewLifecycleOwner(), allWorkmatesInThisRestaurant -> {
                for (int i = 0; i <= (allWorkmatesInThisRestaurant.size()) -1; i++){
                    allWorkmatesInThisRestaurantList.add(allWorkmatesInThisRestaurant.get(i));
                }
                binding.workmatesView.setAdapter(adapter);
                adapter.setData(allWorkmatesInThisRestaurantList);
            });

            binding.itemBackwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });


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

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
    }
    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }


}
