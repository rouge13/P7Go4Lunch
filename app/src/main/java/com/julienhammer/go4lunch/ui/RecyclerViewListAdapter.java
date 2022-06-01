package com.julienhammer.go4lunch.ui;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.places.PlacePhotoResponse;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.maps.internal.StringJoin;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.models.RestaurantDetails;

import com.julienhammer.go4lunch.R;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<RestaurantDetails> restaurantArrayList = new ArrayList<>();
    ItemPlaceBinding bindingItemPlace;

//    public RecyclerViewListAdapter(RestaurantDetails mRestaurants) {
//    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View rootListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place,parent,false);
//        return new RecyclerViewViewHolder(rootListView);
            bindingItemPlace = ItemPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new RecyclerViewViewHolder(bindingItemPlace);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            RestaurantDetails restaurantDetails = restaurantArrayList.get(position);
            bindingItemPlace.textViewName.setText(restaurantDetails.getNameRes());
            bindingItemPlace.textViewAddress.setText(restaurantDetails.getAddressRes());
//            bindingItemPlace.imagePlaceViewPhoto.setImageBitmap(restaurantDetails.getphotoRes());
//            System.out.println(position);

//        PhotoMetadata photo = restaurantDetails.getPhotoMetadataRes();

//        bindingItemPlace.imagePlaceViewPhoto.;
        //        mAddressViewRes = bindingItemPlace.textViewAddress;
//        mImageViewRes = bindingItemPlace.imagePlaceViewPhoto;
//
//        viewHolder.text_view_name.setText(user.getTitle());
//        viewHolder.txtView_description.setText(user.getDescription());

    }

    @Override
    public int getItemCount() {
        return restaurantArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<RestaurantDetails> restaurantArrayList) {
        this.restaurantArrayList = restaurantArrayList;
        notifyDataSetChanged();
    }
}