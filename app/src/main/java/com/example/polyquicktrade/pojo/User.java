package com.example.polyquicktrade.pojo;

import java.util.ArrayList;
import java.util.List;


public class User {

    private String uid;
    private String userName;
    private String pictureURI;
    private List<String> cartProducts;
    private List<String> wishlistProducts;
    private long joinDate;

    public User() {
        uid = "";
        userName = "";
        pictureURI = "";
        cartProducts = new ArrayList<>();
        wishlistProducts = new ArrayList<>();
        joinDate = 0L;
    }

    public User(String uid, String userName, String pictureURI, List<String> cartProducts, List<String> wishlistProducts, long joinDate) {
        this.uid = uid;
        this.userName = userName;
        this.pictureURI = pictureURI;
        this.cartProducts = cartProducts;
        this.wishlistProducts = wishlistProducts;
        this.joinDate = joinDate;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPictureURI() {
        return pictureURI;
    }

    public void setPictureURI(String pictureURI) {
        this.pictureURI = pictureURI;
    }

    public List<String> getCartProducts() {
        return cartProducts;
    }

    public void setCartProducts(List<String> cartProducts) {
        this.cartProducts = cartProducts;
    }

    public List<String> getWishlistProducts() {
        return wishlistProducts;
    }

    public void setWishlistProducts(List<String> wishlistProducts) {
        this.wishlistProducts = wishlistProducts;
    }

    public long getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(long joinDate) {
        this.joinDate = joinDate;
    }
}
