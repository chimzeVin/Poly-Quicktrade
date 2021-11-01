package com.example.polyquicktrade.ui

import android.util.Log
import com.example.polyquicktrade.pojo.User.userName
import com.example.polyquicktrade.pojo.User.uid
import com.example.polyquicktrade.pojo.Product.id
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.pojo.User
import java.util.*

class UserAccViewModel : ViewModel() {
    val userMutableLiveData: MutableLiveData<User?>
    fun addUser(user: FirebaseUser) {
        val db = FirebaseDatabase.getInstance().reference


//        Query query = db.child("users").equalTo(user.getUid(),"uid");
        val joinDate = Date().time
        val user1 = User(
            user.uid,
            user.displayName!!,
            user.photoUrl.toString(),
            ArrayList(),
            ArrayList(),
            joinDate
        )
        Log.d("MY_TAG", "addUser: " + user1.userName)
        db.child("users").child(user1.uid).setValue(user1)
    }

    fun getSellerFromFirebase(userID: String) {
        val db = FirebaseDatabase.getInstance().reference
        Log.d("MY_TAG", "getSellerFromFirebase: $userID")
        val userRef = db.child("users").child(userID)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                User user =  dataSnapshot.getValue(User.class);

//                assert user != null;
//                if (user.getUserName() != null)
//                Log.d("MY_TAG", "onDataChange: " + user.getUserName());
                for (valueRes in dataSnapshot.children) {
                    val user = valueRes.getValue(
                        User::class.java
                    )!!
                    if (user.uid == userID) {
                        userMutableLiveData.value = user
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun addToWishlistFirebase(product: Product, user: FirebaseUser) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("users")
            .child(user.uid)
            .child("wishlistProducts")
            .push()
            .setValue(product.id)
    }

    fun addToCartFirebase(product: Product, user: FirebaseUser) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("users")
            .child(user.uid)
            .child("cartProducts")
            .push()
            .setValue(product.id)
    }

    init {
        userMutableLiveData = MutableLiveData()
    }
}