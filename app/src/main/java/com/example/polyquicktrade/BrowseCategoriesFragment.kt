package com.example.polyquicktrade

import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Product
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.browse.BrowseAdapter
import com.example.polyquicktrade.BrowseCategoriesViewModel
import com.example.polyquicktrade.SharedViewModel
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.polyquicktrade.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class BrowseCategoriesFragment : Fragment(), RecyclerClickListener<Product?> {
    private var categoryName: TextView? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: BrowseAdapter? = null
    private var progressBar: ProgressBar? = null
    private var browseCategoriesViewModel: BrowseCategoriesViewModel? = null
    private var sharedViewModel: SharedViewModel? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        browseCategoriesViewModel = ViewModelProviders.of(this).get(
            BrowseCategoriesViewModel::class.java
        )
        sharedViewModel = ViewModelProviders.of(requireActivity()).get(
            SharedViewModel::class.java
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_browse_categories, container, false)
        recyclerView = v.findViewById(R.id.browseCategoryRecyclerView)
        categoryName = v.findViewById(R.id.browseCategoriesTextView)
        progressBar = v.findViewById(R.id.browseCategoryProgressBar)
        progressBar.setVisibility(View.VISIBLE)
        sharedViewModel!!.category.observe(viewLifecycleOwner, Observer { s ->
            categoryName.setText(s)
            browseCategoriesViewModel!!.getCategoryProductsFromFirebase(db, s)
        })
        layoutManager = LinearLayoutManager(context)
        recyclerView.setLayoutManager(layoutManager)
        browseCategoriesViewModel!!.categoryProducts.observe(
            viewLifecycleOwner,
            Observer { products ->
                mAdapter = BrowseAdapter(products, null, this@BrowseCategoriesFragment)
                recyclerView.setAdapter(mAdapter)
                progressBar.setVisibility(View.GONE)
            })
        return v
    }

    override fun onClick(item: Product, view: View) {

//        NavDirections action =
//                BrowseFragmentDirections.actionNavigationBrowseToProductFragment();
//        NavHostFragment.findNavController(BrowseFragment.this).navigate(action);
        sharedViewModel!!.setProduct(item)
        val action =
            BrowseCategoriesFragmentDirections.actionBrowseCategoriesFragmentToProductFragment()
        NavHostFragment.findNavController(this@BrowseCategoriesFragment).navigate(action)
    }
}