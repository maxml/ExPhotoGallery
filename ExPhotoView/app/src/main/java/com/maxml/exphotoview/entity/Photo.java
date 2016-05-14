package com.maxml.exphotoview.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Maxml on 14.05.2016.
 */
public class Photo {

    private String camera;
    @SerializedName("image_url")
    private String url;
    private String name;
    private User user;

    public Photo() {
    }

    public Photo(String camera, String url, String name, User user) {
        this.camera = camera;
        this.url = url;
        this.name = name;
        this.user = user;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
