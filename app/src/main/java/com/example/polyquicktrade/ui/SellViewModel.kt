package com.example.polyquicktrade.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.android.gms.tasks.OnFailureListener
import java.util.*

class SellViewModel : ViewModel() {
    private var productMutableLiveData: MutableLiveData<ArrayList<Product>>? = null
    fun getProductMutableLiveData(): MutableLiveData<ArrayList<Product>> {
        if (productMutableLiveData == null) productMutableLiveData = MutableLiveData()
        return productMutableLiveData
    }

    fun getSellerProductsFromFirebase(db: FirebaseFirestore, id: String?) {
        val products = ArrayList<Product>()
        db.collectionGroup("product_documents")
            .whereEqualTo("seller", id).get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    for (document in Objects.requireNonNull(task.result)) {
                        Log.d("MY_TAG", document.id + " => " + document.data)
                        products.add(document.toObject(Product::class.java))
                        if (productMutableLiveData == null) productMutableLiveData =
                            MutableLiveData()
                        productMutableLiveData!!.value = products
                    }
                } else {
                    Log.d("MY_TAG", "Sell ViewModel not succesful: ")
                }
            }.addOnFailureListener { e ->
                Log.d("MY_TAG", "Message: " + e.message)
                Log.d("MY_TAG", "eString: $e")
            }
    }
}