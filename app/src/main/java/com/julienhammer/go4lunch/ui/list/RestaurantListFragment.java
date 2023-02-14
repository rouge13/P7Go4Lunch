package com.julienhammer.go4lunch.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.PlacesResponse;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantListFragment extends Fragment {
    FragmentListBinding binding;
    ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
    private static final String MISSING_PHOTO_REFERENCE = "%20image%20missing%20reference";

    public RestaurantListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RestaurantListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantListFragment newInstance() {
        RestaurantListFragment fragment = new RestaurantListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerViewListAdapter adapter = new RecyclerViewListAdapter((MainActivity) getActivity());
        initAdapterAndRv(adapter);
        addLocationViewModel(adapter);
        addAllRestaurantsViewModel(adapter);
    }

    private void addAllRestaurantsViewModel(RecyclerViewListAdapter adapter) {
        RestaurantsViewModel mRestaurantsViewModel =
                new ViewModelProvider(requireActivity(), viewModelFactory).get(RestaurantsViewModel.class);
        mRestaurantsViewModel.getNearbyPlaces().observe(getViewLifecycleOwner(), places -> {

            initAllRestaurants(adapter, places);
            mRestaurantsViewModel.getAllSearchFilteredRestaurant().observe(getViewLifecycleOwner(), filteredRestaurant -> {
                if (!filteredRestaurant.isEmpty()){
                    ArrayList<PlacesResponse.Result> filteredPlaces = new ArrayList<>();
                    for (int i = 0; i < places.results.size(); i++){
                        if (filteredRestaurant.contains(places.results.get(i).name)){
                            filteredPlaces.add(places.results.get(i));
                        }
                    }
                    initAllRestaurants(adapter, new PlacesResponse.Root(null, null, filteredPlaces, "OK"));
                } else {
                    initAllRestaurants(adapter, places);
                }
            });
        });
    }

    private void initAllRestaurants(RecyclerViewListAdapter adapter, PlacesResponse.Root places) {
        String photoRef;
        String mMissingPhoto = MISSING_PHOTO_REFERENCE;
        ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
        for (int i = 0; i < (places.results.size()); i++) {
            String openNowText = "";
            openNowText = getString(getOpenHourTextId(places.results.get(i).opening_hours != null
                    ? places.results.get(i).opening_hours.open_now : null));
            if (places.results.get(i).photos != null) {
                photoRef = places.results.get(i).photos.get(0).photo_reference;
            } else {
                photoRef = mMissingPhoto;
            }
            LatLng resLocation = new LatLng(places.results.get(i).geometry.location.lat, places.results.get(i).geometry.location.lng);
            RestaurantDetails restaurantDetails = new RestaurantDetails(places.results.get(i).place_id, places.results.get(i).name, places.results.get(i).formatted_address, photoRef, openNowText, (float) places.results.get(i).rating, resLocation
            );
            allRestaurants.add(restaurantDetails);
        }
        adapter.setData(allRestaurants);
    }

    @NonNull
    public int getOpenHourTextId(Boolean openNow) {
        if (openNow == null) {
            return R.string.open_now_case_not_showing;
        } if (openNow) {
            return R.string.open_now_case_true;
        } else {
            return R.string.open_now_case_false;
        }
    }

    private void addLocationViewModel(RecyclerViewListAdapter adapter) {
        LocationViewModel locationViewModel = new ViewModelProvider(requireActivity(), viewModelFactory).get(LocationViewModel.class);
        locationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
            adapter.setLocation(new LatLng(location.getLatitude(), location.getLongitude()));
        });
    }

    private void initAdapterAndRv(RecyclerViewListAdapter adapter) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.listViewPlaces.setLayoutManager(layoutManager);
        binding.listViewPlaces.setAdapter(adapter);
    }
}