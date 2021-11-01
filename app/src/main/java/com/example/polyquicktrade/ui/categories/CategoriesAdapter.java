package com.example.polyquicktrade.ui.categories;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {

    private  List<String> categories;
    private RecyclerClickListener<String> clickListener;

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder{
        private TextView catName;
        private ConstraintLayout constraintLayout;
        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            catName = itemView.findViewById(R.id.CatNameTextView);
            constraintLayout = itemView.findViewById(R.id.categoriesConstraintLayout);
        }

        public void bindClickListener(final String category, final RecyclerClickListener<String> clickListener) {

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(category, view);
                }
            });
        }
    }

    public CategoriesAdapter(Context context, RecyclerClickListener<String> clickListener) {

        categories = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.categories_array)));
        Collections.sort(categories, String.CASE_INSENSITIVE_ORDER);
        categories.remove("Other");
        categories.add("Other");
        this.clickListener = clickListener;

    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_categories_view, parent, false);

        return new CategoriesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        holder.catName.setText(categories.get(position));

        holder.bindClickListener(categories.get(position), this.clickListener);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
