package com.example.polyquicktrade.ui.sell

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.polyquicktrade.databinding.FragmentSellBinding
import com.example.polyquicktrade.ui.AddProductActivity
import com.example.polyquicktrade.ui.SellAdapter
import com.example.polyquicktrade.ui.SellViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore


class SellFragment: Fragment() {

    private var _binding: FragmentSellBinding? = null
    private val binding get() = _binding!!
    private var user: FirebaseUser? = null
    private var db: FirebaseFirestore? = null
    private val sellViewModel: SellViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        _binding = FragmentSellBinding.inflate(inflater)

        user = FirebaseAuth.getInstance().currentUser
        db = FirebaseFirestore.getInstance()

        initViews(binding)

        return binding.root
    }

    fun initViews(binding: FragmentSellBinding){
        binding.addSellerFab.setOnClickListener {
            if (user != null) {
                openAddProductActivity()
            } else {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Toast.makeText(context, "You are not logged in", Toast.LENGTH_LONG).show()
                }
            }
        }

        if (user != null) {
            sellViewModel.getSellerProductsFromFirebase(db, user!!.uid)
            sellViewModel.productMutableLiveData.observe(viewLifecycleOwner, {products->
                val sellAdapter = SellAdapter(products, SellAdapter.ItemListener{
                    //Navigate
                    Toast.makeText(context, it.name, Toast.LENGTH_SHORT).show()
                })
                Log.d("MY_TAG", products[0].toString())
                binding.SellRecyclerView.adapter = sellAdapter
            })
        }
    }

    private fun openAddProductActivity() {
        val i = Intent(context, AddProductActivity::class.java)
        startActivity(i)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}