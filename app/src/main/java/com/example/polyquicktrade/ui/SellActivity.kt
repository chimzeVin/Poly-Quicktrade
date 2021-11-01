package com.example.polyquicktrade.ui

import com.example.polyquicktrade.pojo.Product.toString
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.SellAdapter
import com.example.polyquicktrade.ui.SellViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseUser
import android.os.Bundle
import com.example.polyquicktrade.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.example.polyquicktrade.pojo.Product
import android.widget.Toast
import android.content.Intent
import android.util.Log
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.example.polyquicktrade.ui.AddProductActivity
import java.util.ArrayList

class SellActivity : AppCompatActivity() {
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var sellAdapter: SellAdapter? = null
    private var sellViewModel: SellViewModel? = null
    private var db: FirebaseFirestore? = null
    private var user: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(R.string.title_activity_sell)
        }
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        recyclerView = findViewById(R.id.SellRecyclerView)
        sellViewModel = ViewModelProviders.of(this).get(SellViewModel::class.java)
        layoutManager = LinearLayoutManager(this)
        recyclerView.setLayoutManager(layoutManager)
        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()
        if (user != null) {
            sellViewModel!!.getSellerProductsFromFirebase(db, user!!.uid)
            sellViewModel!!.productMutableLiveData.observe(this, Observer { products ->
                sellAdapter = SellAdapter(products)
                Log.d("MY_TAG", products[0].toString())
                recyclerView.setAdapter(sellAdapter)
            })
        }
        fab.setOnClickListener {
            if (user != null) {
                openAddProductActivity()
            } else {
                Toast.makeText(this@SellActivity, "You are not logged in", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun openAddProductActivity() {
        val i = Intent(this, AddProductActivity::class.java)
        startActivity(i)
    }
}