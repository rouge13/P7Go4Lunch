package com.julienhammer.go4lunch.models;

import android.graphics.Bitmap;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.maps.model.Photo;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantDetails implements Parcelable {
    private String idRes;
    private String nameRes;
    private String addressRes;
    private Photo photoRes;
    private String iconRes;
    private String openNowRes;
    private String photoRefRes;

    public RestaurantDetails(Parcel in) {
        idRes = in.readString();
        nameRes = in.readString();
        addressRes = in.readString();
        iconRes = in.readString();
        openNowRes = in.readString();
        photoRefRes = in.readString();
    }

    public static final Creator<RestaurantDetails> CREATOR = new Creator<RestaurantDetails>() {
        @Override
        public RestaurantDetails createFromParcel(Parcel in) {
            return new RestaurantDetails(in);
        }

        @Override
        public RestaurantDetails[] newArray(int size) {
            return new RestaurantDetails[size];
        }
    };

    public RestaurantDetails(String id, String name, String address, String photoRefRes, String iconRes, String openNow, Photo photoRes) {
        this.idRes = id;
        this.nameRes = name;
        this.addressRes = address;
        this.photoRes = photoRes;
        this.photoRefRes = photoRefRes;
        this.iconRes = iconRes;
        this.openNowRes = openNow;

    }

    public String getIdRes() {
        return idRes;
    }

    public void setIdRes(String idRes) { this.idRes = idRes; }

    public String getNameRes() {
        return nameRes;
    }

    public void setNameRes(String nameRes) {
        this.nameRes = nameRes;
    }

    public String getAddressRes() {
        return addressRes;
    }

    public void setAddressRes(String addressRes) {
        this.addressRes = addressRes;
    }

    public Photo getphotoRes() { return photoRes; }

    public void setphotoRes(Photo photoRes) { this.photoRes = photoRes; }
//
    public String getIconRes() { return iconRes; }

    public void setIconRes(String iconRes) { this.iconRes = iconRes; }

    public String getOpenNowRes() { return openNowRes; }

    public void setOpenNowRes(String photoRes) { this.openNowRes = openNowRes; }

    public String getPhotoRefRes() {
        return photoRefRes;
    }

    public void setPhotoRefRes(String photoRefRes) {
        this.photoRefRes = photoRefRes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idRes);
        dest.writeString(nameRes);
        dest.writeString(addressRes);
        dest.writeString(iconRes);
        dest.writeParcelable((Parcelable) photoRes, flags);
        dest.writeString(openNowRes);
        dest.writeString(photoRefRes);
    }
}
