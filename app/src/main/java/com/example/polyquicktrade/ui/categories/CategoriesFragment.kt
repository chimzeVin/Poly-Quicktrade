package com.example.polyquicktrade.ui.categories

import com.example.polyquicktrade.RecyclerClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.categories.CategoriesAdapter
import com.example.polyquicktrade.SharedViewModel
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.polyquicktrade.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment

class CategoriesFragment : Fragment(), RecyclerClickListener<String?> {
    private var recyclerView: RecyclerView? = null
    private var categoriesAdapter: CategoriesAdapter? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var sharedViewModel: SharedViewModel? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_categories, container, false)
        recyclerView = root.findViewById(R.id.CategoryRecyclerView)
        layoutManager = LinearLayoutManager(context)
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(
            SharedViewModel::class.java
        )
        recyclerView.setLayoutManager(layoutManager)
        categoriesAdapter = CategoriesAdapter(requireContext(), this)
        recyclerView.setAdapter(categoriesAdapter)
        return root
    }

    override fun onClick(item: String, view: View) {
        sharedViewModel!!.setCategory(item)
        val action =
            CategoriesFragmentDirections.actionNavigationCategoriesToBrowseCategoriesFragment()
        NavHostFragment.findNavController(this@CategoriesFragment).navigate(action)
        //    }
    }
}