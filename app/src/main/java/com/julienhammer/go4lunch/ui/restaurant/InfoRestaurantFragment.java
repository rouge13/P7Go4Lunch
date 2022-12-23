package com.julienhammer.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Intent;
import static android.Manifest.permission.CALL_PHONE;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
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
import com.google.android.libraries.places.api.model.Place;
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
    static String PLACE_ID = "placeId";
    static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_NAME = "nameRes";
    private static String RESTAURANT_ADDRESS = "addressRes";
    private static String RESTAURANT_OPEN_NOW = "openNowRes";
    private static String RESTAURANT_PHOTO_REF = "photoRefRes";
    private static String RESTAURANT_RATING = "ratingRes";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";


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
        mInfoRestaurantViewModel.getInfoRestaurantLiveData().observe(getViewLifecycleOwner(),restaurantDetails ->
        {
            binding.restaurantInfoCall.setText(R.string.restaurant_call);
            binding.restaurantInfoLike.setText(R.string.restaurant_like);
            binding.restaurantInfoWebsite.setText(R.string.restaurant_website);
            mRestaurantInfo = restaurantDetails;

            mInfoRestaurantViewModel.initRestaurantsDetailsInfo(mRestaurantInfo.getIdRes());
            mUserViewModel.thisRestaurantIsLiked(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()), mRestaurantInfo.getIdRes());
            mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(getViewLifecycleOwner(), placeId -> {

                checkIfRestaurantIsChoiced(mRestaurantInfo.getIdRes(), placeId);

                binding.restaurantInfoName.setText(mRestaurantInfo.getNameRes());
                ConvertToImage.loadGooglePhoto(view.getContext(), binding.itemRestaurantImage, restaurantDetails.getPhotoRefRes());
                binding.itemChoiceRestaurantButton.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("UseCompatLoadingForDrawables")
                    @Override
                    public void onClick(View v) {
                        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = shChoice.edit();
                        if (Objects.equals(mRestaurantInfo.getIdRes(), placeId)){
                            mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),"");


                            myEdit.putString(PLACE_ID, "");
                            myEdit.apply();

                        } else {
                            mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(),mRestaurantInfo.getIdRes());
                            myEdit.putString(PLACE_ID, mRestaurantInfo.getIdRes());
                            myEdit.apply();
                            saveValueOfTheRestaurantChoiceAllDataNeeded(
                                    mRestaurantInfo.getIdRes(),
                                    mRestaurantInfo.getNameRes(),
                                    mRestaurantInfo.getAddressRes(),
                                    mRestaurantInfo.getPhotoRefRes(),
                                    mRestaurantInfo.getOpenNowRes(),
                                    (float) mRestaurantInfo.getRatingRes(),
                                    (float) mRestaurantInfo.getLocationRes().latitude,
                                    (float) mRestaurantInfo.getLocationRes().longitude);
                        }
                        checkIfRestaurantIsChoiced(mRestaurantInfo.getIdRes(), placeId);
                    }
                });
            });

            mUserViewModel.getIfRestaurantIsLiked().observe(getViewLifecycleOwner(), isNotInListOfLikes -> {
                checkIfRestaurantIsLiked(isNotInListOfLikes);
                binding.cardLike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mUserViewModel.setUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
                        checkIfRestaurantIsLiked(isNotInListOfLikes);
                    }
                });
            });


            mInfoRestaurantViewModel.getRestaurantDetailsInfoLiveData().observe(getViewLifecycleOwner(), place -> {
//                String phoneNumber = place.getPhoneNumber();
                String websiteRestaurant = place.getWebsiteUri().toString();
                binding.cardCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        callOnPhoneNumber(place);
                    }
                });
                binding.cardWebsite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        goOnWebsiteOfThePlace(place);
                    }
                });
            });
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            binding.workmatesView.setLayoutManager(layoutManager);
            adapter = new RecyclerViewWorkmateAdapter(true, getActivity());

            mInfoRestaurantViewModel.getAllWorkmatesInThisRestaurantLiveData().observe(getViewLifecycleOwner(), allWorkmatesInThisRestaurant -> {
                ArrayList<User> allWorkmatesInThisRestaurantList = new ArrayList<User>();
                ArrayList<String> restaurantNameWhereTheWorkmateEat = new ArrayList<>();
                for (int i = 0; i <= (allWorkmatesInThisRestaurant.size()) -1; i++){
                    allWorkmatesInThisRestaurantList.add(allWorkmatesInThisRestaurant.get(i));
                }
                binding.workmatesView.setAdapter(adapter);
                adapter.setData(allWorkmatesInThisRestaurantList, restaurantNameWhereTheWorkmateEat);
            });

            binding.itemBackwardButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().onBackPressed();
                }
            });
        });
    }

    private void checkIfRestaurantIsChoiced(String idRes, String placeId) {
        if (!Objects.equals(idRes, placeId)){
            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_block_48));
        } else {
            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        }
    }

    private void goOnWebsiteOfThePlace(Place place) {
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

    private void checkIfRestaurantIsLiked(boolean isNotInListOfLikes) {
        if (!isNotInListOfLikes){
            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_yellow_24));
        } else {
            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
        }
    }
    private void callOnPhoneNumber(Place place) {
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

    private void saveValueOfTheRestaurantChoiceAllDataNeeded(String placeId, String nameRes, String addressRes, String photoRefRes, String openNowRes, float ratingRes, float latRes, float lngRes) {
        // Storing data into SharedPreferences
        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE,MODE_PRIVATE);
        // Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = shChoice.edit();
        // Storing the key and its value as the data fetched from edittext
        myEdit.putString(PLACE_ID, placeId);
        myEdit.putString(RESTAURANT_NAME, nameRes);
        myEdit.putString(RESTAURANT_ADDRESS, addressRes);
        myEdit.putString(RESTAURANT_PHOTO_REF, photoRefRes);
        myEdit.putString(RESTAURANT_OPEN_NOW, openNowRes);
        myEdit.putFloat(RESTAURANT_RATING, ratingRes);
        myEdit.putFloat(RESTAURANT_LAT, latRes);
        myEdit.putFloat(RESTAURANT_LNG, lngRes);
        // Once the changes have been made,
        // we need to commit to apply those changes made,
        // otherwise, it will throw an error
        myEdit.apply();
    }


}