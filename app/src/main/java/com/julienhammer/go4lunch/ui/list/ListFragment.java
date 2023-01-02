package com.julienhammer.go4lunch.ui.list;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.data.location.LocationRepository;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private LocationViewModel mLocationViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;
    private static final String TAG = null;
    private ListViewModel listViewModel;
    FragmentListBinding binding;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    RecyclerViewListAdapter adapter;
    private PlacesSearchResult placesSearchResult;


    private void configureViewModel() {
        ViewModelFactory locationViewModelFactory = ViewModelFactory.getInstance();
        mLocationViewModel =
                new ViewModelProvider(requireActivity(), locationViewModelFactory).get(LocationViewModel.class);
    }

    private void initRestaurantsList(){
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel =
                new ViewModelProvider(requireActivity(), restaurantsViewModelFactory).get(RestaurantsViewModel.class);

    }


    public ListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
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
        configureViewModel();
        initRestaurantsList();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        adapter = new RecyclerViewListAdapter();


        Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
        executor.execute(() -> {

            mainExecutor.execute(() -> {

                mLocationViewModel.getLocationLiveData().observe(getViewLifecycleOwner(), location -> {
                    adapter = new RecyclerViewListAdapter(new LatLng(location.getLatitude(),location.getLongitude()));
                    mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {

                        String photoRef;
                        String mMissingPhoto = "%20image%20missing%20reference";
                        ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                        for (int i = 0; i <= (placesSearchResults.length) - 1; i++) {
                            String openNowCase = "";
                            if (placesSearchResults[i].permanentlyClosed){
                                i++;
                            } else {
                                if (placesSearchResults[i].openingHours != null && placesSearchResults[i].openingHours.openNow != null ) {
                                    if (placesSearchResults[i].openingHours.openNow) {
                                        openNowCase = String.valueOf(R.string.openNowCaseTrue);
                                    } else {
                                        openNowCase = String.valueOf(R.string.openNowCaseFalse);
                                    }
                                } else {
                                    openNowCase = String.valueOf(R.string.openNowCaseNotShowing);
                                }
                                if (placesSearchResults[i].photos != null){
                                    photoRef = placesSearchResults[i].photos[0].photoReference;
                                } else {
                                    photoRef = mMissingPhoto;
                                }
                                LatLng resLocation = new LatLng(placesSearchResults[i].geometry.location.lat , placesSearchResults[i].geometry.location.lng);
                                RestaurantDetails restaurantDetails = new RestaurantDetails(
                                        placesSearchResults[i].placeId,
                                        placesSearchResults[i].name,
                                        placesSearchResults[i].vicinity,
                                        photoRef,
                                        openNowCase,
                                        placesSearchResults[i].rating,
                                        resLocation
                                );
                                allRestaurants.add(restaurantDetails);
                            }

                        }
                        adapter.setData(allRestaurants);
                        binding.listViewPlaces.setLayoutManager(layoutManager);
                        binding.listViewPlaces.setAdapter(adapter);
                    });
                });
            });
        });
    }


}