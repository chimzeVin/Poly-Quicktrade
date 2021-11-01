package com.example.polyquicktrade.ui.ask;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polyquicktrade.pojo.Enquiry;
import com.example.polyquicktrade.R;
import com.example.polyquicktrade.RecyclerClickListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

class AskAdapter extends RecyclerView.Adapter<AskAdapter.AskViewHolder> {


    private final List<Enquiry> enquiries;
    private final RecyclerClickListener<Enquiry> clickListener;

    public static class AskViewHolder extends RecyclerView.ViewHolder{

        TextView enquiryNameTV, enquireDescTV;
        CardView cardView;

        public AskViewHolder(@NonNull View itemView) {
            super(itemView);

            enquiryNameTV = itemView.findViewById(R.id.EnquiryNameTextView);
            enquireDescTV = itemView.findViewById(R.id.EnquiryDescTextView);
            cardView = itemView.findViewById(R.id.EnquiryCardView);
        }

        public void bindListener(final Enquiry enquiry, final RecyclerClickListener<Enquiry> clickListener) {

            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onClick(enquiry, view);
                }
            });
        }
    }

    public AskAdapter(List<Enquiry> enquiries, RecyclerClickListener<Enquiry> clickListener) {

        this.enquiries = enquiries;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public AskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.custom_ask_view, parent, false);

        return new AskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AskViewHolder holder, int position) {

        final Enquiry enquiry = this.enquiries.get(position);

        holder.enquiryNameTV.setText(enquiry.getProductName());
        holder.enquireDescTV.setText(enquiry.getProductDesc());

        holder.bindListener(enquiry, this.clickListener);

    }

    @Override
    public int getItemCount() {
        return enquiries.size();
    }
}
