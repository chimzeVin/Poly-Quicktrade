package com.example.polyquicktrade.ui.wishlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.android.gms.tasks.OnFailureListener
import java.util.*

class WishlistViewModel : ViewModel() {
    private val wishlistProducts: ArrayList<String>
    val productMutableLiveData: MutableLiveData<ArrayList<Product>>
    fun getWishlistFromFirebase(user: FirebaseUser) {
        val wishlistRef = FirebaseDatabase.getInstance().reference.child("users").child(user.uid)
            .child("wishlistProducts")
        wishlistRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (postSnapshot in dataSnapshot.children) {
                    wishlistProducts.add(postSnapshot.getValue(String::class.java)!!)
                }
                getWishlistProductIDs(user)
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun getWishlistProductIDs(user: FirebaseUser) {
        val products = ArrayList<Product>()
        for (productID in wishlistProducts) {
            FirebaseFirestore.getInstance().collectionGroup("product_documents")
                .whereEqualTo("id", productID)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        for (document in Objects.requireNonNull(task.result)) {
                            products.add(document.toObject(Product::class.java))
                        }
                        productMutableLiveData.setValue(products)
                    } else {
                        Log.d("MY_TAG", "wishlistViewModel not succesful: ")
                    }
                }.addOnFailureListener { e -> Log.d("MY_TAG", "Message: " + e.message) }

            //END OF FOR LOOP
        }
    }

    init {
        productMutableLiveData = MutableLiveData()
        wishlistProducts = ArrayList()
    }
}