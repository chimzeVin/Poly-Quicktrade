package com.example.polyquicktrade

import android.util.Log
import com.example.polyquicktrade.pojo.Enquiry.productResponses
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.pojo.Enquiry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.android.gms.tasks.OnFailureListener
import java.util.*

class BrowseCategoriesViewModel : ViewModel() {
    val categoryProducts: MutableLiveData<ArrayList<Product>>
    fun getEnquiryProducts(enquiry: Enquiry) {
        val products = ArrayList<Product>()
        if (enquiry.productResponses != null && enquiry.productResponses!!.size > 0) {
            for (productID in enquiry.productResponses!!) {
                FirebaseFirestore.getInstance().collectionGroup("product_documents")
                    .whereEqualTo("id", productID)
                    .get().addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in Objects.requireNonNull(task.result)) {
                                products.add(document.toObject(Product::class.java))
                            }
                            categoryProducts.setValue(products)
                        } else {
                            Log.d("MY_TAG", "wishlistViewModel not succesful: ")
                        }
                    }.addOnFailureListener { e -> Log.d("MY_TAG", "Message: " + e.message) }

                //END OF FOR LOOP
            }
        }
    }

    fun getCategoryProductsFromFirebase(db: FirebaseFirestore, category: String?) {
        val products = ArrayList<Product>()
        db.collectionGroup("product_documents").whereEqualTo("category", category)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in Objects.requireNonNull(task.result)) {
                        Log.d("MY_TAG", document.id + " => " + document.data)
                        products.add(document.toObject(Product::class.java))
                        categoryProducts.value = products
                    }
                } else {
                    Log.d("MY_TAG", "BrowseCategoryViewModel not succesful: ")
                }
            }.addOnFailureListener { e -> Log.d("MY_TAG", "Message: " + e.message) }
    }

    init {
        categoryProducts = MutableLiveData()
    }
}