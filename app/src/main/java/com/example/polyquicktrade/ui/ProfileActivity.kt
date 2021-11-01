package com.example.polyquicktrade.ui

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.widget.TextView
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.example.polyquicktrade.R
import com.bumptech.glide.Glide
import de.hdodenhof.circleimageview.CircleImageView

class ProfileActivity : AppCompatActivity() {
    var mFirebaseAuth: FirebaseAuth? = null
    var firebaseUser: FirebaseUser? = null
    var nameTextview: TextView? = null
    var emailTextview: TextView? = null
    var profileImageView: CircleImageView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setTitle(R.string.title_activity_profile)
        }
        mFirebaseAuth = FirebaseAuth.getInstance()
        nameTextview = findViewById(R.id.tv_username)
        emailTextview = findViewById(R.id.tv_email)
        profileImageView = findViewById(R.id.profile_image)
    }

    override fun onStart() {
        super.onStart()
        firebaseUser = mFirebaseAuth!!.currentUser
        updateUI(firebaseUser)
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            nameTextview!!.text = firebaseUser.displayName
            emailTextview!!.text = firebaseUser.email
            Glide.with(this).load(firebaseUser.photoUrl).into(profileImageView!!)
        } else {
            //TODO handle error
        }

//        firebaseUser.getPhoneNumber();
    }

    companion object {
        private const val TAG = "MY_TAG"
    }
}