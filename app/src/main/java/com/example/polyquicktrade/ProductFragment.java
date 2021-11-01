package com.example.polyquicktrade;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.polyquicktrade.pojo.Product;
import com.example.polyquicktrade.pojo.User;
import com.example.polyquicktrade.ui.UserAccViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class ProductFragment extends Fragment implements View.OnClickListener {

    private SharedViewModel sharedViewModel;
    private UserAccViewModel userAccViewModel;
    private TextView pName;
    private TextView pPrice;
    private TextView pLocation;
    private TextView pDesc;
    private TextView pDate;
    private TextView pWishlist;
    private TextView sName;
    private TextView sNumber;
    private ImageView pPhoto;
    private Button buyNowbtn;
    private Button cartBtn;
    private RecyclerView deliveryRecyclerView;
    private RecyclerView paymentRecyclerView;
    private GridLayoutManager layoutManager;
    private ProductFragmentAdapter mAdapter;
    private GridLayoutManager layoutManager2;
    private Product mProduct;
    private FirebaseUser currentUser;

    public ProductFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_product, container, false);
        sharedViewModel =
                new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        userAccViewModel =
                new ViewModelProvider(this).get(UserAccViewModel.class);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        initViews(v);

        sharedViewModel.getProduct().observe(getViewLifecycleOwner(), new Observer<Product>() {
            @Override
            public void onChanged(Product product) {
                mProduct = product;

                updateUI(product);
            }
        });
        return v;
    }

    private void initViews(View v) {

        pName = v.findViewById(R.id.prodNameView);
        pPrice = v.findViewById(R.id.prodPriceView);
        pLocation = v.findViewById(R.id.prodLocationView);
        pDesc = v.findViewById(R.id.prodDescView);
        pDate= v.findViewById(R.id.prodAddDateView);
        pWishlist = v.findViewById(R.id.prodWishlistView);
        sName = v.findViewById(R.id.prodSellerNameView);
        sNumber = v.findViewById(R.id.prodSellerNumberView);
        pPhoto = v.findViewById(R.id.prodPhotoView);
        buyNowbtn = v.findViewById(R.id.prodBuyNowView);
        cartBtn = v.findViewById(R.id.prodCartView);
        pWishlist.setOnClickListener(this);
        cartBtn.setOnClickListener(this);

        deliveryRecyclerView = v.findViewById(R.id.deliveryOptionsRecycler);
        paymentRecyclerView = v.findViewById(R.id.paymentOptionsRecycler);

        layoutManager = new GridLayoutManager(this.getContext(), 4);
        layoutManager2 = new GridLayoutManager(this.getContext(), 3);

        deliveryRecyclerView.setLayoutManager(layoutManager);
        paymentRecyclerView.setLayoutManager(layoutManager2);


    }

    private void updateUI(Product product) {

        String date = DateFormat.getDateInstance().format(new Date(product.getUploadTime()));

        pName.setText(product.getName());

        double priceUnformatted = product.getPrice();
        String priceFormatted = getStringPrice(priceUnformatted);

        pPrice.setText(String.format("MWK %s", priceFormatted));
        pLocation.setText(product.getLocation());
        pDesc.setText(product.getDescription());
        pDate.setText(date);
        sName.setText("Khumbolawo Mussa");
        sNumber.setText(product.getPhoneNumber());

        Glide.with(this)
                .load(product.getPhotoURI())
                .centerCrop()
                .placeholder(new ColorDrawable(Color.LTGRAY))
                .into(pPhoto);

        List<String> deliveryOptions = product.getDeliveryOptions();
        List<String> paymentOptions = product.getPaymentOptions();

//        userAccViewModel.getSellerFromFirebase(product.getSeller());

//        userAccViewModel.getUserMutableLiveData().observe(this, new Observer<User>() {
//            @Override
//            public void onChanged(User user) {
//                sName.setText(user.getUserName());
//            }
//        });






        mAdapter = new ProductFragmentAdapter(deliveryOptions);
        deliveryRecyclerView.setAdapter(mAdapter);

        mAdapter = new ProductFragmentAdapter(paymentOptions);
        paymentRecyclerView.setAdapter(mAdapter);



    }

    public String getStringPrice(double price) {
        String fPrice;
        NumberFormat numberFormatter;
        numberFormatter = NumberFormat.getNumberInstance(Locale.US);

        fPrice = numberFormatter.format(((int) price));

        return fPrice;
    }



    @Override
    public void onClick(View view) {



        if (currentUser != null){

            if (view.getId() == R.id.prodWishlistView) {
                userAccViewModel.addToWishlistFirebase(mProduct, currentUser);
                Toast.makeText(requireContext(), "Added to Wishlist", Toast.LENGTH_LONG).show();

            }else if (view.getId() == R.id.prodCartView){

                userAccViewModel.addToCartFirebase(mProduct, currentUser);
                Toast.makeText(requireContext(), "Added to Cart", Toast.LENGTH_LONG).show();

            }

        }else{
            Toast.makeText(requireContext(), "You must first login", Toast.LENGTH_LONG).show();
        }



    }

    private class ProductFragmentAdapter extends RecyclerView.Adapter<ProductFragmentAdapter.ProductFragmentViewHolder> {

        private List<String> options;

        public ProductFragmentAdapter(List<String> options) {
            this.options = options;
        }

        class ProductFragmentViewHolder extends RecyclerView.ViewHolder{
            TextView optionTextView;
            public ProductFragmentViewHolder(@NonNull View itemView) {
                super(itemView);
                optionTextView = itemView.findViewById(R.id.OptionTextView);
            }
        }

        @NonNull
        @Override
        public ProductFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.custom_product_fragment, parent, false);

            return new ProductFragmentViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductFragmentViewHolder holder, int position) {

            holder.optionTextView.setText(options.get(position));

        }

        @Override
        public int getItemCount() {
            return options.size();
        }




    }
}
