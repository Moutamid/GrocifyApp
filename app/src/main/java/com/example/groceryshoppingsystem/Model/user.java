package com.example.groceryshoppingsystem.Model;

public class user {
    private String details, image, price, quantity, category;

    public user() {
    }

    public user(String details, String image, String price, String quantity) {
        this.details = details;
        this.image = image;
        this.price = price;
        this.quantity = quantity;
    }

    public String getdetails() {
        return details;
    }

    public void setdetails(String details) {
        this.details = details;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
