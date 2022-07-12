package com.julienhammer.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.databinding.ItemWorkmatesBinding;
import com.julienhammer.go4lunch.models.RestaurantDetails;
import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.ui.list.RecyclerViewListViewHolder;
import com.julienhammer.go4lunch.utils.ConvertToImage;
import com.julienhammer.go4lunch.R;

import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewWorkmateAdapter extends RecyclerView.Adapter<RecyclerViewWorkmateViewHolder> {
    ArrayList<Workmate> workmatesArrayList = new ArrayList<>();


    @NonNull
    @Override
    public RecyclerViewWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewWorkmateViewHolder(ItemWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWorkmateViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        Workmate workmate = workmatesArrayList.get(position);
        ConvertToImage.loadIcon(context, holder.BindingItemWorkmate.imageWorkmateViewPhoto, workmate.getWkmPhotoUrl());
        holder.BindingItemWorkmate.textViewInformation.setText(workmate.getWkmName() + " is eating something somewhere.");

    }


    @Override
    public int getItemCount() {
        return workmatesArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<Workmate> workmatesArrayList) {
        this.workmatesArrayList = workmatesArrayList;
        notifyDataSetChanged();
    }
}

