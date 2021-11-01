package com.example.polyquicktrade;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.ui.browse.BrowseAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class BrowseCategoriesFragment extends Fragment implements RecyclerClickListener<Product> {

private TextView categoryName;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BrowseAdapter mAdapter;
    private ProgressBar progressBar;

    private BrowseCategoriesViewModel browseCategoriesViewModel;
    private SharedViewModel sharedViewModel;
    private FirebaseFirestore db;
    public BrowseCategoriesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        browseCategoriesViewModel = ViewModelProviders.of(this).get(BrowseCategoriesViewModel.class);

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_browse_categories, container, false);


        recyclerView = v.findViewById(R.id.browseCategoryRecyclerView);
        categoryName = v.findViewById(R.id.browseCategoriesTextView);
        progressBar = v.findViewById(R.id.browseCategoryProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        sharedViewModel.getCategory().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                categoryName.setText(s);
                browseCategoriesViewModel.getCategoryProductsFromFirebase(db, s);
            }
        });

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        browseCategoriesViewModel.getCategoryProducts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                mAdapter = new BrowseAdapter(products, null, BrowseCategoriesFragment.this);
                recyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);
            }
        });


        return v;
    }

    @Override
    public void onClick(Product item, View view) {

//        NavDirections action =
//                BrowseFragmentDirections.actionNavigationBrowseToProductFragment();
//        NavHostFragment.findNavController(BrowseFragment.this).navigate(action);

        sharedViewModel.setProduct(item);
        NavDirections action =
                BrowseCategoriesFragmentDirections.actionBrowseCategoriesFragmentToProductFragment();
        NavHostFragment.findNavController(BrowseCategoriesFragment.this).navigate(action);

    }
}
