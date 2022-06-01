package com.julienhammer.go4lunch.ui.list;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.Photo;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.ui.RecyclerViewListAdapter;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.Arrays;
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

    private ListViewModel listViewModel;
    FragmentListBinding binding;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    RecyclerView mRecyclerView;
//    RestaurantDetails mRestaurants = null;
    RecyclerViewListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;


    private void configureListViewModel() {
        ViewModelFactory listViewModelFactory = ViewModelFactory.getInstance();
        ListViewModel listViewModel =
                new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);

        this.listViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(ListViewModel.class);
//        listViewModel.refresh();
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
//        binding = binding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());

//        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
//        binding.listViewPlaces.setLayoutManager(layoutManager);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater, container, false);
        return binding.getRoot();
              //        binding = FragmentListBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        layoutManager = new LinearLayoutManager(this);
//        binding.listViewPlaces.setLayoutManager(layoutManager);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureListViewModel();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());

        adapter = new RecyclerViewListAdapter();

        listViewModel.getLocationForPlacesLiveData().observe(getViewLifecycleOwner(), location -> {

            Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
            executor.execute(() -> {
                PlacesSearchResult[] placesSearchResults = new NearbySearch().run(
                        getString(R.string.google_map_key),
                        location
                ).results;
                mainExecutor.execute(()->{

//                    for (PlacesSearchResult placesSearchResult : placesSearchResults){
//                        String placeName = placesSearchResults.;
////                        mRestaurants.setAddressRes(placesSearchResults[i].vicinity);
//                    }

                    ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                    for (int i = 0; i <= (placesSearchResults.length) -1; i++){
//                        String placeLoc = placesSearchResults[i].name;
//                        String urlPhoto = placesSearchResults[i].photos. ();

                        RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId,placesSearchResults[i].name,placesSearchResults[i].vicinity);

//                        RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId,placesSearchResults[i].name,placesSearchResults[i].vicinity);
//                        mRestaurants.setIdRes(placesSearchResults[i].placeId);
//                        mRestaurants.setNameRes(placesSearchResults[i].name);
//                        mRestaurants.setAddressRes(placesSearchResults[i].vicinity);
                        allRestaurants.add(restaurantDetails);

                    }
//                    mRecyclerView.setAdapter(new RecyclerViewListAdapter(mRestaurants));
                    adapter.setData(allRestaurants);
                    binding.listViewPlaces.setAdapter(adapter);
                    binding.listViewPlaces.setLayoutManager(layoutManager);

                });
            });
        });


    }
}