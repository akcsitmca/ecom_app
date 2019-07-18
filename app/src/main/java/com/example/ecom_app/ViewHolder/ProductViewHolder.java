package com.example.ecom_app.ViewHolder;

import android.media.Image;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom_app.Interface.ItemClickListner;
import com.example.ecom_app.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtItemName, txtItemdescription, txtItemPrice;
    public ImageView imageView;
    public ItemClickListner listner;

    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.item_image);
        txtItemName = (TextView) itemView.findViewById(R.id.item_name);
        txtItemdescription = (TextView) itemView.findViewById(R.id.item_description);
        txtItemPrice = (TextView) itemView.findViewById(R.id.item_price);
    }

    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(), false);
    }
}
