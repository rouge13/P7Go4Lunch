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
        adapter = new RecyclerViewWorkmateAdapter(false);
        mWorkmateViewModel.getWorkmateMutableLiveData().observe(getViewLifecycleOwner(), workmate -> {
            mRestaurantsViewModel.getRestaurantsLiveData().observe(getViewLifecycleOwner(), placesSearchResults -> {
                ArrayList<String> restaurantNameWhereTheWorkmateEat = new ArrayList<String>();
                ArrayList<User> allWorkmates = new ArrayList<User>();

                for (int t = 0; t <= (workmate.size()) -1; t++){
                    allWorkmates.add(workmate.get(t));
                    for (int i = 0; i <= (placesSearchResults.length) -1; i++){
                        if (Objects.equals(workmate.get(t).getUserPlaceId(), placesSearchResults[i].placeId)){
                            restaurantNameWhereTheWorkmateEat.add(t, placesSearchResults[i].name);
                            break;

                        }
                        else if (Objects.equals(workmate.get(t).getUserPlaceId(), "")) {
                            restaurantNameWhereTheWorkmateEat.add(t, "");
                            break;
                        }
                    }
                }

                adapter.setData(allWorkmates, restaurantNameWhereTheWorkmateEat);
                binding.workmatesView.setLayoutManager(layoutManager);
                binding.workmatesView.setAdapter(adapter);

            });

        });
    }

}