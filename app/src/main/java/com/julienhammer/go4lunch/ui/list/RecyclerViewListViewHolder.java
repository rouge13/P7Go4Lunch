package com.julienhammer.go4lunch.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.FragmentListBinding;
import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewListViewHolder extends RecyclerView.ViewHolder {
    ItemPlaceBinding bindingItemPlace;
    public RecyclerViewListViewHolder(@NonNull ItemPlaceBinding binding) {
        super(binding.getRoot());
        bindingItemPlace = binding;
    }
}
