package com.example.polyquicktrade.pojo

import java.util.ArrayList

class User {
    var uid: String
    var userName: String
    var pictureURI: String
    var cartProducts: List<String>
    var wishlistProducts: List<String>
    var joinDate: Long

    constructor() {
        uid = ""
        userName = ""
        pictureURI = ""
        cartProducts = ArrayList()
        wishlistProducts = ArrayList()
        joinDate = 0L
    }

    constructor(
        uid: String,
        userName: String,
        pictureURI: String,
        cartProducts: List<String>,
        wishlistProducts: List<String>,
        joinDate: Long
    ) {
        this.uid = uid
        this.userName = userName
        this.pictureURI = pictureURI
        this.cartProducts = cartProducts
        this.wishlistProducts = wishlistProducts
        this.joinDate = joinDate
    }
}