package com.example.polyquicktrade.ui.browse

import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.SharedViewModel
import com.example.polyquicktrade.ui.browse.BrowseViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.browse.BrowseAdapter
import com.google.firebase.firestore.FirebaseFirestore
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
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
import com.example.polyquicktrade.pojo.User
import java.util.ArrayList

class BrowseFragment : Fragment(), RecyclerClickListener<Product?> {
    private var sharedViewModel: SharedViewModel? = null
    private var browseViewModel: BrowseViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var mAdapter: BrowseAdapter? = null
    private var progressBar: ProgressBar? = null
    private var db: FirebaseFirestore? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        browseViewModel = ViewModelProvider(this).get(BrowseViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        browseViewModel!!.getProductsFromFirebase(db)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_browse, container, false)
        recyclerView = root.findViewById(R.id.browseRecyclerView)
        progressBar = root.findViewById(R.id.browseProgressBar)
        progressBar.setVisibility(View.VISIBLE)
        layoutManager = LinearLayoutManager(context)
        recyclerView.setLayoutManager(layoutManager)
        browseViewModel!!.productsMutableLiveData.observe(viewLifecycleOwner, Observer { products ->
            browseViewModel!!.userMutableLiveData.observe(viewLifecycleOwner, Observer { users ->
                for (user in users) {
                }
                mAdapter = BrowseAdapter(products, users, this@BrowseFragment)
                recyclerView.setAdapter(mAdapter)
                progressBar.setVisibility(View.GONE)
            })
        })
        return root
    }

    override fun onClick(item: Product, view: View) {

//                        Toast.makeText(BrowseFragment.this.getContext(), item.getName(), Toast.LENGTH_SHORT).show();
//                        BrowseFragment.this.clickListener.onClick(item);
//        Navigation.findNavController(view).navigate(R.id.action_navigation_browse_to_productFragment);
        sharedViewModel!!.setProduct(item)
        val action = BrowseFragmentDirections.actionNavigationBrowseToProductFragment()
        NavHostFragment.findNavController(this@BrowseFragment).navigate(action)
    }
}