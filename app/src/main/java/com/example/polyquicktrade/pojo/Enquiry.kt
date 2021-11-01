package com.example.polyquicktrade.pojo

import java.util.ArrayList

class Enquiry {
    var id: String? = null
    var productName: String? = null
    var productDesc: String? = null
    var userID: String? = null
    var creationTime: Long = 0
    var productResponses: ArrayList<String>? = null

    constructor(
        id: String?,
        productName: String?,
        productDesc: String?,
        userID: String?,
        creationTime: Long,
        productResponses: ArrayList<String>?
    ) {
        this.id = id
        this.productName = productName
        this.productDesc = productDesc
        this.userID = userID
        this.creationTime = creationTime
        this.productResponses = productResponses
    }

    constructor() {}
}