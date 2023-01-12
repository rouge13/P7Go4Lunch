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
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {
    FragmentListBinding binding;
    ViewModelFactory viewModelFactory = ViewModelFactory.getInstance();
    private static final String MISSING_PHOTO_REFERENCE = "%20image%20missing%20reference";

    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListFragment newInstance() {
        ListFragment fragment = new ListFragment();
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
        RecyclerViewListAdapter adapter = new RecyclerViewListAdapter();
        initAdapterAndRv(adapter);
        addLocationViewModel(adapter);
        addAllRestaurantsViewModel(adapter);
    }

    private void addAllRestaurantsViewModel(RecyclerViewListAdapter adapter) {
        RestaurantsViewModel restaurantsViewModel =
                new ViewModelProvider(requireActivity(), viewModelFactory).get(RestaurantsViewModel.class);
        restaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {
            String photoRef;
            String mMissingPhoto = MISSING_PHOTO_REFERENCE;
            ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
            for (int i = 0; i < (placesSearchResults.length); i++) {
                String openNowCase = "";
                if (placesSearchResults[i].permanentlyClosed) {
                    i++;
                } else {
                    if (placesSearchResults[i].openingHours != null && placesSearchResults[i].openingHours.openNow != null) {
                        if (placesSearchResults[i].openingHours.openNow) {
                            openNowCase = getString(R.string.openNowCaseTrue);
                        } else {
                            openNowCase = getString(R.string.openNowCaseFalse);
                        }
                    } else {
                        openNowCase = getString(R.string.openNowCaseNotShowing);
                    }
                    if (placesSearchResults[i].photos != null) {
                        photoRef = placesSearchResults[i].photos[0].photoReference;
                    } else {
                        photoRef = mMissingPhoto;
                    }
                    LatLng resLocation = new LatLng(placesSearchResults[i].geometry.location.lat, placesSearchResults[i].geometry.location.lng);
                    RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId, placesSearchResults[i].name, placesSearchResults[i].vicinity, photoRef, openNowCase, placesSearchResults[i].rating, resLocation
                    );
                    allRestaurants.add(restaurantDetails);
                }
            }
            adapter.setData(allRestaurants);
        });
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