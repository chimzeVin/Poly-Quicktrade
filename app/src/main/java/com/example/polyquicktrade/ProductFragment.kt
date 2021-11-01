package com.example.polyquicktrade

import android.graphics.Color
import com.example.polyquicktrade.pojo.Product.uploadTime
import com.example.polyquicktrade.pojo.Product.name
import com.example.polyquicktrade.pojo.Product.price
import com.example.polyquicktrade.pojo.Product.location
import com.example.polyquicktrade.pojo.Product.description
import com.example.polyquicktrade.pojo.Product.phoneNumber
import com.example.polyquicktrade.pojo.Product.photoURI
import com.example.polyquicktrade.pojo.Product.deliveryOptions
import com.example.polyquicktrade.pojo.Product.paymentOptions
import com.example.polyquicktrade.ui.UserAccViewModel.addToWishlistFirebase
import com.example.polyquicktrade.ui.UserAccViewModel.addToCartFirebase
import com.example.polyquicktrade.SharedViewModel
import com.example.polyquicktrade.ui.UserAccViewModel
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.polyquicktrade.ProductFragment.ProductFragmentAdapter
import com.example.polyquicktrade.pojo.Product
import com.google.firebase.auth.FirebaseUser
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.polyquicktrade.R
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.bumptech.glide.Glide
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class ProductFragment : Fragment(), View.OnClickListener {
    private var sharedViewModel: SharedViewModel? = null
    private var userAccViewModel: UserAccViewModel? = null
    private var pName: TextView? = null
    private var pPrice: TextView? = null
    private var pLocation: TextView? = null
    private var pDesc: TextView? = null
    private var pDate: TextView? = null
    private var pWishlist: TextView? = null
    private var sName: TextView? = null
    private var sNumber: TextView? = null
    private var pPhoto: ImageView? = null
    private var buyNowbtn: Button? = null
    private var cartBtn: Button? = null
    private var deliveryRecyclerView: RecyclerView? = null
    private var paymentRecyclerView: RecyclerView? = null
    private var layoutManager: GridLayoutManager? = null
    private var mAdapter: ProductFragmentAdapter? = null
    private var layoutManager2: GridLayoutManager? = null
    private var mProduct: Product? = null
    private var currentUser: FirebaseUser? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_product, container, false)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        userAccViewModel = ViewModelProvider(this).get(UserAccViewModel::class.java)
        currentUser = FirebaseAuth.getInstance().currentUser
        initViews(v)
        sharedViewModel!!.product.observe(viewLifecycleOwner, Observer { product ->
            mProduct = product
            updateUI(product)
        })
        return v
    }

    private fun initViews(v: View) {
        pName = v.findViewById(R.id.prodNameView)
        pPrice = v.findViewById(R.id.prodPriceView)
        pLocation = v.findViewById(R.id.prodLocationView)
        pDesc = v.findViewById(R.id.prodDescView)
        pDate = v.findViewById(R.id.prodAddDateView)
        pWishlist = v.findViewById(R.id.prodWishlistView)
        sName = v.findViewById(R.id.prodSellerNameView)
        sNumber = v.findViewById(R.id.prodSellerNumberView)
        pPhoto = v.findViewById(R.id.prodPhotoView)
        buyNowbtn = v.findViewById(R.id.prodBuyNowView)
        cartBtn = v.findViewById(R.id.prodCartView)
        pWishlist.setOnClickListener(this)
        cartBtn.setOnClickListener(this)
        deliveryRecyclerView = v.findViewById(R.id.deliveryOptionsRecycler)
        paymentRecyclerView = v.findViewById(R.id.paymentOptionsRecycler)
        layoutManager = GridLayoutManager(this.context, 4)
        layoutManager2 = GridLayoutManager(this.context, 3)
        deliveryRecyclerView.setLayoutManager(layoutManager)
        paymentRecyclerView.setLayoutManager(layoutManager2)
    }

    private fun updateUI(product: Product) {
        val date = DateFormat.getDateInstance().format(Date(product.uploadTime))
        pName!!.text = product.name
        val priceUnformatted = product.price
        val priceFormatted = getStringPrice(priceUnformatted)
        pPrice!!.text = String.format("MWK %s", priceFormatted)
        pLocation!!.text = product.location
        pDesc!!.text = product.description
        pDate!!.text = date
        sName!!.text = "Khumbolawo Mussa"
        sNumber!!.text = product.phoneNumber
        Glide.with(this)
            .load(product.photoURI)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .into(pPhoto!!)
        val deliveryOptions: List<String?> = product.deliveryOptions
        val paymentOptions: List<String?> = product.paymentOptions

//        userAccViewModel.getSellerFromFirebase(product.getSeller());

//        userAccViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                sName.setText(user.getUserName());
//            }
//        });
        mAdapter = ProductFragmentAdapter(deliveryOptions)
        deliveryRecyclerView!!.adapter = mAdapter
        mAdapter = ProductFragmentAdapter(paymentOptions)
        paymentRecyclerView!!.adapter = mAdapter
    }

    fun getStringPrice(price: Double): String {
        val fPrice: String
        val numberFormatter: NumberFormat
        numberFormatter = NumberFormat.getNumberInstance(Locale.US)
        fPrice = numberFormatter.format(price.toInt().toLong())
        return fPrice
    }

    override fun onClick(view: View) {
        if (currentUser != null) {
            if (view.id == R.id.prodWishlistView) {
                userAccViewModel!!.addToWishlistFirebase(mProduct!!, currentUser!!)
                Toast.makeText(requireContext(), "Added to Wishlist", Toast.LENGTH_LONG).show()
            } else if (view.id == R.id.prodCartView) {
                userAccViewModel!!.addToCartFirebase(mProduct!!, currentUser!!)
                Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(requireContext(), "You must first login", Toast.LENGTH_LONG).show()
        }
    }

    private inner class ProductFragmentAdapter(private val options: List<String?>) :
        RecyclerView.Adapter<ProductFragmentAdapter.ProductFragmentViewHolder>() {
        internal inner class ProductFragmentViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var optionTextView: TextView

            init {
                optionTextView = itemView.findViewById(R.id.OptionTextView)
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ProductFragmentViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val v = inflater.inflate(R.layout.custom_product_fragment, parent, false)
            return ProductFragmentViewHolder(v)
        }

        override fun onBindViewHolder(holder: ProductFragmentViewHolder, position: Int) {
            holder.optionTextView.text = options[position]
        }

        override fun getItemCount(): Int {
            return options.size
        }
    }
}