package com.app.buy.Helper;

public class UserModel {
    String key;
    String name;
    String email;
    String phone;
    String image;
    String address;
    String about;
    String lat;
    String lng;
    String deviceID;
    String selectedType;
    boolean subscriptionFromGoogle, isVIP;
    String duration;
    String country;

    public UserModel() {
    }



    public UserModel(String key, String name, String phone, String email, String image, String country) {
        this.key = key;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.image = image;
        this.country = country;
    }

    public UserModel(String key, String name, String email, String phone, String image, String address, String about,
                     String lat, String lng, String deviceID, boolean subscriptionFromGoogle, boolean isVIP, String duration, String country) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.image = image;
        this.address = address;
        this.about = about;
        this.lat = lat;
        this.lng = lng;
        this.deviceID = deviceID;
        this.subscriptionFromGoogle = subscriptionFromGoogle;
        this.isVIP = isVIP;
        this.duration = duration;
        this.country = country;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getAbout() { return about; }
    public void setAbout(String about) { this.about = about; }

    public String getLat() { return lat; }
    public void setLat(String lat) { this.lat = lat; }

    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSelectedType() {
        return selectedType;
    }

    public void setSelectedType(String selectedType) {
        this.selectedType = selectedType;
    }

    public boolean isSubscriptionFromGoogle() {
        return subscriptionFromGoogle;
    }

    public void setSubscriptionFromGoogle(boolean subscriptionFromGoogle) {
        this.subscriptionFromGoogle = subscriptionFromGoogle;
    }

    public boolean isVIP() {
        return isVIP;
    }

    public void setVIP(boolean VIP) {
        isVIP = VIP;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLng() { return lng; }
    public void setLng(String lng) { this.lng = lng; }
}
