package com.example.polyquicktrade.ui.ask;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;
import com.example.polyquicktrade.SharedViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AskFragment extends Fragment implements RecyclerClickListener<Enquiry> {

    private AskViewModel mViewModel;
    private SharedViewModel sharedViewModel;
    private FloatingActionButton enquireFab;

    private RecyclerView recyclerView;
    private AskAdapter askAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db;

    public static AskFragment newInstance() {
        return new AskFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ask, container, false);
        db = FirebaseFirestore.getInstance();

        mViewModel =
                ViewModelProviders.of(this).get(AskViewModel.class);
        sharedViewModel =
                ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);


        enquireFab = view.findViewById(R.id.fad_add_enquiry);
        recyclerView = view.findViewById(R.id.AskRecyclerView);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);



        enquireFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AskDialogFragment askDialogFragment = new AskDialogFragment();
                askDialogFragment.show(getFragmentManager(), "Ask");
            }
        });

        mViewModel.getEnquiriesFromFirebase(db);


        mViewModel.getEnquiriesMutableLiveData().observe(this, new Observer<ArrayList<Enquiry>>() {
            @Override
            public void onChanged(ArrayList<Enquiry> enquiries) {

                askAdapter = new AskAdapter(enquiries, AskFragment.this);
                recyclerView.setAdapter(askAdapter);


            }
        });



        return view;
    }


    @Override
    public void onClick(Enquiry item, View view) {

        sharedViewModel.setEnquiry(item);
        NavDirections action =
                AskFragmentDirections.actionNavigationAskToEnquiryAndResponsesFragment();

        NavHostFragment.findNavController(AskFragment.this).navigate(action);


    }
}
