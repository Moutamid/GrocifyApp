package com.app.buy.Model;


import java.util.List;
import java.util.Map;

import java.util.Map;

public class Villa {
    private String name;
    private String description;
    private String image;
    private double lat;
    private double lng;
    private String phone;
    private Map<String, String> images; // Stores multiple image URLs with labels

    // Constructor
    public Villa() {
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Getter and Setter for main image URL
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    // Getter and Setter for latitude
    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    // Getter and Setter for longitude
    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    // Getter and Setter for phone number
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter and Setter for image gallery
    public Map<String, String> getImages() {
        return images;
    }

    public void setImages(Map<String, String> images) {
        this.images = images;
    }
}
