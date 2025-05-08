package com.app.buy.Model;

public class Medicine {
    private String name;
    private String lng;
    private String lat;
    private String country;
    private String category;
    private String details;
    private String image;
    private String price;
    private String priceGNF;
    private String quantity;
    private String pharmacy_id;
    private String pharmacy_name;

    public Medicine() {}

    public Medicine(String name, String category, String details, String image, String price, String quantity) {
        this.name = name;
        this.category = category;
        this.details = details;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getDetails() { return details; }
    public String getImage() { return image; }
    public String getPrice() { return price; }
    public String getQuantity() { return quantity; }

    public String getPriceGNF() {
        return priceGNF;
    }

    public void setPriceGNF(String priceGNF) {
        this.priceGNF = priceGNF;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public String getPharmacy_id() {
        return pharmacy_id;
    }
}

