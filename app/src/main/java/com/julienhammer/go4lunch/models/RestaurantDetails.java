package com.julienhammer.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantDetails {
    private String idRes;
    private String nameRes;
    private String addressRes;
    private String openNowRes;
    private String photoRefRes;
    private float ratingRes;
    private LatLng locationRes;

    public LatLng getLocationRes() {
        return locationRes;
    }

    public void setLocationRes(LatLng locationRes) {
        this.locationRes = locationRes;
    }

    public RestaurantDetails(String id, String name, String address, String photoRefRes, String openNow, float ratingRes, LatLng locationRes) {
        this.idRes = id;
        this.nameRes = name;
        this.addressRes = address;
        this.photoRefRes = photoRefRes;
        this.openNowRes = openNow;
        this.ratingRes = ratingRes;
        this.locationRes = locationRes;
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

    public String getOpenNowRes() { return openNowRes; }

    public void setOpenNowRes(String photoRes) { this.openNowRes = openNowRes; }

    public String getPhotoRefRes() {
        return photoRefRes;
    }

    public void setPhotoRefRes(String photoRefRes) {
        this.photoRefRes = photoRefRes;
    }

    public double getRatingRes() {
        return ratingRes;
    }

    public void setRatingRes(float ratingRes) {
        this.ratingRes = ratingRes;
    }

}
