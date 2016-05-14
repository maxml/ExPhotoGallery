package com.maxml.exphotoview.entity;

/**
 * Created by Maxml on 14.05.2016.
 */
public class PhotoPage {

    private int current_page;
    private int total_pages;
    private int total_items;
    private Photo[] photos;

    public PhotoPage() {
    }

    public PhotoPage(int current_page, Photo[] photos) {
        this.current_page = current_page;
        this.photos = photos;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public Photo[] getPhotos() {
        return photos;
    }

    public void setPhotos(Photo[] photos) {
        this.photos = photos;
    }
}
