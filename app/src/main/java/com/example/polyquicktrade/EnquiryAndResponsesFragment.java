package com.example.polyquicktrade;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.ui.browse.BrowseAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class EnquiryAndResponsesFragment extends Fragment implements RecyclerClickListener<Product>{


    private TextView enquiryName;
    private TextView enquiryDesc;
    private Button addProductResponseBtn;
    private SharedViewModel sharedViewModel;
    private RecyclerView recyclerView;
    private BrowseCategoriesViewModel browseCategoriesViewModel;
    private FirebaseFirestore db;
    private RecyclerView.LayoutManager layoutManager;
    private BrowseAdapter browseAdapter;

    public EnquiryAndResponsesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_enquiry_and_responses, container, false);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        browseCategoriesViewModel = new ViewModelProvider(this).get(BrowseCategoriesViewModel.class);
        db = FirebaseFirestore.getInstance();

        initViews(view);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

        sharedViewModel.getEnquiry().observe(getViewLifecycleOwner(), new Observer<Enquiry>() {
            @Override
            public void onChanged(final Enquiry enquiry) {
                enquiryName.setText(enquiry.getProductName());
                enquiryDesc.setText(enquiry.getProductDesc());
                browseCategoriesViewModel.getEnquiryProducts(enquiry);
                addProductResponseBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EnquiryAndResponsesFragmentDirections.ActionEnquiryAndResponsesFragmentToAddProductActivity action
                                = EnquiryAndResponsesFragmentDirections.actionEnquiryAndResponsesFragmentToAddProductActivity();

                        action.setAddProductToEnquiry(enquiry.getId());

                        NavHostFragment.findNavController(EnquiryAndResponsesFragment.this).navigate(action);
                    }
                });
            }
        });

        browseCategoriesViewModel.getCategoryProducts().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                browseAdapter = new BrowseAdapter(products, null, EnquiryAndResponsesFragment.this);
                recyclerView.setAdapter(browseAdapter);


            }
        });


        return view;
    }

    private void initViews(View view) {
        enquiryName = view.findViewById(R.id.ResponsesNameTextView);
        enquiryDesc = view.findViewById(R.id.ResponsesDescTextView);
        addProductResponseBtn = view.findViewById(R.id.AddProdResponseBtn);
        recyclerView = view.findViewById(R.id.ResponsesRecyclerView);


    }

    @Override
    public void onClick(Product item, View view) {
        sharedViewModel.setProduct(item);
        NavDirections action = EnquiryAndResponsesFragmentDirections.actionEnquiryAndResponsesFragmentToProductFragment();
        NavHostFragment.findNavController(EnquiryAndResponsesFragment.this).navigate(action);
    }
}
