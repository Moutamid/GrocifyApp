package com.app.buy.Model;

public class PharmacyModel {
    private String email;
    private String country;
    private String phone;
    private String name;
    private String image;
    private String lat;
    private String lng;
    private String about;
    private String address;
    private String key;
    private String status;

    public PharmacyModel() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getKey() {
        return key;
    }

    public String getStatus() {
        return status;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public PharmacyModel(String email, String phone, String name, String image, String lat, String lng, String about, String address, String key) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.about = about;
        this.address = address;
        this.key = key;
    }

    public PharmacyModel(String email, String phone, String name, String image, String lat, String lng, String about, String address, String key, String status) {
        this.email = email;
        this.phone = phone;
        this.name = name;
        this.image = image;
        this.lat = lat;
        this.lng = lng;
        this.about = about;
        this.address = address;
        this.key = key;
        this.status = status;
    }

    public String getCountry() {
        return country;
    }
}
