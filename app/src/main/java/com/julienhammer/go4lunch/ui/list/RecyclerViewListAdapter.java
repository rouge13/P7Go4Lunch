package com.julienhammer.go4lunch.ui.list;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.R;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.di.ViewModelFactory;
import com.julienhammer.go4lunch.models.RestaurantDetails;

import com.julienhammer.go4lunch.ui.MainActivity;
import com.julienhammer.go4lunch.ui.list.restaurant.InfoRestaurantFragment;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.viewmodel.InfoRestaurantViewModel;
import com.julienhammer.go4lunch.viewmodel.LocationViewModel;
import com.julienhammer.go4lunch.viewmodel.RestaurantsViewModel;
import com.julienhammer.go4lunch.viewmodel.UserViewModel;

import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewListViewHolder> {
    ArrayList<RestaurantDetails> mRestaurantArrayList = new ArrayList<>();
    InfoRestaurantViewModel mInfoRestaurantViewModel;



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
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInfoRestaurantViewModel.getInfoRestaurant(mRestaurantArrayList.get(position));
                AppCompatActivity activity = (AppCompatActivity) v.getContext();

                activity.getSupportFragmentManager().beginTransaction().replace(R.id.container_layout, InfoRestaurantFragment.newInstance()).addToBackStack(null).commit();

               //               Intent intent = new Intent(v.getContext(), InfoRestaurantFragment.class);
//                android.app.Fragment fragment = null;
//                if (fragment != null){
//                    fragment.startActivity(intent);
//                }
//                context.startActivity(intent);
//                Intent intent = new Intent(v.getContext(), InfoRestaurantFragment.class);
//                intent.putExtra("Restaurants", mRestaurantArrayList.get(position));
//                v.getContext().startActivity(intent);
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