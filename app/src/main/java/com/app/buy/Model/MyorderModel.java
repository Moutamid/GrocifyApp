package com.app.buy.Model;

public class MyorderModel {
  private String OrderID, Date, orderNums, orderPrice, orderProducts, OrderCheck, orderAddress, orderEmail, orderPhone, orderName;
String token, uid, key;
int type;
String status;

    public MyorderModel(String orderID, String date, String orderNums, String orderPrice, String orderProducts, String orderCheck, String orderAddress, String orderEmail, String orderPhone, String orderName, String token, String uid, String key, int type) {
        OrderID = orderID;
        Date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProducts = orderProducts;
        OrderCheck = orderCheck;
        this.orderAddress = orderAddress;
        this.orderEmail = orderEmail;
        this.orderPhone = orderPhone;
        this.orderName = orderName;
        this.token = token;
        this.uid = uid;
        this.key = key;
        this.type = type;
    } public MyorderModel(String orderID, String date, String orderNums, String orderPrice, String orderProducts, String orderCheck, String orderAddress, String orderEmail, String orderPhone, String orderName, String token, String uid, String key, String status) {
        OrderID = orderID;
        Date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProducts = orderProducts;
        OrderCheck = orderCheck;
        this.orderAddress = orderAddress;
        this.orderEmail = orderEmail;
        this.orderPhone = orderPhone;
        this.orderName = orderName;
        this.token = token;
        this.uid = uid;
        this.key = key;
        this.status = status;
    }

    public MyorderModel(String OrderId, String date, String orderNums, String orderPrice, String orderProducts, String OrderCheck, String status) {
        this.OrderID = OrderId;
        Date = date;
        this.orderNums = orderNums;
        this.orderPrice = orderPrice;
        this.orderProducts = orderProducts;
        this.OrderCheck = OrderCheck;
        this.status = status;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getOrderNums() {
        return orderNums;
    }

    public void setOrderNums(String orderNums) {
        this.orderNums = orderNums;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(String orderProducts) {
        this.orderProducts = orderProducts;
    }

    public String getOrderCheck() {
        return OrderCheck;
    }

    public void setOrderCheck(String orderCheck) {
        OrderCheck = orderCheck;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public String getOrderEmail() {
        return orderEmail;
    }

    public void setOrderEmail(String orderEmail) {
        this.orderEmail = orderEmail;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
