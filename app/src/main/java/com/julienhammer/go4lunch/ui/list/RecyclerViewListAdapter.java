package com.julienhammer.go4lunch.ui.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;

import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.SharedRestaurantSelectedViewModel;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListViewHolder> {
    ArrayList<RestaurantDetails> mRestaurantArrayList = new ArrayList<>();
    InfoRestaurantViewModel mInfoRestaurantViewModel;
    MainActivity mainActivity;
    private SharedRestaurantSelectedViewModel mSharedRestaurantSelectedViewModel;
    private LatLng mLocation;

    public RecyclerViewListAdapter(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public RecyclerViewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewListViewHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Context context = holder.itemView.getContext();
        initViewModel(context);
        RestaurantDetails restaurantDetails = mRestaurantArrayList.get(position);
        holder.bindingItemPlace.textViewName.setText(restaurantDetails.getNameRes());
        holder.bindingItemPlace.textViewAddress.setText(restaurantDetails.getAddressRes());
        ConvertToImage.loadGooglePhoto(context, holder.bindingItemPlace.imagePlaceViewPhoto, restaurantDetails.getPhotoRefRes());
        holder.bindingItemPlace.textViewOpeningHours.setText(restaurantDetails.getOpenNowRes());
        mInfoRestaurantViewModel.getCountWorkmatesForRestaurant(restaurantDetails.getIdRes()).observe((MainActivity) context, integer -> {
            initNumberWorkmateForEachRestaurant(holder, context, restaurantDetails, integer);
        });
        initStarsRating(holder, View.INVISIBLE);
        mInfoRestaurantViewModel.casesOfStars(restaurantDetails.getRatingRes()).observe((LifecycleOwner) context, nb -> {
            getStarsRating(holder, nb);
        });
        mInfoRestaurantViewModel.distanceFromLocation(mLocation, restaurantDetails.getLocationRes()).observe((LifecycleOwner) context, distance -> {
            holder.bindingItemPlace.textRestaurantDist.setText(String.format(Locale.getDefault(), "%dm", distance));
        });
        holder.itemView.setOnClickListener(v -> {
            mSharedRestaurantSelectedViewModel.initSelectedRestaurant(restaurantDetails);
            mainActivity.onInfoRestaurantSelected();
        });
    }

    private void initStarsRating(@NonNull RecyclerViewListViewHolder holder, int invisible) {
        holder.bindingItemPlace.imageRatingIconOne.setVisibility(invisible);
        holder.bindingItemPlace.imageRatingIconTwo.setVisibility(invisible);
        holder.bindingItemPlace.imageRatingIconThree.setVisibility(invisible);
    }

    private void initNumberWorkmateForEachRestaurant(@NonNull RecyclerViewListViewHolder holder, Context context, RestaurantDetails restaurantDetails, Integer integer) {
        if (integer > 0 && !Objects.equals(restaurantDetails.getIdRes(), "")) {
            holder.bindingItemPlace.textRestaurantNumberUserChoice.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageUserIcon.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.textRestaurantNumberUserChoice.setText(String.format(Locale.getDefault(), "(%d)", integer));
            holder.bindingItemPlace.imageUserIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_perm_identity_24));
        } else {
            holder.bindingItemPlace.textRestaurantNumberUserChoice.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageUserIcon.setVisibility(View.INVISIBLE);
        }
    }

    private void getStarsRating(@NonNull RecyclerViewListViewHolder holder, Integer nb) {
        if (Objects.equals(nb, 1)) {
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
        } else if (Objects.equals(nb, 2)) {
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.VISIBLE);
        } else if (Objects.equals(nb, 3)) {
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.VISIBLE);
        } else {
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.INVISIBLE);
        }
    }


    private void initViewModel(Context context) {
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel =
                new ViewModelProvider((FragmentActivity) context, infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
        ViewModelFactory sharedRestaurantSelectedViewModelFactory = ViewModelFactory.getInstance();
        mSharedRestaurantSelectedViewModel =
                new ViewModelProvider(mainActivity, sharedRestaurantSelectedViewModelFactory).get(SharedRestaurantSelectedViewModel.class);
    }

    @Override
    public int getItemCount() {
        return mRestaurantArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<RestaurantDetails> restaurantArrayList) {
        this.mRestaurantArrayList = restaurantArrayList;
        notifyDataSetChanged();
    }

    public void setLocation(LatLng location) {
        this.mLocation = location;
        notifyDataSetChanged();
    }
}