package com.julienhammer.go4lunch.ui.workmates;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.julienhammer.go4lunch.databinding.ItemWorkmatesBinding;
import com.julienhammer.go4lunch.models.User;
//import com.julienhammer.go4lunch.models.Workmate;
import com.julienhammer.go4lunch.utils.ConvertToImage;

import java.util.ArrayList;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RecyclerViewWorkmateAdapter extends RecyclerView.Adapter<RecyclerViewWorkmateViewHolder> {
    ArrayList<User> workmatesArrayList = new ArrayList<>();
    boolean displayFargmentInfoRestaurant;
    public RecyclerViewWorkmateAdapter(boolean displayFragmentInfo) {
        this.displayFargmentInfoRestaurant = displayFragmentInfo;
    }

    @NonNull
    @Override
    public RecyclerViewWorkmateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecyclerViewWorkmateViewHolder(ItemWorkmatesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewWorkmateViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        User workmate = workmatesArrayList.get(position);
        ConvertToImage.loadIcon(context, holder.BindingItemWorkmate.imageWorkmateViewPhoto, workmate.getUserPhotoUrl());
        if (this.displayFargmentInfoRestaurant){
            holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + " is joining!");
        } else {
            holder.BindingItemWorkmate.textViewInformation.setText(workmate.getUserName() + " is eating something somewhere.");
        }
    }

    @Override
    public int getItemCount() {
        return workmatesArrayList.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(ArrayList<User> workmatesArrayList) {
        if (this.workmatesArrayList == null){
            this.workmatesArrayList = new ArrayList<>();
        }
        this.workmatesArrayList.clear();
        if(workmatesArrayList != null){
            this.workmatesArrayList.addAll(workmatesArrayList);
        }
        notifyDataSetChanged();
    }


}

