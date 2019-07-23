package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ecom_app.Model.AdminOrders;
import com.example.ecom_app.ViewHolder.AdminPendingOrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminNewOrdersActivity extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView pendingOrderList;
    private DatabaseReference orderref;
    private DatabaseReference AdminOrderRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_new_orders);

        orderref = FirebaseDatabase.getInstance().getReference().child("Orders");
        AdminOrderRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin view");

        pendingOrderList = findViewById(R.id.pending_order_list) ;
        pendingOrderList.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<AdminOrders> options =
                new FirebaseRecyclerOptions.Builder<AdminOrders>()
                .setQuery(orderref,AdminOrders.class)
                .build();

        FirebaseRecyclerAdapter<AdminOrders, AdminPendingOrderViewHolder> adapter =
                new FirebaseRecyclerAdapter<AdminOrders, AdminPendingOrderViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull AdminPendingOrderViewHolder holder, final int position, @NonNull AdminOrders model) {

                        holder.userName.setText("User Name: "+ model.getName());
                        holder.userPhoneNumber.setText("Phone Number: "+ model.getPhone());
                        holder.userTotalPrice.setText("Total Price "+ model.getTotalPrice());
                        holder.userDateTime.setText("Date-Time: "+ model.getDate());
                        holder.userShippingAddress.setText("Address: "+ model.getAddress());
                        holder.userShippingCity.setText("City: "+ model.getCity());

                        holder.ShowOrderBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String uID = getRef(position).getKey();

                                Intent intent = new Intent(AdminNewOrdersActivity.this, AdminUserProductActivity.class);
                                intent.putExtra("uid", uID);
                                startActivity(intent);
                            }
                        });
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                CharSequence options[] = new CharSequence[]{
                                        "Yes", "No"
                                };


                                final AlertDialog.Builder builder = new AlertDialog.Builder(AdminNewOrdersActivity.this);
                                builder.setTitle("Have you shifted the order or product?");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(i == 0)
                                        {
                                            String uID = getRef(position).getKey();

                                            removeOrder(uID);

                                        }
                                        else {
//                                            Intent intent = new Intent(AdminNewOrdersActivity.this, AdminNewOrdersActivity.class);
//                                            startActivity(intent);
                                        }
                                    }
                                });
                                builder.show();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public AdminPendingOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_pending_orders_layout, parent, false);
                        return new AdminPendingOrderViewHolder(view);
                    }
                };

        pendingOrderList.setAdapter(adapter);
        adapter.startListening();
    }

    private void removeOrder(String uID) {
        orderref.child(uID).removeValue();
        AdminOrderRef.child(uID).removeValue();

    }

}
