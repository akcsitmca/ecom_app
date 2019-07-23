package com.example.ecom_app.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecom_app.Interface.ItemClickListner;
import com.example.ecom_app.R;

public class AdminPendingOrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView userName, userPhoneNumber, userTotalPrice, userDateTime, userShippingAddress, userShippingCity;
    public Button ShowOrderBtn;
    private ItemClickListner itemClickListner;

    public AdminPendingOrderViewHolder(View itemView) {
        super(itemView);

        userName = itemView.findViewById(R.id.order_user_name);
        userPhoneNumber = itemView.findViewById(R.id.order_phone_number);
        userTotalPrice = itemView.findViewById(R.id.order_total_price);
        userDateTime = itemView.findViewById(R.id.order_date_time);
        userShippingAddress = itemView.findViewById(R.id.order_address);
        userShippingCity =itemView.findViewById(R.id.order_city);
        ShowOrderBtn = itemView.findViewById(R.id.order_details_btn);
    }


    public void onClick(View view) {

        itemClickListner.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner) {
        this.itemClickListner = itemClickListner;
    }


}
