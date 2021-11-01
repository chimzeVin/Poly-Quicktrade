package com.example.polyquicktrade.ui.browse

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.polyquicktrade.R
import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Product
import com.example.polyquicktrade.pojo.User
import com.example.polyquicktrade.ui.browse.BrowseAdapter.BrowseViewHolder
import java.text.NumberFormat
import java.util.*

class BrowseAdapter(
    products: List<Product>?,
    users: ArrayList<User>?,
    clickListener: RecyclerClickListener<Product?>
) : RecyclerView.Adapter<BrowseViewHolder>() {
    private var products: List<Product>?
    private val users: List<User>?
    private val clickListener: RecyclerClickListener<Product?>

    class BrowseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView
        var price: TextView
        var seller: TextView
        var location: TextView
        var productPhoto: ImageView
        var cardView: CardView

        internal inner class ProductFragmentViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView)

        fun bindListener(product: Product?, clickListener: RecyclerClickListener<Product?>) {
            cardView.setOnClickListener { view -> clickListener.onClick(product, view) }
        }

        init {
            name = itemView.findViewById(R.id.cbName)
            price = itemView.findViewById(R.id.cbPrice)
            seller = itemView.findViewById(R.id.cbSeller)
            location = itemView.findViewById(R.id.cbLocation)
            productPhoto = itemView.findViewById(R.id.cbImageView)
            cardView = itemView.findViewById(R.id.browseCardView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrowseViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.custom_browse_view, parent, false)
        return BrowseViewHolder(v)
    }

    override fun onBindViewHolder(holder: BrowseViewHolder, position: Int) {
        val product = products!![position]
        holder.name.text = product.name
        val priceUnformatted = product.price
        val priceFormatted = getStringPrice(priceUnformatted)
        holder.price.text = String.format("MWK %s", priceFormatted)
        holder.location.text = product.location
        Glide.with(holder.itemView.context)
            .load(product.photoURI)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .into(holder.productPhoto)
        holder.bindListener(product, clickListener)
        if (users != null) {
            for (user in users) {
                if (product.seller == user.uid) {
                    holder.seller.text = user.userName
                    break
                }
            }
        }

//        userAccViewModel.getSellerFromFirebase(product.getId());
//        userAccViewModel.getUserMutableLiveData().observe(viewLifecycleOwner, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//
////                if (user != null ){
//
////                    Log.d("MY_TAG", "onChanged: "+ user.getUserName());
////                }
////                holder.seller.setText(user.getUserName());//you can send the data of the seller along with the product details
//
//            }
//        });
    }

    override fun getItemCount(): Int {
        return products!!.size //TODO
    }

    fun getStringPrice(price: Double): String {
        val fPrice: String
        val numberFormatter: NumberFormat
        numberFormatter = NumberFormat.getNumberInstance(Locale.US)
        fPrice = numberFormatter.format(price.toInt().toLong())
        return fPrice
    }

    init {
        if (products == null) this.products = ArrayList()
        this.products = products
        this.users = users
        this.clickListener = clickListener
    }
}