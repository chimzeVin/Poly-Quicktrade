package com.example.polyquicktrade.ui.browse;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.pojo.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BrowseViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Product>> productsMutableLiveData;
    private MutableLiveData<ArrayList<User>> userMutableLiveData;
    private ArrayList<User> users ;

    public BrowseViewModel() {
        productsMutableLiveData = new MutableLiveData<>();
        userMutableLiveData = new MutableLiveData<>();
        users = new ArrayList<>();

    }

    public MutableLiveData<ArrayList<Product>> getProductsMutableLiveData() {
        return productsMutableLiveData;
    }


    public MutableLiveData<ArrayList<User>> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void getProductsFromFirebase(FirebaseFirestore db){

//        CollectionReference collRef = db
//                .collection("Products")
//                .document("Other")
//                .collection("2")
//                .document("1")
//                .collection("3")
//                ;

        final ArrayList<Product> products = new ArrayList<>();

        db.collectionGroup("product_documents").get().addOnCompleteListener(task -> {

            if (task.isSuccessful()){

                Set<String> sellerIDs= new HashSet<>();


                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Log.d("MY_TAG", document.getId() + " => " + document.getData());
                    products.add(document.toObject(Product.class));

                    sellerIDs.add(document.toObject(Product.class).getSeller());

                    productsMutableLiveData.setValue(products);
                }

                for (String id : sellerIDs) {

                    getSellerFromFirebase(id);
                }



            }
            else {
                Log.d("MY_TAG", "BrowseViewModel not successful: ");

            }
        }).addOnFailureListener(e -> {

            Log.d("MY_TAG", "Message: "+e.getMessage());
            Log.d("MY_TAG", "eString: "+ e.toString());


        });




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

    public void getSellerFromFirebase(final String userID){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();



        Log.d("MY_TAG", "getSellerFromFirebase: " + userID);

        DatabaseReference userRef = db.child("users").child(userID);


        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()) {

                    String id =  (String) dataSnapshot.child("uid").getValue();
                    String username = (String) dataSnapshot.child("userName").getValue();
                    String pictureURI = (String) dataSnapshot.child("pictureURI").getValue();
                    long joinDate = (long) dataSnapshot.child("joinDate").getValue();
                    User user = new User(id, username, pictureURI, new ArrayList<String>(), new ArrayList<String>(), joinDate);
                    users.add(user);

                    Log.d("MY_TAG", "getSellerFromFirebase: value set");
                    userMutableLiveData.setValue(users);


//                    Log.d("MY_TAG", "onDataChange: uid = " + dataSnapshot.child("uid").getValue());
//                        Log.d("MY_TAG", "onDataChange: userName = " + dataSnapshot.child("userName").getValue());
//                    Log.d("MY_TAG", "onDataChange: pictureURI = " + dataSnapshot.child("pictureURI").getValue() );
//                    Log.d("MY_TAG", "onDataChange: cartProducts = " + dataSnapshot.child("cartProducts").getValue() );
//                    Log.d("MY_TAG", "onDataChange: wishlistProducts = " + dataSnapshot.child("wishlistProducts").getValue() );
//                    Log.d("MY_TAG", "onDataChange: joinDate = " + dataSnapshot.child("joinDate").getValue() );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("MY_TAG", "getSellerFromFirebase: cancelled");

            }
        });

    }


}