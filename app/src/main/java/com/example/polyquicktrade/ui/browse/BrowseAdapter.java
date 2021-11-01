package com.example.polyquicktrade.ui.browse;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;
import com.example.polyquicktrade.pojo.User;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class BrowseAdapter extends RecyclerView.Adapter<BrowseAdapter.BrowseViewHolder> {
    private List<Product> products;
    private List<User> users;
    private RecyclerClickListener<Product> clickListener;


    public BrowseAdapter(List<Product> products, ArrayList<User> users, RecyclerClickListener<Product> clickListener) {
        if (products == null)
            this.products = new ArrayList<>();

        this.products = products;

        this.users = users;

        this.clickListener = clickListener;

    }

    public static class BrowseViewHolder extends RecyclerView.ViewHolder{

        TextView name, price, seller, location;
        ImageView productPhoto;
        CardView cardView;        class ProductFragmentViewHolder extends RecyclerView.ViewHolder{

            public ProductFragmentViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }

        public void bindListener(final Product product, final RecyclerClickListener<Product> clickListener){

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(product, view);

                }
            });

        }

        public BrowseViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cbName);
            price = itemView.findViewById(R.id.cbPrice);
            seller = itemView.findViewById(R.id.cbSeller);
            location = itemView.findViewById(R.id.cbLocation);
            productPhoto = itemView.findViewById(R.id.cbImageView);
            cardView = itemView.findViewById(R.id.browseCardView);

        }
    }


    @NonNull
    @Override
    public BrowseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_browse_view, parent, false);

        return new BrowseViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final BrowseViewHolder holder, int position) {

        final Product product = this.products.get(position);
        holder.name.setText(product.getName());
        double priceUnformatted = product.getPrice();
        String priceFormatted = getStringPrice(priceUnformatted);
        holder.price.setText(String.format("MWK %s", priceFormatted));
        holder.location.setText(product.getLocation());
        Glide.with(holder.itemView.getContext())
                .load(product.getPhotoURI())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .into(holder.productPhoto);

        holder.bindListener(product, clickListener);


        if (users!= null){

            for (User user :users) {
                if (product.getSeller().equals(user.getUid())){

                    holder.seller.setText(user.getUserName());
                    break;
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

    @Override
    public int getItemCount() {
        return products.size();//TODO
    }

    public String getStringPrice(double price) {
        String fPrice;
        NumberFormat numberFormatter;
        numberFormatter = NumberFormat.getNumberInstance(Locale.US);

        fPrice = numberFormatter.format(((int) price));

        return fPrice;
    }



}

