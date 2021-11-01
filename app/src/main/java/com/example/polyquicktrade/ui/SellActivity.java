package com.example.polyquicktrade.ui;

import android.content.Intent;
import android.os.Bundle;

import com.example.polyquicktrade.pojo.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.polyquicktrade.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private SellAdapter sellAdapter;
    private SellViewModel sellViewModel;
    private FirebaseFirestore db;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_sell);
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        recyclerView = findViewById(R.id.SellRecyclerView);
        sellViewModel = ViewModelProviders.of(this).get(SellViewModel.class);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();

        if (user != null){

            sellViewModel.getSellerProductsFromFirebase(db, user.getUid());

            sellViewModel.getProductMutableLiveData().observe(this, new Observer<ArrayList<Product>>() {
                @Override
                public void onChanged(ArrayList<Product> products) {
                    sellAdapter = new SellAdapter(products);
                    Log.d("MY_TAG", products.get(0).toString());
                    recyclerView.setAdapter(sellAdapter);

                }
            });

        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (user != null){
                openAddProductActivity();
                }else {
                    Toast.makeText(SellActivity.this, "You are not logged in", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void openAddProductActivity() {
        Intent i = new Intent(this, AddProductActivity.class);
        startActivity(i);
    }

}
