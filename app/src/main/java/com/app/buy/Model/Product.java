package com.app.buy.Model;

public class Product {
     public String quantity , price , priceGNF, details , image, category, name, pharmacy_id, pharmacy_name;
     public String lat , lng , country;


    public Product(String quantity, String price, String priceGNF, String details, String image, String category, String name, String pharmacy_id, String pharmacy_name, String lat, String lng, String country) {
        this.quantity = quantity;
        this.price = price;
        this.priceGNF = priceGNF;
        this.details = details;
        this.image = image;
        this.category = category;
        this.name = name;
        this.pharmacy_id = pharmacy_id;
        this.pharmacy_name = pharmacy_name;
        this.lat = lat;
        this.lng = lng;
        this.country = country;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceGNF() {
        return priceGNF;
    }

    public void setPriceGNF(String priceGNF) {
        this.priceGNF = priceGNF;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPharmacy_id() {
        return pharmacy_id;
    }

    public void setPharmacy_id(String pharmacy_id) {
        this.pharmacy_id = pharmacy_id;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public void setPharmacy_name(String pharmacy_name) {
        this.pharmacy_name = pharmacy_name;
    }
}
