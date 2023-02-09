package com.julienhammer.go4lunch.ui.workmates;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ActivityMainBinding;
import com.julienhammer.go4lunch.databinding.FragmentWorkmatesBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.WorkmateViewModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorkmatesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WorkmatesFragment extends Fragment {
    FragmentWorkmatesBinding binding;
    private WorkmateViewModel mWorkmateViewModel;
    private RestaurantsViewModel mRestaurantsViewModel;
//    ActivityMainBinding activityMainBinding;
//    Toolbar toolbar;
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
     * @return A new instance of fragment RestaurantListFragment.
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
//        initBasicToolbar();
//        initSearchToolbar();
        // Inflate the layout for this fragment
        binding = FragmentWorkmatesBinding.inflate(inflater, container, false);
//        configureToolBar();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRestaurantsList();
        configureWormatesViewModel();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        adapter = new RecyclerViewWorkmateAdapter(false, getActivity());
        mWorkmateViewModel.getWorkmateMutableLiveData().observe(getViewLifecycleOwner(), workmate -> {
            mRestaurantsViewModel.getNearbyPlaces().observe(getViewLifecycleOwner(), placesSearchResults -> {
                ArrayList<User> allWorkmates = new ArrayList<User>();
                ArrayList<String> allRestaurantsName = new ArrayList<>();
                for (int t = 0; t <= (workmate.size()) -1; t++) {
                    allWorkmates.add(workmate.get(t));
                    allRestaurantsName.add("");
                }
                adapter.setData(allWorkmates, allRestaurantsName);
                binding.workmatesView.setLayoutManager(layoutManager);
                binding.workmatesView.setAdapter(adapter);
            });
        });
    }

//    private void configureToolBar() {
//        toolbar = activityMainBinding.customToolbar;
//        toolbar.setTitle(R.string.available_workmates);
//
//        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
//        if (actionBar != null){
//            actionBar.setDisplayHomeAsUpEnabled(false);
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_menu_24);
//        }
//    }

//    private void initBasicToolbar() {
//        activityMainBinding.restaurantSearchEditText.getText().clear();
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activityMainBinding.restaurantsRecyclerView.setVisibility(View.INVISIBLE);
//        activityMainBinding.searchConstraint.setVisibility(View.GONE);
//        activityMainBinding.searchRestaurantImage.setVisibility(View.VISIBLE);
//    }
//
//    private void initSearchToolbar() {
//        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//        activityMainBinding.searchConstraint.setVisibility(View.VISIBLE);
//        activityMainBinding.searchRestaurantImage.setVisibility(View.GONE);
//    }
}