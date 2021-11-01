package com.example.polyquicktrade.ui.wishlist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;
import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.ui.browse.BrowseAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class WishlistFragment extends Fragment implements RecyclerClickListener<Product> {

    private WishlistViewModel wishlistViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BrowseAdapter browseAdapter;
    private FirebaseUser currentUser;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        wishlistViewModel =
                new ViewModelProvider(this).get(WishlistViewModel.class);
        View root = inflater.inflate(R.layout.fragment_wishlist, container, false);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        recyclerView = root.findViewById(R.id.wishlistRecyclerView);
        layoutManager = new LinearLayoutManager(requireContext());
        recyclerView.setLayoutManager(layoutManager);


        if (currentUser != null){

            wishlistViewModel.getWishlistFromFirebase(currentUser);
        }
        else {
            Toast.makeText(requireContext(), "You need to be logged in to see your wishlist", Toast.LENGTH_LONG).show();
        }

        wishlistViewModel.getProductMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                browseAdapter = new BrowseAdapter(products, null, WishlistFragment.this);
                recyclerView.setAdapter(browseAdapter);

            }
        });


        return root;
    }

    @Override
    public void onClick(Product item, View view) {

    }
}