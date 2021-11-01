package com.example.polyquicktrade.ui

import android.graphics.Color
import com.example.polyquicktrade.pojo.Product.uploadTime
import com.example.polyquicktrade.pojo.Product.name
import com.example.polyquicktrade.pojo.Product.description
import com.example.polyquicktrade.pojo.Product.price
import com.example.polyquicktrade.pojo.Product.photoURI
import com.example.polyquicktrade.pojo.Product
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.SellAdapter.SellViewHolder
import android.widget.TextView
import com.example.polyquicktrade.R
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.widget.ImageView
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

internal class SellAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<SellViewHolder>() {
    class SellViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var prodNameTV: TextView
        var prodDescTV: TextView
        var prodPriceTV: TextView
        var prodDate: TextView
        var prodImageView: ImageView

        init {
            prodNameTV = itemView.findViewById(R.id.sellProdNameTV)
            prodDescTV = itemView.findViewById(R.id.sellProdDescTV)
            prodPriceTV = itemView.findViewById(R.id.sellProdPriceTV)
            prodDate = itemView.findViewById(R.id.sellProdDateTV)
            prodImageView = itemView.findViewById(R.id.csImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.custom_sell, parent, false)
        return SellViewHolder(view)
    }

    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
        val product = products[position]
        val date = DateFormat.getDateInstance().format(Date(product.uploadTime))
        holder.prodNameTV.text = product.name
        holder.prodDescTV.text = product.description
        val priceUnformatted = product.price
        val priceFormatted = getStringPrice(priceUnformatted)
        holder.prodPriceTV.text = String.format("MWK %s", priceFormatted)
        holder.prodDate.text = date
        Glide.with(holder.itemView.context)
            .load(product.photoURI)
            .centerCrop()
            .placeholder(ColorDrawable(Color.LTGRAY))
            .into(holder.prodImageView)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun getStringPrice(price: Double): String {
        val fPrice: String
        val numberFormatter: NumberFormat
        numberFormatter = NumberFormat.getNumberInstance(Locale.US)
        fPrice = numberFormatter.format(price.toInt().toLong())
        return fPrice
    }
}