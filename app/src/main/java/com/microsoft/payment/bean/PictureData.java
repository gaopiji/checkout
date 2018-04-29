package com.microsoft.payment.bean;

import java.io.Serializable;

/**
 * Created by v-pigao on 2/28/2018.
 */

public class PictureData implements Serializable{
    String userId;
    String PictureUrl;
    boolean isLoaded;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPictureUrl() {
        return PictureUrl;
    }

    public void setPictureUrl(String pictureUrl) {
        PictureUrl = pictureUrl;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    @Override
    public String toString() {
        return "PictureData{" +
                "userId=" + userId +
                ", PictureUrl='" + PictureUrl + '\'' +
                ", isLoaded=" + isLoaded +
                '}';
    }
}
