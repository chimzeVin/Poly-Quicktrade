package com.example.polyquicktrade.ui.browse

import android.util.Log
import com.example.polyquicktrade.pojo.Product.seller
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.pojo.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*

class BrowseViewModel : ViewModel() {
    val productsMutableLiveData: MutableLiveData<ArrayList<Product>>
    val userMutableLiveData: MutableLiveData<ArrayList<User>>
    private val users: ArrayList<User>
    fun getProductsFromFirebase(db: FirebaseFirestore) {

//        CollectionReference collRef = db
//                .collection("Products")
//                .document("Other")
//                .collection("2")
//                .document("1")
//                .collection("3")
//                ;
        val products = ArrayList<Product>()
        db.collectionGroup("product_documents").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val sellerIDs: MutableSet<String> = HashSet()
                for (document in Objects.requireNonNull(task.result)) {
                    Log.d("MY_TAG", document.id + " => " + document.data)
                    products.add(document.toObject(Product::class.java))
                    sellerIDs.add(document.toObject(Product::class.java).seller)
                    productsMutableLiveData.value = products
                }
                for (id in sellerIDs) {
                    getSellerFromFirebase(id)
                }
            } else {
                Log.d("MY_TAG", "BrowseViewModel not succesful: ")
            }
        }.addOnFailureListener { e ->
            Log.d("MY_TAG", "Message: " + e.message)
            Log.d("MY_TAG", "eString: $e")
        }


//        DocumentReference newProdRef = db
//                .collection("Products")
//                .document(product.getCategory())
//                .collection(month+"")
//                .document(week+"")
//                .collection(day + "")
//                .document();

//        collRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
////                City city = documentSnapshot.toObject(City.class);
//            }
//        });

//        return productsMutableLiveData;
    }

    fun getSellerFromFirebase(userID: String) {
        val db = FirebaseDatabase.getInstance().reference
        Log.d("MY_TAG", "getSellerFromFirebase: $userID")
        val userRef = db.child("users").child(userID)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    val id = dataSnapshot.child("uid").value as String?
                    val username = dataSnapshot.child("userName").value as String?
                    val pictureURI = dataSnapshot.child("pictureURI").value as String?
                    val joinDate = dataSnapshot.child("joinDate").value as Long
                    val user = User(
                        id!!, username!!, pictureURI!!, ArrayList(), ArrayList(), joinDate
                    )
                    users.add(user)
                    userMutableLiveData.value = users


//                    Log.d("MY_TAG", "onDataChange: uid = " + dataSnapshot.child("uid").getValue());
//                        Log.d("MY_TAG", "onDataChange: userName = " + dataSnapshot.child("userName").getValue());
//                    Log.d("MY_TAG", "onDataChange: pictureURI = " + dataSnapshot.child("pictureURI").getValue() );
//                    Log.d("MY_TAG", "onDataChange: cartProducts = " + dataSnapshot.child("cartProducts").getValue() );
//                    Log.d("MY_TAG", "onDataChange: wishlistProducts = " + dataSnapshot.child("wishlistProducts").getValue() );
//                    Log.d("MY_TAG", "onDataChange: joinDate = " + dataSnapshot.child("joinDate").getValue() );
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    init {
        productsMutableLiveData = MutableLiveData()
        userMutableLiveData = MutableLiveData()
        users = ArrayList()
    }
}