package com.example.polyquicktrade.ui;

import android.util.Log;

import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.pojo.User;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class UserAccViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;

    public UserAccViewModel() {
        userMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void addUser(FirebaseUser user) {




        DatabaseReference db = FirebaseDatabase.getInstance().getReference();


//        Query query = db.child("users").equalTo(user.getUid(),"uid");

        long joinDate = new Date().getTime();


        User user1 =
                new User(user.getUid(),user.getDisplayName(), user.getPhotoUrl().toString(), new ArrayList<String>(), new ArrayList<String>(), joinDate);

        Log.d("MY_TAG", "addUser: " + user1.getUserName());

        db.child("users").child(user1.getUid()).setValue(user1);
    }

    public void getSellerFromFirebase(final String userID){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();


        Log.d("MY_TAG", "getSellerFromFirebase: " + userID);

        DatabaseReference userRef = db.child("users").child(userID);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                User user =  dataSnapshot.getValue(User.class);

//                assert user != null;
//                if (user.getUserName() != null)
//                Log.d("MY_TAG", "onDataChange: " + user.getUserName());
                for (DataSnapshot valueRes : dataSnapshot.getChildren()){
                    User user = valueRes.getValue(User.class);
                    assert user != null;
                    if (user.getUid().equals(userID)){
                        userMutableLiveData.setValue(user);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addToWishlistFirebase(Product product, FirebaseUser user){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users")
                .child(user.getUid())
                .child("wishlistProducts")
                .push()
                .setValue(product.getId());






    }

    public void addToCartFirebase(Product product, FirebaseUser user){
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users")
                .child(user.getUid())
                .child("cartProducts")
                .push()
                .setValue(product.getId());


    }
}
