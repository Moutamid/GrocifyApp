package com.app.buy.Model;

public class HorizontalProductModel {
    private String productimage;
    private String producttitle;
    private String productprice;
    private boolean checked;
    private String detailsDate;
    private String category_name;
    private String quantity;

    public HorizontalProductModel(String productimage, String producttitle, String productprice, boolean checked, String detailsDate, String category_name, String quantity) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productprice = productprice;
        this.checked = checked;
        this.detailsDate = detailsDate;
        this.category_name = category_name;
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public HorizontalProductModel(String productimage, String producttitle, String productprice, boolean checked, String detailsDate, String category_name) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productprice = productprice;
        this.checked = checked;
        this.detailsDate = detailsDate;
        this.category_name = category_name;
    }

    public HorizontalProductModel(String productimage, String producttitle, String productprice, boolean checked, String detailsDate) {
        this.productimage = productimage;
        this.producttitle = producttitle;
        this.productprice = productprice;
        this.checked = checked;
        this.detailsDate = detailsDate;
    }

    public String getdetailsDate() {
        return detailsDate;
    }

    public void setdetailsDate(String detailsDate) {
        detailsDate = detailsDate;
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

    public String getProductprice() {
        return productprice;
    }

    public void setProductprice(String productprice) {
        this.productprice = productprice;
    }

    public String getDetailsDate() {
        return detailsDate;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public void setDetailsDate(String detailsDate) {
        this.detailsDate = detailsDate;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
