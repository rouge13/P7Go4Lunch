package com.julienhammer.go4lunch.ui;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.julienhammer.go4lunch.utils.ConvertToImage;

import java.net.URL;
import java.util.ArrayList;

import okhttp3.internal.http2.Http2Connection;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListAdapter extends RecyclerView.Adapter<RecyclerViewViewHolder> {
    ArrayList<RestaurantDetails> restaurantArrayList = new ArrayList<>();


    @NonNull
    @Override
    public RecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View rootListView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_place,parent,false);
//        return new RecyclerViewViewHolder(rootListView);
        return new RecyclerViewViewHolder(ItemPlaceBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));




    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        RestaurantDetails restaurantDetails = restaurantArrayList.get(position);
        holder.bindingItemPlace.textViewName.setText(restaurantDetails.getNameRes());
        holder.bindingItemPlace.textViewAddress.setText(restaurantDetails.getAddressRes());
        if (restaurantDetails.getphotoRes() != null){
            ConvertToImage.loadGooglePhoto(context, holder.bindingItemPlace.imagePlaceViewPhoto, restaurantDetails.getphotoRes().photoReference);
        } else {
            ConvertToImage.loadIcon(context, holder.bindingItemPlace.imagePlaceViewPhoto, restaurantDetails.getIconRes());
        }
        holder.bindingItemPlace.textViewOpeningHours.setText(restaurantDetails.getOpenNowRes());
//        holder.isRecyclable();
//        notifyItemChanged(position, restaurantArrayList.size());
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

//    public ArrayList<RestaurantDetails> getDataList() {
//        if (restaurantArrayList == null)
//            restaurantArrayList = new ArrayList<>();
//        return restaurantArrayList;
//    }
//
//
//    public void remove(int position) {
//        if (position > 0 && position < getDataList().size()) {
//            getDataList().remove(position);
//            notifyItemRemoved(position);
//        }
//    }
//
//    public void removeAll() {
//        int oldSize = restaurantArrayList.size();
//        restaurantArrayList.clear();
//        notifyItemRangeRemoved(0, oldSize);
//    }
//
//    public void add(ArrayList<RestaurantDetails> newList) {
//        if (newList != null && !newList.isEmpty()) {
//            int oldSize = restaurantArrayList.size();
//            restaurantArrayList.addAll(newList);
//            notifyItemRangeChanged(oldSize - 1, newList.size());
//        }
//    }
//
//    public void add(RestaurantDetails restaurantDetails, int position) {
//        if (position > 0 && position <= restaurantArrayList.size()) {
//            restaurantArrayList.add(restaurantDetails);
//            notifyItemInserted(position);
//        }
//    }
//
//    public void notifyItemRangeChanged() {
//        notifyItemRangeChanged(0, restaurantArrayList.size());
//    }


}