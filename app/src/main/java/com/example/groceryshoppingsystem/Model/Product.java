package com.example.groceryshoppingsystem.Model;

public class Product {
    public String quantity , price , details , image, category;

    public Product(String quantity, String price, String details, String image, String category) {
        this.quantity = quantity;
        this.price = price;
        this.details = details;
        this.image = image;
        this.category = category;
    }
}
