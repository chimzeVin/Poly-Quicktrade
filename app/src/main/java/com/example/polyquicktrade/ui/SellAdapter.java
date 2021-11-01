package com.example.polyquicktrade.ui;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.R;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class SellAdapter extends RecyclerView.Adapter<SellAdapter.SellViewHolder> {

    private final List<Product> products;

    public static class SellViewHolder extends RecyclerView.ViewHolder{

        TextView prodNameTV, prodDescTV, prodPriceTV, prodDate;
        ImageView prodImageView;
        public SellViewHolder(@NonNull View itemView) {
            super(itemView);
            prodNameTV = itemView.findViewById(R.id.sellProdNameTV);
            prodDescTV = itemView.findViewById(R.id.sellProdDescTV);
            prodPriceTV = itemView.findViewById(R.id.sellProdPriceTV);
            prodDate = itemView.findViewById(R.id.sellProdDateTV);

            prodImageView = itemView.findViewById(R.id.csImageView);
        }
    }

    public SellAdapter(List<Product> products) {

        this.products = products;
    }

    @NonNull
    @Override
    public SellViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_sell, parent, false);

        return new SellViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SellViewHolder holder, int position) {
        final Product product = this.products.get(position);

        String date = DateFormat.getDateInstance().format(new Date(product.getUploadTime()));


        holder.prodNameTV.setText(product.getName());
        holder.prodDescTV.setText(product.getDescription());

        double priceUnformatted = product.getPrice();
        String priceFormatted = getStringPrice(priceUnformatted);

        holder.prodPriceTV.setText(String.format("MWK %s", priceFormatted));
        holder.prodDate.setText(date);

        Glide.with(holder.itemView.getContext())
                .load(product.getPhotoURI())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .into(holder.prodImageView);


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public String getStringPrice(double price) {
        String fPrice;
        NumberFormat numberFormatter;
        numberFormatter = NumberFormat.getNumberInstance(Locale.US);

        fPrice = numberFormatter.format(((int) price));

        return fPrice;
    }


}
