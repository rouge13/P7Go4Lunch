package com.julienhammer.go4lunch.ui.list;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;
import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.events.ShowInfoRestaurantDetailEvent;
import com.julienhammer.go4lunch.models.RestaurantDetails;

import com.julienhammer.go4lunch.models.User;
import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListViewHolder> {
    ArrayList<RestaurantDetails> mRestaurantArrayList = new ArrayList<>();
    InfoRestaurantViewModel mInfoRestaurantViewModel;
    LatLng userLatLngLocation;
    public RecyclerViewListAdapter(LatLng userLocation) {
        this.userLatLngLocation = userLocation;
    }

    @NonNull
    @Override
    public RecyclerViewListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewListViewHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewListViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Context context = holder.itemView.getContext();
        initInfoRestaurantViewModel(context);
        RestaurantDetails restaurantDetails = mRestaurantArrayList.get(position);
        holder.bindingItemPlace.textViewName.setText(restaurantDetails.getNameRes());
        holder.bindingItemPlace.textViewAddress.setText(restaurantDetails.getAddressRes());
        ConvertToImage.loadGooglePhoto(context, holder.bindingItemPlace.imagePlaceViewPhoto, restaurantDetails.getPhotoRefRes());
        holder.bindingItemPlace.textViewOpeningHours.setText(restaurantDetails.getOpenNowRes());

        mInfoRestaurantViewModel.getCountWorkmatesForRestaurant(restaurantDetails.getIdRes()).observe((MainActivity) context, integer -> {
            if (integer > 0 && !Objects.equals(restaurantDetails.getIdRes(), "")) {
                holder.bindingItemPlace.textRestaurantNumberUserChoice.setVisibility(View.VISIBLE);
                holder.bindingItemPlace.imageUserIcon.setVisibility(View.VISIBLE);
                holder.bindingItemPlace.textRestaurantNumberUserChoice.setText(String.format(Locale.getDefault(),"(%d)", integer));
                holder.bindingItemPlace.imageUserIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_perm_identity_24));
            } else {
                holder.bindingItemPlace.textRestaurantNumberUserChoice.setVisibility(View.INVISIBLE);
                holder.bindingItemPlace.imageUserIcon.setVisibility(View.INVISIBLE);
            }
        });

        if (restaurantDetails.getRatingRes() > 1 && restaurantDetails.getRatingRes() < 2.5){
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.INVISIBLE);
        }
        else if (restaurantDetails.getRatingRes() >= 2.5 && restaurantDetails.getRatingRes() < 4){
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.INVISIBLE);
        }
        else if (restaurantDetails.getRatingRes() >= 4){
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.VISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.VISIBLE);
        } else {
            holder.bindingItemPlace.imageRatingIconOne.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageRatingIconTwo.setVisibility(View.INVISIBLE);
            holder.bindingItemPlace.imageRatingIconThree.setVisibility(View.INVISIBLE);
        }
        double distance = SphericalUtil.computeDistanceBetween(restaurantDetails.getLocationRes(), this.userLatLngLocation);
        int distanceArrondi = (int) Math.round(distance);
        holder.bindingItemPlace.textRestaurantDist.setText(String.format(Locale.getDefault(),"%dm", distanceArrondi));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoRestaurantViewModel.setInfoRestaurant(restaurantDetails);
                EventBus.getDefault().post(new ShowInfoRestaurantDetailEvent(restaurantDetails));
            }
        });
    }



    private void initInfoRestaurantViewModel(Context context) {
        ViewModelFactory infoRestaurantViewModelFactory = ViewModelFactory.getInstance();
        mInfoRestaurantViewModel =
                new ViewModelProvider((FragmentActivity) context, infoRestaurantViewModelFactory).get(InfoRestaurantViewModel.class);
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
}