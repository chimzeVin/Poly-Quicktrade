package com.example.polyquicktrade.ui.wishlist;

import android.util.Log;

import com.example.polyquicktrade.pojo.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import static com.google.firebase.database.FirebaseDatabase.getInstance;

public class WishlistViewModel extends ViewModel {


    private final ArrayList<String> wishlistProducts;
    private MutableLiveData<ArrayList<Product>> productMutableLiveData;

    public MutableLiveData<ArrayList<Product>> getProductMutableLiveData() {
        return productMutableLiveData;
    }

    public WishlistViewModel() {

        productMutableLiveData = new MutableLiveData<>();
        wishlistProducts = new ArrayList<>();
    }

    public void getWishlistFromFirebase(final FirebaseUser user){

        DatabaseReference wishlistRef = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).child("wishlistProducts");


        wishlistRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    wishlistProducts.add(postSnapshot.getValue(String.class));
                }

                getWishlistProductIDs(user);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void getWishlistProductIDs(FirebaseUser user) {

        final ArrayList<Product> products = new ArrayList<>();

        for (String productID : wishlistProducts) {


            FirebaseFirestore.getInstance().collectionGroup("product_documents").whereEqualTo("id", productID)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {

                    if (task.isSuccessful()){


                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                            products.add(document.toObject(Product.class));



                        }
                        productMutableLiveData.setValue(products);

                    }
                    else {
                        Log.d("MY_TAG", "wishlistViewModel not succesful: ");

                    }

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d("MY_TAG", "Message: "+e.getMessage());
                }
            });

            //END OF FOR LOOP
        }



    }
}