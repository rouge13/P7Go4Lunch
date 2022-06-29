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

import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListFragment extends Fragment {

    private static final String TAG = null;
    private ListViewModel listViewModel;
    FragmentListBinding binding;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    RecyclerViewListAdapter adapter;
    private PlacesSearchResult placesSearchResult;
//    ImageView imageView;
//    GoogleApiClient mGoogleApiClient;
//    public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=100";

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

                    ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                    for (int i = 0; i <= (placesSearchResults.length) -1; i++){
                        String openNowCase ="";

                        if (placesSearchResults[i].openingHours != null && placesSearchResults[i].openingHours.openNow != null){

                            Boolean openNow = placesSearchResults[i].openingHours.openNow;

                            if (openNow = false){
                                openNowCase = "Not open";
                            } else {
                                openNowCase = "Open now";
                            }

                        }else{

                            openNowCase = "Doesn't show if it's open";

                        }

                        RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId,placesSearchResults[i].name,placesSearchResults[i].vicinity, placesSearchResults[i].photos[0].photoReference, placesSearchResults[i].icon.toString(), openNowCase, placesSearchResults[i].photos[0]);
                        allRestaurants.add(restaurantDetails);

                    }

                    adapter.setData(allRestaurants);
                    binding.listViewPlaces.setLayoutManager(layoutManager);
                    binding.listViewPlaces.setAdapter(adapter);

//                    layoutManager.scrollToPosition(allRestaurants.size() -1);


                });
            });
        });


    }






}