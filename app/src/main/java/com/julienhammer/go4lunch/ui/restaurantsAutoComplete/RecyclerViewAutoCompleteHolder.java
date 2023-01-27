package com.julienhammer.go4lunch.ui.restaurantsAutoComplete;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.ItemPlaceBinding;
import com.julienhammer.go4lunch.databinding.ItemRestaurantSearchBinding;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewAutoCompleteHolder extends RecyclerView.ViewHolder {
        ItemRestaurantSearchBinding bindingItemRestaurantSearch;
        public RecyclerViewAutoCompleteHolder(@NonNull ItemRestaurantSearchBinding binding) {
            super(binding.getRoot());
            bindingItemRestaurantSearch = binding;
        }
}
