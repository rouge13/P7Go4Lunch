package com.julienhammer.go4lunch.ui.list;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.utils.NearbySearch;
import com.julienhammer.go4lunch.viewmodel.ListViewModel;

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
    ItemPlaceBinding bindingItemPlace;
    ExecutorService executor = Executors.newSingleThreadExecutor();

    private void configureListViewModel() {
        ViewModelFactory listViewModelFactory = ViewModelFactory.getInstance();
        ListViewModel listViewModel =
                new ViewModelProvider(this, listViewModelFactory).get(ListViewModel.class);
        listViewModel.refresh();
        this.listViewModel = new ViewModelProvider(
                this,
                ViewModelFactory.getInstance()
        ).get(ListViewModel.class);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        bindingItemPlace = ItemPlaceBinding.inflate(inflater, container, false);
//
//        return bindingItemPlace.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        configureListViewModel();
        listViewModel.getLocationForPlacesLiveData().observe(getViewLifecycleOwner(), location -> {
            Executor mainExecutor = ContextCompat.getMainExecutor(getContext());
            executor.execute(() -> {
                PlacesSearchResult[] placesSearchResults = new NearbySearch().run(
                        getString(R.string.google_map_key),
                        location
                ).results;
                mainExecutor.execute(()->{
                    Marker[] allMarkers = new Marker[placesSearchResults.length];
                    for (int i = 0; i <= (placesSearchResults.length) -1; i++){
//                        String placeLoc = placesSearchResults[i].name;
//                        bindingItemPlace.textViewName.setText(placeLoc);
//                        bindingItemPlace.textViewOpeningHours.setText(placesSearchResults[i].openingHours.openNow.toString());
                    }

                });
            });
        });


    }
}