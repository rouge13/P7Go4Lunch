package com.julienhammer.go4lunch.ui.workmates;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.databinding.ItemWorkmatesBinding;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewWorkmateViewHolder extends RecyclerView.ViewHolder {
    ItemWorkmatesBinding BindingItemWorkmate;
    public RecyclerViewWorkmateViewHolder(@NonNull ItemWorkmatesBinding binding) {
        super(binding.getRoot());
        BindingItemWorkmate = binding;
    }
}
