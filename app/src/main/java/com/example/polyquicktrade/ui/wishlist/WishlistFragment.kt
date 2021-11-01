package com.example.polyquicktrade.ui.wishlist

import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.ui.wishlist.WishlistViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.browse.BrowseAdapter
import com.google.firebase.auth.FirebaseUser
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.polyquicktrade.R
import com.google.firebase.auth.FirebaseAuth
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import java.util.ArrayList

class WishlistFragment : Fragment(), RecyclerClickListener<Product?> {
    private var wishlistViewModel: WishlistViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var browseAdapter: BrowseAdapter? = null
    private var currentUser: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        wishlistViewModel = ViewModelProvider(this).get(WishlistViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_wishlist, container, false)
        currentUser = FirebaseAuth.getInstance().currentUser
        recyclerView = root.findViewById(R.id.wishlistRecyclerView)
        layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setLayoutManager(layoutManager)
        if (currentUser != null) {
            wishlistViewModel!!.getWishlistFromFirebase(currentUser)
        } else {
            Toast.makeText(
                requireContext(),
                "You need to be logged in to see your wishlist",
                Toast.LENGTH_LONG
            ).show()
        }
        wishlistViewModel!!.productMutableLiveData.observe(
            viewLifecycleOwner,
            Observer { products ->
                browseAdapter = BrowseAdapter(products, null, this@WishlistFragment)
                recyclerView.setAdapter(browseAdapter)
            })
        return root
    }

    override fun onClick(item: Product, view: View) {}
}