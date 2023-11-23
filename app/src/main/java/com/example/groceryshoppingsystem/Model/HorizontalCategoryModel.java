package com.example.groceryshoppingsystem.Model;

public class HorizontalCategoryModel {
    private String productimage;
    private String producttitle;

    public HorizontalCategoryModel(String productimage, String producttitle) {
        this.productimage = productimage;
        this.producttitle = producttitle;
    }

    public String getProductimage() {
        return productimage;
    }

    public void setProductimage(String productimage) {
        this.productimage = productimage;
    }

    public String getProducttitle() {
        return producttitle;
    }

    public void setProducttitle(String producttitle) {
        this.producttitle = producttitle;
    }
}
