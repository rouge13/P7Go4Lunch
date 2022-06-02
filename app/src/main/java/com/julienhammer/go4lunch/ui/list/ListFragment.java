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
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.maps.model.PlacesSearchResult;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.ui.RecyclerViewListAdapter;
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
    RecyclerView mRecyclerView;
//    RestaurantDetails mRestaurants = null;
    RecyclerViewListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
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

//                    for (PlacesSearchResult placesSearchResult : placesSearchResults){
//                        String placeName = placesSearchResults.;
////                        mRestaurants.setAddressRes(placesSearchResults[i].vicinity);
//                    }

                    ArrayList<RestaurantDetails> allRestaurants = new ArrayList<RestaurantDetails>();
                    for (int i = 0; i <= (placesSearchResults.length) -1; i++){
//                        String placeLoc = placesSearchResults[i].name;
//                        String urlPhoto = placesSearchResults[i].photos. ();








//                        // Define a Place ID.
//                        final String placeId = placesSearchResults[i].placeId;
//
//                        // Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
//                        final List<Place.Field> fields = Collections.singletonList(Place.Field.PHOTO_METADATAS);
//
//                        // Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
//                        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, fields);
//
//                        PlacesClient placesClient = null;
//
////                        assert false;
//                        placesClient.fetchPlace(placeRequest).addOnSuccessListener((response) -> {
//                            final Place place = response.getPlace();
//
//                            // Get the photo metadata.
//                            final List<PhotoMetadata> metadata = place.getPhotoMetadatas();
//                            if (metadata == null || metadata.isEmpty()) {
//                                Log.w(TAG, "No photo metadata.");
//                                return;
//                            }
//                            final PhotoMetadata photoMetadata = metadata.get(0);
//
//                            // Get the attribution text.
//                            final String attributions = photoMetadata.getAttributions();
//
//                            // Create a FetchPhotoRequest.
//                            final FetchPhotoRequest photoRequest = FetchPhotoRequest.builder(photoMetadata)
//                                    .setMaxWidth(500) // Optional.
//                                    .setMaxHeight(300) // Optional.
//                                    .build();
//                            placesClient.fetchPhoto(photoRequest).addOnSuccessListener((fetchPhotoResponse) -> {
//                                Bitmap bitmap = fetchPhotoResponse.getBitmap();
////                                imageView.setImageBitmap(bitmap);
//                            }).addOnFailureListener((exception) -> {
//                                exception.printStackTrace();
////                                responseView.setText(exception.getMessage());
////                                if (exception instanceof ApiException) {
////                                    final ApiException apiException = (ApiException) exception;
////                                    Log.e(TAG, "Place not found: " + exception.getMessage());
////                                    final int statusCode = apiException.getStatusCode();
////                                    // TODO: Handle error with given status code.
////                                }
//                            });
//                        });

///**
// * Load a bitmap from the photos API asynchronously
// * by using buffers and result callbacks.
// */
//
//                        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, placesSearchResults[i].placeId)
//                                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {
//
//
//                                    @Override
//                                    public void onResult(PlacePhotoMetadataResult photos) {
//                                        if (!photos.getStatus().isSuccess()) {
//                                            Log.d(TAG, "Couldn\'t receive photos bundle successfully.");
//                                            return;
//                                        }
//
//                                        Log.d(TAG, "Photo bundle received successfully");
//
//                                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();
//                                        if (photoMetadataBuffer.getCount() > 0) {
//                                            // Display the first bitmap in an ImageView in the size of the view
//                                            photoMetadataBuffer.get(0)
//                                                    .getScaledPhoto(mGoogleApiClient, imageView.getWidth(),
//                                                            imageView.getHeight())
//                                                    .setResultCallback(new ResultCallback<PlacePhotoResult>() {
//                                                        @Override
//                                                        public void onResult(PlacePhotoResult placePhotoResult) {
//                                                            if (!placePhotoResult.getStatus().isSuccess()) {
//                                                                Log.d(TAG, "Couldn\'t retrieve the photo successfully.");
//                                                                return;
//                                                            }
//
//                                                            Log.d(TAG, "Successfully retrieved photo from photo bundle.");
//
//                                                            imageView.setImageBitmap(placePhotoResult.getBitmap());
//                                                        }
//                                                    });
//                                        } else {
//                                            Log.d(TAG, "0 images in the buffer.");
//                                        }
//                                        photoMetadataBuffer.release();
//                                    }
//                                });












                        RestaurantDetails restaurantDetails = new RestaurantDetails(placesSearchResults[i].placeId,placesSearchResults[i].name,placesSearchResults[i].vicinity, placesSearchResults[i].photos[0], placesSearchResults[i].photos[0].photoReference, placesSearchResults[i].icon.toString());

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