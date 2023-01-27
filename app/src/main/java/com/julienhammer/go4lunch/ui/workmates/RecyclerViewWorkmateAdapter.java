package com.julienhammer.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ItemWorkmatesBinding;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.utils.ConvertToImage;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewWorkmateAdapter extends RecyclerView.Adapter<RecyclerViewWorkmateViewHolder> {
    private final PlacesClient placesClient;
    ArrayList<User> workmatesArrayList = new ArrayList<>();
    ArrayList<String> restaurantsPlaceName = new ArrayList<>();
    boolean displayFargmentInfoRestaurant;

    public RecyclerViewWorkmateAdapter(boolean displayFragmentInfo, Context context) {
        this.displayFargmentInfoRestaurant = displayFragmentInfo;
        placesClient = Places.createClient(context);
    }

    @NonNull
    @Override
    public RecyclerViewWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewWorkmateViewHolder(ItemWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWorkmateViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        User workmate = workmatesArrayList.get(position);
        ConvertToImage.loadIcon(context, holder.BindingItemWorkmate.imageWorkmateViewPhoto, workmate.getUserPhotoUrl());
        if (this.displayFargmentInfoRestaurant) {
            holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + context.getResources().getString(R.string.is_joining));
        } else {
            String restaurantPlaceName = restaurantsPlaceName.get(position);
            if (restaurantPlaceName == null || restaurantPlaceName.equals("")) {
                List<Place.Field> fields = Arrays.asList(Place.Field.NAME);
                // Construct a request object, passing the place ID and fields array.
                final FetchPlaceRequest request = FetchPlaceRequest.newInstance(workmate.getUserPlaceId(), fields);
                Task<FetchPlaceResponse> placeTask = placesClient.fetchPlace(request);
                placeTask.addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
                    @Override
                    public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                        if (fetchPlaceResponse.getPlace() != null && fetchPlaceResponse.getPlace().getName() != null && !fetchPlaceResponse.getPlace().getName().isEmpty()) {
                            holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + context.getResources().getString(R.string.is_eating_to) + fetchPlaceResponse.getPlace().getName());
                            holder.BindingItemWorkmate.textViewInformation.setOnClickListener(view -> Toast.makeText(context, workmate.getUserName() + context.getResources().getString(R.string.is_eating_to) + fetchPlaceResponse.getPlace().getName(), Toast.LENGTH_LONG).show());
                        } else {
                            holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + context.getResources().getString(R.string.not_selected));
                            holder.BindingItemWorkmate.textViewInformation.setOnClickListener(view -> Toast.makeText(context, workmate.getUserName() + context.getResources().getString(R.string.not_selected), Toast.LENGTH_LONG).show());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull @NotNull Exception e) {
                        holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + context.getResources().getString(R.string.not_selected));
                        holder.BindingItemWorkmate.textViewInformation.setOnClickListener(view -> Toast.makeText(context, workmate.getUserName() + context.getResources().getString(R.string.not_selected), Toast.LENGTH_LONG).show());
                    }
                });
            } else {
                holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + context.getResources().getString(R.string.not_selected));
                holder.BindingItemWorkmate.textViewInformation.setOnClickListener(view -> Toast.makeText(context, workmate.getUserName() + context.getResources().getString(R.string.not_selected), Toast.LENGTH_LONG).show());
            }
        }
    }

    @Override
    public int getItemCount() {
        return workmatesArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<User> workmatesArrayList, ArrayList<String> restaurantsPlaceName) {
        if (this.workmatesArrayList == null) {
            this.workmatesArrayList = new ArrayList<>();
        }
        this.workmatesArrayList.clear();
        this.restaurantsPlaceName.clear();
        this.workmatesArrayList.addAll(workmatesArrayList);
        this.restaurantsPlaceName.addAll(restaurantsPlaceName);
        notifyDataSetChanged();
    }


}

