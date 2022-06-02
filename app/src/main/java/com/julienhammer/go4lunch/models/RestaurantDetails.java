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
//    private double latRes;
//    private double lonRes;
    private String photoRefRes;
//    private PhotoMetadata photoMetadataRes;
//    private double ratingRes;
//    private boolean detailedInfoRes;
    public RestaurantDetails(Parcel in) {
        idRes = in.readString();
        nameRes = in.readString();
        addressRes = in.readString();
        photoRes = in.readParcelable(Photo.class.getClassLoader());
        iconRes = in.readString();

//        latRes = in.readDouble();
//        lonRes = in.readDouble();
        photoRefRes = in.readString();
//        photoMetadataRes = in.readParcelable(PhotoMetadata.class.getClassLoader());
//        ratingRes = in.readDouble();
//        detailedInfoRes = in.readByte() != 0;
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

    public RestaurantDetails(String id, String name, String address, Photo photo, String photoRefRes, String iconRes) {
        this.idRes = id;
        this.nameRes = name;
        this.addressRes = address;
        this.photoRes = photo;
        this.photoRefRes = photoRefRes;
        this.iconRes = iconRes;
//        this.photoMetadataRes = photo;

    }

//    public RestaurantDetails(String placeId, String name, String vicinity) {
//    }


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

    public String getIconRes() { return iconRes; }

    public void setIconRes(Photo photoRes) { this.photoRes = photoRes; }

//    public double getLatRes() {
//        return latRes;
//    }
//
//    public void setLatRes(double latRes) {
//        this.latRes = latRes;
//    }
//
//    public double getLonRes() { return lonRes; }
//
//    public void setLonRes(double lonRes) { this.lonRes = lonRes; }
//
    public String getPhotoRefRes() {
        return photoRefRes;
    }

    public void setPhotoRefRes(String photoRefRes) {
        this.photoRefRes = photoRefRes;
    }

//    public PhotoMetadata getPhotoMetadataRes() {
//        return photoMetadataRes;
//    }
//
//    public void setPhotoMetadataRes(PhotoMetadata photoMetadataRes) { this.photoMetadataRes = photoMetadataRes; }
//
//    public double getRatingRes() { return ratingRes; }
//
//    public void setRatingRes(double ratingRes) { this.ratingRes = ratingRes; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idRes);
        dest.writeString(nameRes);
        dest.writeString(addressRes);
        dest.writeParcelable((Parcelable) photoRes, flags);
//        dest.writeDouble(latRes);
//        dest.writeDouble(lonRes);
        dest.writeString(photoRefRes);
//        dest.writeParcelable(photoMetadataRes, flags);
//        dest.writeDouble(ratingRes);
    }
}
