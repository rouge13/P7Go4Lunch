package com.julienhammer.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.libraries.places.api.model.PhotoMetadata;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class RestaurantDetails implements Parcelable {
    private String idRes;
    private String nameRes;
    private String addressRes;
    private double latRes;
    private double lonRes;
    private String photoRefRes;
    private PhotoMetadata photoMetadataRes;
    private double ratingRes;
    private boolean detailedInfoRes;


    public RestaurantDetails(Parcel in) {
        idRes = in.readString();
        nameRes = in.readString();
        addressRes = in.readString();
        latRes = in.readDouble();
        lonRes = in.readDouble();
        photoRefRes = in.readString();
        photoMetadataRes = in.readParcelable(PhotoMetadata.class.getClassLoader());
        ratingRes = in.readDouble();
        detailedInfoRes = in.readByte() != 0;
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

    public double getLatRes() {
        return latRes;
    }

    public void setLatRes(double latRes) {
        this.latRes = latRes;
    }

    public double getLonRes() { return lonRes; }

    public void setLonRes(double lonRes) { this.lonRes = lonRes; }

    public String getPhotoRefRes() {
        return photoRefRes;
    }

    public void setPhotoRefRes(String photoRefRes) {
        this.photoRefRes = photoRefRes;
    }

    public PhotoMetadata getPhotoMetadataRes() {
        return photoMetadataRes;
    }

    public void setPhotoMetadataRes(PhotoMetadata photoMetadataRes) { this.photoMetadataRes = photoMetadataRes; }

    public double getRatingRes() { return ratingRes; }

    public void setRatingRes(double ratingRes) { this.ratingRes = ratingRes; }

    public boolean isDetailedInfoRes() { return detailedInfoRes; }

    public void setDetailedInfoRes(boolean detailedInfoRes) { this.detailedInfoRes = detailedInfoRes; }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idRes);
        dest.writeString(nameRes);
        dest.writeString(addressRes);
        dest.writeDouble(latRes);
        dest.writeDouble(lonRes);
        dest.writeString(photoRefRes);
        dest.writeParcelable(photoMetadataRes, flags);
        dest.writeDouble(ratingRes);
        dest.writeByte((byte) (detailedInfoRes ? 1 : 0));
    }
}
