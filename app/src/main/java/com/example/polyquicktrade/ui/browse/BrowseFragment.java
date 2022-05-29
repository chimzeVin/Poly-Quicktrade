package com.example.polyquicktrade.ui.browse;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;
import com.example.polyquicktrade.SharedViewModel;
import com.example.polyquicktrade.pojo.Product;
import com.google.firebase.firestore.FirebaseFirestore;

public class BrowseFragment extends Fragment implements RecyclerClickListener<Product> {

    private SharedViewModel sharedViewModel;
    private BrowseViewModel browseViewModel;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private BrowseAdapter mAdapter;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        browseViewModel =
                new ViewModelProvider(this).get(BrowseViewModel.class);

        sharedViewModel =
                new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        browseViewModel.getProductsFromFirebase(db);

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_browse, container, false);

        recyclerView = root.findViewById(R.id.browseRecyclerView);
        progressBar = root.findViewById(R.id.browseProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);


        browseViewModel.getProductsMutableLiveData().observe(getViewLifecycleOwner(), products -> {



            browseViewModel.getUserMutableLiveData().observe(getViewLifecycleOwner(), users -> {
//                for (User user :
//                        users) {
//
//                }



                Log.d("MY_TAG", "BrowseFragment: Reached usermutabledata ");
                mAdapter = new BrowseAdapter(products, users, BrowseFragment.this);
                recyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);


            });

        });




        return root;
    }


    @Override
    public void onClick(Product item, View view) {

//                        Toast.makeText(BrowseFragment.this.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
//                        BrowseFragment.this.clickListener.onClick(item);
//        Navigation.findNavController(view).navigate(R.id.action_navigation_browse_to_productFragment);

        sharedViewModel.setProduct(item);
        NavDirections action =
                BrowseFragmentDirections.actionNavigationBrowseToProductFragment();
        NavHostFragment.findNavController(BrowseFragment.this).navigate(action);
    }
}