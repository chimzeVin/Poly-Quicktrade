package com.example.polyquicktrade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.pojo.Enquiry

class SharedViewModel : ViewModel() {
    val product: MutableLiveData<Product>
    val category: MutableLiveData<String>
    val enquiry: MutableLiveData<Enquiry>
    fun setProduct(product: Product) {
        this.product.value = product
    }

    fun setCategory(item: String) {
        category.value = item
    }

    fun uploadWishlistItemToFirebase() {}
    fun setEnquiry(item: Enquiry) {
        enquiry.value = item
    }

    init {
        product = MutableLiveData()
        this.category = MutableLiveData()
        enquiry = MutableLiveData()
    }
}