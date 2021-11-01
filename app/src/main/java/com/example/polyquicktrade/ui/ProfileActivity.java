package com.example.polyquicktrade.ui;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.polyquicktrade.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import de.hdodenhof.circleimageview.CircleImageView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "MY_TAG";
    FirebaseAuth mFirebaseAuth;
    FirebaseUser firebaseUser;
    TextView nameTextview;
    TextView emailTextview;
    CircleImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(R.string.title_activity_profile);
        }
        mFirebaseAuth = FirebaseAuth.getInstance();

        nameTextview = findViewById(R.id.tv_username);
        emailTextview = findViewById(R.id.tv_email);

        profileImageView = findViewById(R.id.profile_image);


    }

    @Override
    protected void onStart() {
        super.onStart();

        firebaseUser = mFirebaseAuth.getCurrentUser();
        updateUI(firebaseUser);


    }

    private void updateUI(FirebaseUser firebaseUser) {

        if (firebaseUser != null) {

            nameTextview.setText(firebaseUser.getDisplayName());
            emailTextview.setText(firebaseUser.getEmail());



            Glide.with(this).load(firebaseUser.getPhotoUrl()).into(profileImageView);


        }
        else{
            //TODO handle error
        }

//        firebaseUser.getPhoneNumber();

    }


}
