package com.julienhammer.go4lunch.ui.list.restaurant;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.ItemWorkmatesBinding;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewInfoRestaurantViewHolder  extends RecyclerView.ViewHolder{

        ItemWorkmatesBinding bindingItemWorkmates;
        public RecyclerViewInfoRestaurantViewHolder(@NonNull ItemWorkmatesBinding binding) {
            super(binding.getRoot());
            bindingItemWorkmates = binding;
        }

}
