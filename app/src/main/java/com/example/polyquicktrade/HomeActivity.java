package com.example.polyquicktrade;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.ui.CartActivity;
import com.example.polyquicktrade.ui.ProfileActivity;
import com.example.polyquicktrade.ui.SearchActivity;
import com.example.polyquicktrade.ui.SellActivity;
import com.example.polyquicktrade.ui.SettingsActivity;
import com.example.polyquicktrade.ui.SignInActivity;
import com.example.polyquicktrade.ui.ask.AskDialogFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.multidex.MultiDex;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity implements AskDialogFragment.AskDialogListener {

    private static final int REQCODE = 1254;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private HomeViewModel homeViewModel;
    private FirebaseFirestore db;

    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String productName, String productDesc) {
        //TODO handle the response
        Log.d("MY_TAG", productName + " - " + productDesc);

        Date date = new Date();
        long currentTime = date.getTime();

        if (currentUser!=null){
            Enquiry enquiry = new Enquiry("", productName, productDesc, currentUser.getUid(), currentTime, new ArrayList<String>());
            homeViewModel.uploadEnquireToFirebase(db, enquiry);

        }
        else
            Toast.makeText(this, "You must first Log In", Toast.LENGTH_LONG).show();



        homeViewModel.getEnquiry().observe(this, new Observer<Enquiry>() {
            @Override
            public void onChanged(Enquiry enquiry) {
                Log.d("MY_TAG", "Uploaded: " + enquiry.getProductName() + " - " + enquiry.getProductDesc());
                Toast.makeText(HomeActivity.this, "Uploaded: " + enquiry.getProductName() + " - " + enquiry.getProductDesc(), Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(myToolbar);

        BottomNavigationView navView = findViewById(R.id.navigation);
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.navigation_sell){
                    Intent intent = new Intent(HomeActivity.this, SellActivity.class);
                    startActivity(intent);
                }
                NavigationUI.onNavDestinationSelected(menuItem, navController);
                return false;
            }
        });

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.

        currentUser = mAuth.getCurrentUser();
//        TODO handle this updateUI(currentUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        MenuItem item = menu.findItem(R.id.action_profile);

        if (currentUser != null){
            item.setTitle("Profile");
        }else
        {
            item.setTitle("Log In");

        }

        MenuItem searchItem = menu.findItem(R.id.action_search);

        // Get the SearchView and set the searchable configuration
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) searchItem.getActionView();



        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager.getSearchableInfo(new ComponentName(this, SearchActivity.class))); //add this to search class to make it call itself when searching again
        searchView.setIconifiedByDefault(true); // Do not iconify the widget; expand it by default




        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
        case R.id.action_profile:
            //TODO if the user is logged in take him to profile screen. if not open login activity
            if (currentUser != null ){

                Intent intent = new Intent(this, ProfileActivity.class);

                startActivity(intent);
                return true;

            }else {

                Intent intent = new Intent(this, SignInActivity.class);


                startActivityForResult(intent, REQCODE);
                return true;
            }


        case R.id.action_cart:
            Intent in = new Intent(this, CartActivity.class);
            Toast.makeText(this, "Cart", Toast.LENGTH_LONG);

            startActivity(in);
        return true;

        case R.id.action_settings:
        Intent intent = new Intent(this, SettingsActivity.class);
            Toast.makeText(this, "Settings", Toast.LENGTH_LONG);

            startActivity(intent);
        return true;

    }

        return super.onOptionsItemSelected(item);
    }

}
