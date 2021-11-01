package com.example.polyquicktrade.ui.ask

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.R
import com.example.polyquicktrade.RecyclerClickListener
import com.example.polyquicktrade.pojo.Enquiry
import com.example.polyquicktrade.ui.ask.AskAdapter.AskViewHolder

internal class AskAdapter(
    private val enquiries: List<Enquiry>,
    private val clickListener: RecyclerClickListener<Enquiry?>
) : RecyclerView.Adapter<AskViewHolder>() {
    class AskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var enquiryNameTV: TextView = itemView.findViewById(R.id.EnquiryNameTextView)
        var enquireDescTV: TextView
        var cardView: CardView
        fun bindListener(enquiry: Enquiry?, clickListener: RecyclerClickListener<Enquiry?>) {
            cardView.setOnClickListener { view -> clickListener.onClick(enquiry, view) }
        }

        init {
            enquireDescTV = itemView.findViewById(R.id.EnquiryDescTextView)
            cardView = itemView.findViewById(R.id.EnquiryCardView)
        }
    }
//
//    fun add(newEnquiries: List<Enquiry>){
//        enquiries.
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AskViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.custom_ask_view, parent, false)
        return AskViewHolder(v)
    }

    override fun onBindViewHolder(holder: AskViewHolder, position: Int) {
        val enquiry = enquiries[position]
        holder.enquiryNameTV.text = enquiry.productName
        holder.enquireDescTV.text = enquiry.productDesc
        holder.bindListener(enquiry, clickListener)
    }

    override fun getItemCount(): Int {
        return enquiries.size
    }


}