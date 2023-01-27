package com.julienhammer.go4lunch.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.julienhammer.go4lunch.BuildConfig;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class ConvertToImage {
    public static final String PHOTO_URL = "https://maps.googleapis.com/maps/api/place/photo?photoreference=%s&key=%s&maxheight=400";
    public static void loadGooglePhoto(Context context, ImageView imageView, String photoreference) {
        String url = String.format(PHOTO_URL, photoreference, BuildConfig.MAPS_API_KEY);
        loadIcon(context, imageView, url);
    }

    public static void loadIcon(Context context, ImageView imageView, String url) {
        if (TextUtils.isEmpty(url)) return;
        Glide.with(context).load(url).centerCrop().into(imageView);
    }

}
