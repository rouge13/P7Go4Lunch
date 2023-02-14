package com.julienhammer.go4lunch.ui.restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import static android.Manifest.permission.CALL_PHONE;
import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.julienhammer.go4lunch.interfaces.OnInfoRestaurantSelectedListener;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.ui.workmates.RecyclerViewWorkmateAdapter;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.SharedRestaurantSelectedViewModel;
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
    RestaurantsViewModel mRestaurantsViewModel;
    SharedRestaurantSelectedViewModel mSharedRestaurantSelectedViewModel;
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

    public static InfoRestaurantFragment newInstance() {
        InfoRestaurantFragment fragment = new InfoRestaurantFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRestaurantViewModel();
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
        mSharedRestaurantSelectedViewModel.getSelectedRestaurant().observe(getViewLifecycleOwner(), restaurantDetails ->
        {
            setTextToView();
            mRestaurantInfo = restaurantDetails;
            mInfoRestaurantViewModel.initRestaurantsDetailsInfo(mRestaurantInfo.getIdRes());
            mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(mRestaurantInfo.getIdRes());
            initAllWorkmates();
            mUserViewModel.thisRestaurantIsLiked(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()), mRestaurantInfo.getIdRes());
            mUserViewModel.getSelectedRestaurantIsChoiced().observe(getViewLifecycleOwner(), placeId -> {
                initChoosedRestaurant(placeId);
                binding.restaurantInfoName.setText(mRestaurantInfo.getNameRes());
                binding.itemChoiceRestaurantButton.setOnClickListener(v -> {
                    SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = shChoice.edit();
                    if (Objects.equals(mRestaurantInfo.getIdRes(), placeId)) {
                        mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), "");
                        myEdit.putString(PLACE_ID, "");
                        mRestaurantsViewModel.initAllRestaurantChoosed();
                        mRestaurantsViewModel.initIsSomeoneEatingThere("NotHere");
                    } else {
                        mUserViewModel.setUserRestaurantChoice(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), mRestaurantInfo.getIdRes());
                        myEdit.putString(PLACE_ID, mRestaurantInfo.getIdRes());
                        saveValueOfTheRestaurantChoiceAllDataNeeded(mRestaurantInfo);
                        mRestaurantsViewModel.initAllRestaurantChoosed();
                        mRestaurantsViewModel.initIsSomeoneEatingThere(mRestaurantInfo.getIdRes());
                    }
                    myEdit.apply();
                    checkIfRestaurantIsChoiced(mRestaurantInfo.getIdRes(), placeId);
                    mInfoRestaurantViewModel.initAllWorkmatesInThisRestaurantMutableLiveData(mRestaurantInfo.getIdRes());
                });
            });
            mUserViewModel.getIfRestaurantIsLiked().observe(getViewLifecycleOwner(), isNotInListOfLikes -> {
                checkIfRestaurantIsLiked(isNotInListOfLikes);
                initRestaurantLike(isNotInListOfLikes);
            });
            mInfoRestaurantViewModel.getRestaurantDetailsInfoLiveData().observe(getViewLifecycleOwner(), place -> {
                initRestaurantBtnCallWebsiteAndImage(place);
            });
            initBackButton();
        });
    }

    private void initBackButton() {
        binding.itemBackwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
    }

    private void initRestaurantLike(Boolean isNotInListOfLikes) {
        binding.cardLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserViewModel.setUserRestaurantLikes(FirebaseAuth.getInstance().getCurrentUser(), mRestaurantInfo.getIdRes());
                checkIfRestaurantIsLiked(isNotInListOfLikes);
            }
        });
    }

    private void initRestaurantBtnCallWebsiteAndImage(Place place) {
        mInfoRestaurantViewModel.getRestaurantPhotoBitmap().observe(getViewLifecycleOwner(), bitmap -> {
            binding.itemRestaurantImage.setImageBitmap(bitmap);
        });
        binding.cardCall.setOnClickListener(view -> callOnPhoneNumber(place));
        binding.cardWebsite.setOnClickListener(view -> goOnWebsiteOfThePlace(place));
    }

    private void initChoosedRestaurant(String placeId) {
        if (!Objects.equals(placeId, "") && Objects.equals(mRestaurantInfo.getIdRes(), placeId)) {
            checkIfRestaurantIsChoiced(mRestaurantInfo.getIdRes(), placeId);
        } else {
            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_block_48));
        }
    }

    private void initAllWorkmates() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.workmatesView.setLayoutManager(layoutManager);
        adapter = new RecyclerViewWorkmateAdapter(true, getActivity());
        // Supprimer l'observe
        mInfoRestaurantViewModel.getAllWorkmatesInThisRestaurantLiveData().observe(getViewLifecycleOwner(), allWorkmatesInThisRestaurant -> {
            ArrayList<User> allWorkmatesInThisRestaurantList = new ArrayList<User>();
            ArrayList<String> restaurantNameWhereTheWorkmateEat = new ArrayList<>();
            for (int i = 0; i <= (allWorkmatesInThisRestaurant.size()) - 1; i++) {
                allWorkmatesInThisRestaurantList.add(allWorkmatesInThisRestaurant.get(i));
            }
            binding.workmatesView.setAdapter(adapter);
            adapter.setData(allWorkmatesInThisRestaurantList, restaurantNameWhereTheWorkmateEat);
        });
    }

    private void setTextToView() {
        binding.restaurantInfoCall.setText(R.string.restaurant_call);
        binding.restaurantInfoLike.setText(R.string.restaurant_like);
        binding.restaurantInfoWebsite.setText(R.string.restaurant_website);
    }

    private void checkIfRestaurantIsChoiced(String idRes, String placeId) {
        if (!Objects.equals(idRes, placeId) && Objects.equals(placeId, "")) {
            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_block_48));
        } else {
            binding.itemChoiceRestaurantButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_check_circle_24));
        }
    }

    private void goOnWebsiteOfThePlace(Place place) {
        if (place.getWebsiteUri() != null) {
            String url = "";
            if (!place.getWebsiteUri().toString().startsWith("http://") && !place.getWebsiteUri().toString().startsWith("https://")) {
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
        if (!isNotInListOfLikes) {
            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_yellow_24));
        } else {
            binding.restaurantInfoLikeIcon.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_star_rate_24));
        }
    }

    private void callOnPhoneNumber(Place place) {
        if (place.getPhoneNumber() != null) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + place.getPhoneNumber()));//change the number
            if (ContextCompat.checkSelfPermission(requireActivity(), CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                startActivity(callIntent);
            } else {
                requestPermissions(new String[]{CALL_PHONE}, 1);
            }

        } else {
            Toast.makeText(getContext(), R.string.no_restaurant_phone_number + mRestaurantInfo.getNameRes(), Toast.LENGTH_SHORT).show();
        }
    }

    private void initRestaurantViewModel() {
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel = new ViewModelProvider(this, infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
        ViewModelFactory userViewModelFactory = ViewModelFactory.getInstance();
        mUserViewModel = new ViewModelProvider(requireActivity(), userViewModelFactory).get(UserViewModel.class);
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel = new ViewModelProvider(requireActivity(), restaurantsViewModelFactory).get(RestaurantsViewModel.class);
        ViewModelFactory sharedRestaurantSelectedViewModelFactory = ViewModelFactory.getInstance();
        mSharedRestaurantSelectedViewModel = new ViewModelProvider(requireActivity(), sharedRestaurantSelectedViewModelFactory).get(SharedRestaurantSelectedViewModel.class);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    private void saveValueOfTheRestaurantChoiceAllDataNeeded(RestaurantDetails mRestaurantInfo) {
        SharedPreferences shChoice = getActivity().getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        SharedPreferences.Editor myEdit = shChoice.edit();
        myEdit.putString(PLACE_ID, mRestaurantInfo.getIdRes());
        myEdit.putString(RESTAURANT_NAME, mRestaurantInfo.getNameRes());
        myEdit.putString(RESTAURANT_ADDRESS, mRestaurantInfo.getAddressRes());
        myEdit.putString(RESTAURANT_PHOTO_REF, mRestaurantInfo.getPhotoRefRes());
        myEdit.putString(RESTAURANT_OPEN_NOW, mRestaurantInfo.getOpenNowRes());
        myEdit.putFloat(RESTAURANT_RATING, (float) mRestaurantInfo.getRatingRes());
        myEdit.putFloat(RESTAURANT_LAT, (float) mRestaurantInfo.getLocationRes().latitude);
        myEdit.putFloat(RESTAURANT_LNG, (float) mRestaurantInfo.getLocationRes().longitude);
        myEdit.apply();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ViewModelFactory sharedRestaurantSelectedViewModelFactory = ViewModelFactory.getInstance();
        mSharedRestaurantSelectedViewModel = new ViewModelProvider(requireActivity(), sharedRestaurantSelectedViewModelFactory).get(SharedRestaurantSelectedViewModel.class);

    }
}
