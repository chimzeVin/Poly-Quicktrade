package com.example.polyquicktrade

import com.example.polyquicktrade.pojo.Enquiry.productName
import com.example.polyquicktrade.pojo.Enquiry.productDesc
import androidx.appcompat.app.AppCompatActivity
import com.example.polyquicktrade.ui.ask.AskDialogFragment.AskDialogListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.example.polyquicktrade.HomeViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.polyquicktrade.pojo.Enquiry
import android.widget.Toast
import android.os.Bundle
import com.example.polyquicktrade.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.content.Intent
import com.example.polyquicktrade.ui.SellActivity
import androidx.lifecycle.ViewModelProviders
import androidx.multidex.MultiDex
import android.app.SearchManager
import android.content.ComponentName
import android.content.Context
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.example.polyquicktrade.ui.SearchActivity
import com.example.polyquicktrade.ui.ProfileActivity
import com.example.polyquicktrade.ui.SignInActivity
import com.example.polyquicktrade.HomeActivity
import com.example.polyquicktrade.ui.CartActivity
import com.example.polyquicktrade.ui.SettingsActivity
import java.util.*

class HomeActivity : AppCompatActivity(), AskDialogListener {
    private var mAuth: FirebaseAuth? = null
    private var currentUser: FirebaseUser? = null
    private var homeViewModel: HomeViewModel? = null
    private var db: FirebaseFirestore? = null
    override fun onDialogPositiveClick(
        dialog: DialogFragment?,
        productName: String?,
        productDesc: String?
    ) {
        //TODO handle the response
        Log.d("MY_TAG", "$productName - $productDesc")
        val date = Date()
        val currentTime = date.time
        if (currentUser != null) {
            val enquiry =
                Enquiry("", productName, productDesc, currentUser!!.uid, currentTime, ArrayList())
            homeViewModel!!.uploadEnquireToFirebase(db, enquiry)
        } else Toast.makeText(this, "You must first Log In", Toast.LENGTH_LONG).show()
        homeViewModel!!.enquiry.observe(this, Observer { enquiry ->
            Log.d("MY_TAG", "Uploaded: " + enquiry.productName + " - " + enquiry.productDesc)
            Toast.makeText(
                this@HomeActivity,
                "Uploaded: " + enquiry.productName + " - " + enquiry.productDesc,
                Toast.LENGTH_LONG
            ).show()
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val myToolbar = findViewById<Toolbar>(R.id.toolbar_home)
        setSupportActionBar(myToolbar)
        val navView = findViewById<BottomNavigationView>(R.id.navigation)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupWithNavController(navView, navController)
        navView.setOnNavigationItemSelectedListener { menuItem ->
            if (menuItem.itemId == R.id.navigation_sell) {
                val intent = Intent(this@HomeActivity, SellActivity::class.java)
                startActivity(intent)
            }
            NavigationUI.onNavDestinationSelected(menuItem, navController)
            false
        }

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        db = FirebaseFirestore.getInstance()
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        MultiDex.install(this)
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        currentUser = mAuth!!.currentUser
        //        TODO handle this updateUI(currentUser);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        val item = menu.findItem(R.id.action_profile)
        if (currentUser != null) {
            item.title = "Profile"
        } else {
            item.title = "Log In"
        }
        val searchItem = menu.findItem(R.id.action_search)

        // Get the SearchView and set the searchable configuration
        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = searchItem.actionView as SearchView


        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(
            searchManager.getSearchableInfo(
                ComponentName(
                    this,
                    SearchActivity::class.java
                )
            )
        ) //add this to search class to make it call itself when searching again
        searchView.setIconifiedByDefault(true) // Do not iconify the widget; expand it by default
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_profile ->             //TODO if the user is logged in take him to profile screen. if not open login activity
                return if (currentUser != null) {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                    true
                } else {
                    val intent = Intent(this, SignInActivity::class.java)
                    startActivityForResult(intent, REQCODE)
                    true
                }
            R.id.action_cart -> {
                val `in` = Intent(this, CartActivity::class.java)
                Toast.makeText(this, "Cart", Toast.LENGTH_LONG)
                startActivity(`in`)
                return true
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                Toast.makeText(this, "Settings", Toast.LENGTH_LONG)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val REQCODE = 1254
    }
}