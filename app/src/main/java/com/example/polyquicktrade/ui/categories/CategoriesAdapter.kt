package com.example.polyquicktrade.ui.categories

import android.content.Context
import com.example.polyquicktrade.RecyclerClickListener
import androidx.recyclerview.widget.RecyclerView
import com.example.polyquicktrade.ui.categories.CategoriesAdapter.CategoriesViewHolder
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.polyquicktrade.R
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import java.util.*

class CategoriesAdapter(context: Context, clickListener: RecyclerClickListener<String?>) :
    RecyclerView.Adapter<CategoriesViewHolder>() {
    private val categories: MutableList<String>
    private val clickListener: RecyclerClickListener<String?>

    class CategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val catName: TextView
        private val constraintLayout: ConstraintLayout
        fun bindClickListener(category: String?, clickListener: RecyclerClickListener<String?>) {
            constraintLayout.setOnClickListener { view -> clickListener.onClick(category, view) }
        }

        init {
            catName = itemView.findViewById(R.id.CatNameTextView)
            constraintLayout = itemView.findViewById(R.id.categoriesConstraintLayout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.custom_categories_view, parent, false)
        return CategoriesViewHolder(v)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.catName.text = categories[position]
        holder.bindClickListener(categories[position], clickListener)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    init {
        categories =
            ArrayList(Arrays.asList(*context.resources.getStringArray(R.array.categories_array)))
        Collections.sort(categories, java.lang.String.CASE_INSENSITIVE_ORDER)
        categories.remove("Other")
        categories.add("Other")
        this.clickListener = clickListener
    }
}