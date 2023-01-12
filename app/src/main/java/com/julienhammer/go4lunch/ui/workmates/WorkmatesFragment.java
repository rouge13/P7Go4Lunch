package com.julienhammer.go4lunch.ui.workmates;

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

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.databinding.FragmentWorkmatesBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.ui.list.ListFragment;
import com.julienhammer.go4lunch.ui.list.RecyclerViewListAdapter;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {
    FragmentWorkmatesBinding binding;
    private WorkmateViewModel mWorkmateViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;

    RecyclerViewWorkmateAdapter adapter;

    private void initRestaurantsList(){
        ViewModelFactory restaurantsViewModelFactory = ViewModelFactory.getInstance();
        mRestaurantsViewModel =
                new ViewModelProvider(requireActivity(), restaurantsViewModelFactory).get(RestaurantsViewModel.class);

    }

    private void configureWormatesViewModel() {
        ViewModelFactory workmateViewModelFactory = ViewModelFactory.getInstance();
        mWorkmateViewModel =
                new ViewModelProvider(this, workmateViewModelFactory).get(WorkmateViewModel.class);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment ListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WorkmatesFragment newInstance() {
        WorkmatesFragment fragment = new WorkmatesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRestaurantsList();
        configureWormatesViewModel();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
//        if (!Places.isInitialized()){
//            Places.initialize(requireActivity().getApplicationContext(), getString(R.string.google_api_key));
//        }
//        PlacesClient placesClient = Places.createClient(requireActivity());
        adapter = new RecyclerViewWorkmateAdapter(false, getActivity());
        mWorkmateViewModel.getWorkmateMutableLiveData().observe(getViewLifecycleOwner(), workmate -> {
            mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {
//                ArrayList<String> restaurantNameWhereTheWorkmateEat = new ArrayList<String>();
                ArrayList<User> allWorkmates = new ArrayList<User>();
                ArrayList<String> allRestaurantsName = new ArrayList<>();
                for (int t = 0; t <= (workmate.size()) -1; t++) {
                    allWorkmates.add(workmate.get(t));
                    allRestaurantsName.add("");
                }
//                ArrayList<String> allPlaceIdFromNearbySearch = new ArrayList<>();
//                for (int i = 0; i <= (placesSearchResults.length -1); i++){
//                    allPlaceIdFromNearbySearch.add(placesSearchResults[i].placeId);
//                }
//                for (int t = 0; t <= (workmate.size()) -1; t++){
//                    allWorkmates.add(workmate.get(t));
//
//                    if (Objects.equals(workmate.get(t).getUserPlaceId(), "")) {
//                        restaurantNameWhereTheWorkmateEat.add(t, "");
//                    }
//
//                    else if (allPlaceIdFromNearbySearch.contains(workmate.get(t).getUserPlaceId())){
//                        for (int i = 0; i <= (placesSearchResults.length -1); i++){
//                            if (Objects.equals(workmate.get(t).getUserPlaceId(), placesSearchResults[i].placeId)) {
//                                restaurantNameWhereTheWorkmateEat.add(t, placesSearchResults[i].name);
//                                break;
//                            }
//                        }
//                    }
//                    else {

//                        int workmateNumber = t;
//                        mRestaurantsViewModel.getNameOfRestaurantIfMissingInSearchResult(workmate.get(t).getUserPlaceId()).observe(getViewLifecycleOwner(), place -> {
//                            restaurantNameWhereTheWorkmateEat.add(workmateNumber, place.getName());
//
//                        });
//                    }
//                }



                adapter.setData(allWorkmates, allRestaurantsName);
                binding.workmatesView.setLayoutManager(layoutManager);
                binding.workmatesView.setAdapter(adapter);

            });

        });
    }

}