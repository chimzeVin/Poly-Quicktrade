package com.example.polyquicktrade.ui.categories;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavDirections;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;
import com.example.polyquicktrade.SharedViewModel;
import com.example.polyquicktrade.ui.browse.BrowseFragment;
import com.example.polyquicktrade.ui.browse.BrowseFragmentDirections;

public class CategoriesFragment extends Fragment implements RecyclerClickListener<String> {

    private RecyclerView recyclerView;
    private CategoriesAdapter categoriesAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedViewModel sharedViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_categories, container, false);


        recyclerView = root.findViewById(R.id.CategoryRecyclerView);
        layoutManager = new LinearLayoutManager(getContext());

        sharedViewModel = ViewModelProviders.of(requireActivity()).get(SharedViewModel.class);

        recyclerView.setLayoutManager(layoutManager);
        categoriesAdapter = new CategoriesAdapter(requireContext(), this);
        recyclerView.setAdapter(categoriesAdapter);


        return root;
    }

    @Override
    public void onClick(String item, View view) {

        sharedViewModel.setCategory(item);
        NavDirections action =
                CategoriesFragmentDirections.actionNavigationCategoriesToBrowseCategoriesFragment();
        NavHostFragment.findNavController(CategoriesFragment.this).navigate(action);
//    }

    }
}





//otherSeriesList = ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.get_other_series)))

