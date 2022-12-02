package com.julienhammer.go4lunch.ui.restaurantsAutoComplete;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Parcel;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.model.TypeFilter;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.databinding.ItemRestaurantSearchBinding;
import com.julienhammer.go4lunch.models.RestaurantAutoComplete;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewRestaurantsAutoCompleteAdapter extends RecyclerView.Adapter<RecyclerViewAutoCompleteHolder> implements Filterable {
    private static String MY_RESTAURANT_CHOICE_PLACE = "MyRestaurantChoicePlace";
    private static String RESTAURANT_LAT = "latRes";
    private static String RESTAURANT_LNG = "lngRes";
    private static final String TAG = "PlacesAutoAdapter";
    private ArrayList<RestaurantAutoComplete> mResultList = new ArrayList<>();
    ItemRestaurantSearchBinding binding;
    private View.OnClickListener clickListener;

    private Context mContext;
    private CharacterStyle STYLE_BOLD;
    private CharacterStyle STYLE_NORMAL;
    private final PlacesClient placesClient;

    public RecyclerViewRestaurantsAutoCompleteAdapter(Context context) {
        mContext = context;
        STYLE_BOLD = new StyleSpan(Typeface.BOLD);
        STYLE_NORMAL = new StyleSpan(Typeface.NORMAL);
        placesClient = Places.createClient(context);
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    /**
     * Returns the filter for the current set of autocomplete results.
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                // Skip the autocomplete query if no constraints are given.
                if (constraint != null) {
                    // Query the autocomplete API for the (constraint) search string.
                    mResultList = getPredictions(constraint);
                    if (mResultList != null) {
                        // The API successfully returned results.
                        results.values = mResultList;
                        results.count = mResultList.size();
                    }
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    // The API returned at least one result, update the data.
                    notifyDataSetChanged();
                } else {
                    // The API did not return any results, invalidate the data set.
                    //notifyDataSetInvalidated();
                }
            }
        };
    }


    private ArrayList<RestaurantAutoComplete> getPredictions(CharSequence constraint) {

        final ArrayList<RestaurantAutoComplete> resultList = new ArrayList<>();
        SharedPreferences prefs = mContext.getSharedPreferences(MY_RESTAURANT_CHOICE_PLACE, MODE_PRIVATE);
        LatLng userLocation;
        if (prefs.getFloat(RESTAURANT_LAT,0) != 0 && prefs.getFloat(RESTAURANT_LNG, 0) != 0){
            userLocation = new LatLng((double) prefs.getFloat(RESTAURANT_LAT,0) , (double) prefs.getFloat(RESTAURANT_LNG,0));

            double distanceFromCenterToCorner = 5000 * Math.sqrt(2.0);

            LatLng southwestCorner = SphericalUtil.computeOffset(new LatLng(userLocation.latitude, userLocation.longitude), distanceFromCenterToCorner, 225.0);
            LatLng northeastCorner = SphericalUtil.computeOffset(new LatLng(userLocation.latitude, userLocation.longitude), distanceFromCenterToCorner, 45.0);
            RectangularBounds boundUserLocation = new RectangularBounds() {
                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel parcel, int i) {
                }

                @NonNull
                @NotNull
                @Override
                public LatLng getNortheast() {
                    return northeastCorner;
                }

                @NonNull
                @NotNull
                @Override
                public LatLng getSouthwest() {
                    return southwestCorner;
                }
            };
            // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
            // and once again when the user makes a selection (for example when calling fetchPlace()).
            AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

            //https://gist.github.com/graydon/11198540
            // Use the builder to create a FindAutocompletePredictionsRequest.
            FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                    //.setLocationBias(bounds)
                    //.setCountry("BD")
                    //.setTypeFilter(TypeFilter.ADDRESS)
                    .setTypeFilter(TypeFilter.ESTABLISHMENT)
                    .setLocationRestriction(RectangularBounds.newInstance(boundUserLocation.getSouthwest(), boundUserLocation.getNortheast()))
                    .setCountries(Collections.singletonList("FR"))
                    .setSessionToken(token)
                    .setQuery(constraint.toString())
                    .build();

            Task<FindAutocompletePredictionsResponse> autocompletePredictions = placesClient.findAutocompletePredictions(request);

            // This method should have been called off the main UI thread. Block and wait for at most
            // 60s for a result from the API.
            try {
                Tasks.await(autocompletePredictions, 60, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                e.printStackTrace();
            }

            if (autocompletePredictions.isSuccessful()) {
                FindAutocompletePredictionsResponse findAutocompletePredictionsResponse = autocompletePredictions.getResult();
                if (findAutocompletePredictionsResponse != null)
                    for (AutocompletePrediction prediction : findAutocompletePredictionsResponse.getAutocompletePredictions()) {
                        Log.i(TAG, prediction.getPlaceId());
                        resultList.add(new RestaurantAutoComplete(prediction.getPlaceId(), prediction.getPrimaryText(STYLE_NORMAL).toString(), prediction.getFullText(STYLE_BOLD).toString()));
                    }

                return resultList;
            } else {
                return resultList;
            }
        } else {
            return resultList;
        }




    }

    @NonNull
    @Override
    public RecyclerViewAutoCompleteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewAutoCompleteHolder(ItemRestaurantSearchBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAutoCompleteHolder holder, int position) {
        Context context = holder.itemView.getContext();
        RestaurantAutoComplete item = mResultList.get(position);

        List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS);
        final FetchPlaceRequest request = FetchPlaceRequest.builder(item.placeId, placeFields).build();
        Task<FetchPlaceResponse> restaurantTask = placesClient.fetchPlace(request);
        restaurantTask.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse response) {
                if (response.getPlace() != null && response.getPlace().getName() != null){
                    holder.bindingItemRestaurantSearch.textViewRestaurantName.setText(response.getPlace().getName());
                    holder.bindingItemRestaurantSearch.textViewRestaurantAddress.setText(response.getPlace().getAddress());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                if (exception instanceof ApiException) {
                    Toast.makeText(mContext, exception.getMessage() + "", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        holder.bindingItemRestaurantSearch.textViewRestaurantName.setText(mResultList.get(position).placeId);
//        mPredictionHolder.address.setText(mResultList.get(i).address);
//        mPredictionHolder.area.setText(mResultList.get(i).area);
    }

    @Override
    public int getItemCount() {
        return mResultList.size();
    }


}
