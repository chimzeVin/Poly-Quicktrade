package com.example.polyquicktrade

import com.example.polyquicktrade.pojo.Enquiry.productName
import com.example.polyquicktrade.pojo.Enquiry.productDesc
import com.example.polyquicktrade.BrowseCategoriesViewModel.getEnquiryProducts
import com.example.polyquicktrade.pojo.Enquiry.id
import com.example.polyquicktrade.BrowseCategoriesViewModel.categoryProducts
import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Product
import android.widget.TextView
import com.example.polyquicktrade.SharedViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.BrowseCategoriesViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.example.polyquicktrade.ui.browse.BrowseAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.polyquicktrade.R
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.polyquicktrade.pojo.Enquiry
import com.example.polyquicktrade.EnquiryAndResponsesFragmentDirections.ActionEnquiryAndResponsesFragmentToAddProductActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.NavDirections
import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class EnquiryAndResponsesFragment : Fragment(), RecyclerClickListener<Product?> {
    private var enquiryName: TextView? = null
    private var enquiryDesc: TextView? = null
    private var addProductResponseBtn: Button? = null
    private var sharedViewModel: SharedViewModel? = null
    private var recyclerView: RecyclerView? = null
    private var browseCategoriesViewModel: BrowseCategoriesViewModel? = null
    private var db: FirebaseFirestore? = null
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var browseAdapter: BrowseAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_enquiry_and_responses, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        browseCategoriesViewModel = ViewModelProvider(this).get(
            BrowseCategoriesViewModel::class.java
        )
        db = FirebaseFirestore.getInstance()
        initViews(view)
        layoutManager = LinearLayoutManager(context)
        recyclerView!!.layoutManager = layoutManager
        sharedViewModel!!.enquiry.observe(viewLifecycleOwner, Observer { enquiry ->
            enquiryName!!.text = enquiry.productName
            enquiryDesc!!.text = enquiry.productDesc
            browseCategoriesViewModel!!.getEnquiryProducts(enquiry)
            addProductResponseBtn!!.setOnClickListener {
                val action =
                    EnquiryAndResponsesFragmentDirections.actionEnquiryAndResponsesFragmentToAddProductActivity()
                action.setAddProductToEnquiry(enquiry.id)
                NavHostFragment.findNavController(this@EnquiryAndResponsesFragment).navigate(action)
            }
        })
        browseCategoriesViewModel!!.categoryProducts.observe(
            viewLifecycleOwner,
            Observer<ArrayList<Product?>?> { products ->
                browseAdapter = BrowseAdapter(products, null, this@EnquiryAndResponsesFragment)
                recyclerView!!.adapter = browseAdapter
            })
        return view
    }

    private fun initViews(view: View) {
        enquiryName = view.findViewById(R.id.ResponsesNameTextView)
        enquiryDesc = view.findViewById(R.id.ResponsesDescTextView)
        addProductResponseBtn = view.findViewById(R.id.AddProdResponseBtn)
        recyclerView = view.findViewById(R.id.ResponsesRecyclerView)
    }

    override fun onClick(item: Product, view: View) {
        sharedViewModel!!.setProduct(item)
        val action =
            EnquiryAndResponsesFragmentDirections.actionEnquiryAndResponsesFragmentToProductFragment()
        NavHostFragment.findNavController(this@EnquiryAndResponsesFragment).navigate(action)
    }
}