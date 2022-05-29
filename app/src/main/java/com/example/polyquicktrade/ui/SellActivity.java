package com.example.polyquicktrade.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyquicktrade.R;
import com.example.polyquicktrade.pojo.Product;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class SellActivity extends AppCompatActivity {


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




//        FloatingActionButton fab = findViewById(R.id.fab);
//        recyclerView = findViewById(R.id.SellRecyclerView);
//        sellViewModel = ViewModelProviders.of(this).get(SellViewModel.class);
        sellViewModel = new ViewModelProvider(this).get(SellViewModel.class);

//        layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);

//        user = FirebaseAuth.getInstance().getCurrentUser();
//        db = FirebaseFirestore.getInstance();

//        Navigation navController = this.findNavController(R.id.myNavHostFragment);

        NavController navController = Navigation.findNavController(this, R.id.sellNavHostFragment);

        NavigationUI.setupActionBarWithNavController(this, navController);


    }



}
