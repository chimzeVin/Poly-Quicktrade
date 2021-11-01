package com.example.polyquicktrade;

import android.util.Log;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.pojo.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BrowseCategoriesViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Product>> categoryProducts;

    public BrowseCategoriesViewModel() {
        categoryProducts = new MutableLiveData<>();
    }
    public void getEnquiryProducts(Enquiry enquiry) {

        final ArrayList<Product> products = new ArrayList<>();

        if (enquiry.getProductResponses() != null && enquiry.getProductResponses().size()>0) {
            for (String productID : enquiry.getProductResponses()) {


                FirebaseFirestore.getInstance().collectionGroup("product_documents").whereEqualTo("id", productID)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if (task.isSuccessful()) {


                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {

                                products.add(document.toObject(Product.class));


                            }
                            categoryProducts.setValue(products);

                        } else {
                            Log.d("MY_TAG", "wishlistViewModel not succesful: ");

                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("MY_TAG", "Message: " + e.getMessage());
                    }
                });

                //END OF FOR LOOP
            }
        }



    }
    public void getCategoryProductsFromFirebase(FirebaseFirestore db, String category){


        final ArrayList<Product> products = new ArrayList<>();
        db.collectionGroup("product_documents").whereEqualTo("category", category)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()){

                    for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                        Log.d("MY_TAG", document.getId() + " => " + document.getData());
                        products.add(document.toObject(Product.class));


                        categoryProducts.setValue(products);
                    }

                }
                else {
                    Log.d("MY_TAG", "BrowseCategoryViewModel not succesful: ");

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d("MY_TAG", "Message: "+e.getMessage());


            }
        });


    }

    public MutableLiveData<ArrayList<Product>> getCategoryProducts() {
        return categoryProducts;
    }
}
