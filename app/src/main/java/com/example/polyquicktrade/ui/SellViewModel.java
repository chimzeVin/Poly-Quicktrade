package com.example.polyquicktrade.ui;

import android.util.Log;

import com.example.polyquicktrade.pojo.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SellViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Product>> productMutableLiveData;

    public SellViewModel() {
    }

    public MutableLiveData<ArrayList<Product>> getProductMutableLiveData() {
        if (productMutableLiveData ==null)
            productMutableLiveData = new MutableLiveData<>();

        return productMutableLiveData;
    }

    public void getSellerProductsFromFirebase(FirebaseFirestore db, String id){
        final ArrayList<Product> products = new ArrayList<>();
        db.collectionGroup("product_documents")
                .whereEqualTo("seller", id).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Log.d("MY_TAG", document.getId() + " => " + document.getData());
                        products.add(document.toObject(Product.class));
                        if (productMutableLiveData ==null)
                            productMutableLiveData = new MutableLiveData<>();

                        productMutableLiveData.setValue(products);
                    }

                }
                else {
                    Log.d("MY_TAG", "Sell ViewModel not succesful: ");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("MY_TAG", "Message: "+e.getMessage());
                Log.d("MY_TAG", "eString: "+ e.toString());


            }
        });


    }
}
