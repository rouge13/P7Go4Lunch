package com.julienhammer.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class Workmate implements Parcelable {
        private String wkmId;
        private String wkmName;
        private String wkmEmail;
        private String wkmPlaceId;
        private String wkmPhotoUrl;

    protected Workmate(Parcel in) {
        wkmId = in.readString();
        wkmName = in.readString();
        wkmEmail = in.readString();
        wkmPlaceId = in.readString();
        wkmPhotoUrl = in.readString();
    }

    public static final Creator<Workmate> CREATOR = new Creator<Workmate>() {
        @Override
        public Workmate createFromParcel(Parcel in) {
            return new Workmate(in);
        }

        @Override
        public Workmate[] newArray(int size) {
            return new Workmate[size];
        }
    };

    public String getWkmId() {
        return wkmId;
    }

    public void setWkmId(String wkmId) {
        this.wkmId = wkmId;
    }

    public String getWkmName() {
        return wkmName;
    }

    public void setWkmName(String wkmName) {
        this.wkmName = wkmName;
    }

    public String getWkmEmail() {
        return wkmEmail;
    }

    public void setWkmEmail(String wkmEmail) {
        this.wkmEmail = wkmEmail;
    }

    public String getWkmPlaceId() {
        return wkmPlaceId;
    }

    public void setWkmPlaceId(String wkmPlaceId) {
        this.wkmPlaceId = wkmPlaceId;
    }

    public String getWkmPhotoUrl() {
        return wkmPhotoUrl;
    }

    public void setWkmPhotoUrl(String wkmPhotoUrl) {
        this.wkmPhotoUrl = wkmPhotoUrl;
    }

    public Workmate(String wkmId, String wkmName, String wkmEmail, String wkmPlaceId, String wkmPhotoUrl) {
        this.wkmId = wkmId;
        this.wkmName = wkmName;
        this.wkmEmail = wkmEmail;
        this.wkmPlaceId = wkmPlaceId;
        this.wkmPhotoUrl = wkmPhotoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(wkmId);
        dest.writeString(wkmName);
        dest.writeString(wkmEmail);
        dest.writeString(wkmPlaceId);
        dest.writeString(wkmPhotoUrl);
    }
}


