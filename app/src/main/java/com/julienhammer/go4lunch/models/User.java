package com.julienhammer.go4lunch.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

/**
 * Created by Julien HAMMER - Apprenti Java with openclassrooms on .
 */
public class User implements Parcelable {
    private String userId;
    private String userName;
    private String userEmail;
    private String userPlaceId;
    private String userPhotoUrl;

    public String getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(String userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userFirstName) {
        this.userName = userFirstName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPlaceId() {
        return userPlaceId;
    }

    public void setUserPlaceId(String userPlaceId) {
        this.userPlaceId = userPlaceId;
    }

    public User(String userId, String userName, String userEmail, String userPlaceId, String userPhotoUrl) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPlaceId = userPlaceId;
        this.userPhotoUrl = userPhotoUrl;
    }

    public User(){

    }

    protected User(Parcel in) {
        userId = in.readString();
        userName = in.readString();
        userEmail = in.readString();
        userPlaceId = in.readString();
        userPhotoUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userName);
        dest.writeString(userEmail);
        dest.writeString(userPlaceId);
        dest.writeString(userPhotoUrl);

    }


}
