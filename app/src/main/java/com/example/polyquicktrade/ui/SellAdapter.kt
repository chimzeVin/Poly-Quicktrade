package com.example.polyquicktrade.ui

import android.graphics.Color
import com.example.polyquicktrade.pojo.Product
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.SellAdapter.SellViewHolder
import android.view.ViewGroup
import android.view.LayoutInflater
import com.bumptech.glide.Glide
import android.graphics.drawable.ColorDrawable
import com.example.polyquicktrade.databinding.CustomSellBinding
import java.text.DateFormat
import java.text.NumberFormat
import java.util.*

class SellAdapter(private val products: List<Product>, val clickListener: ItemListener) : RecyclerView.Adapter<SellViewHolder>() {
    class SellViewHolder private constructor(private val binding: CustomSellBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product, clickListener: ItemListener) {
            binding.product = product
            binding.clickListener = clickListener
            val date = DateFormat.getDateInstance().format(Date(product.uploadTime))
            binding.sellProdNameTV.text = product.name
            binding.sellProdDescTV.text = product.description
            val priceUnformatted = product.price
            val priceFormatted = getStringPrice(priceUnformatted)
            binding.sellProdPriceTV.text = String.format("MWK %s", priceFormatted)
            binding.sellProdDateTV.text = date
            Glide.with(binding.root.context)
                .load(product.photoURI)
                .centerCrop()
                .placeholder(ColorDrawable(Color.LTGRAY))
                .into(binding.csImageView)
        }
        private fun getStringPrice(price: Double): String {
            val fPrice: String
            val numberFormatter: NumberFormat = NumberFormat.getNumberInstance(Locale.US)
            fPrice = numberFormatter.format(price.toInt().toLong())
            return fPrice
        }

        companion object{
            fun from(parent: ViewGroup): SellViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = CustomSellBinding.inflate(layoutInflater, parent, false)
                return SellViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SellViewHolder {
        return SellViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: SellViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product, clickListener)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    class ItemListener(val clickListener: (product: Product) -> Unit){
        fun onClick(product: Product) = clickListener(product)
    }
}