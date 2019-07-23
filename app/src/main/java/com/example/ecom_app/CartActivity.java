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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ecom_app.Model.Cart;
import com.example.ecom_app.Model.Users;
import com.example.ecom_app.Prevalent.prevalent;
import com.example.ecom_app.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CartActivity extends AppCompatActivity {


    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessButton;
    private TextView txtTotalAmount, txtmsg1;
    private int overTotalPrice = 0;
    private String quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager  = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        nextProcessButton = (Button) findViewById(R.id.next_process_btn);
        txtTotalAmount = (TextView) findViewById(R.id.total_price);
        txtmsg1 = (TextView) findViewById(R.id.msg1);

        nextProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(overTotalPrice == 0)
                {
                    Toast.makeText(CartActivity.this, "Please add atleast one item to the cart to proceed", Toast.LENGTH_LONG).show();
                }
                else if(overTotalPrice > 0)
                {
                    Intent intent = new Intent(CartActivity.this, ConfirmFinalOrder.class);
                    intent.putExtra("Total Price", String.valueOf(overTotalPrice));
                    startActivity(intent);
                }
            }
        });


    }

    protected void onStart() {

        super.onStart();
        CheckOrderState();

        overTotalPrice = 0;



        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        FirebaseRecyclerOptions<Cart> options = new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User view")
                        .child(prevalent.CurrentOnlineUser.getPhone())
                        .child("Products"), Cart.class).build();


        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int i, @NonNull final Cart cart) {
                holder.txtProductQuantity.setText("Quantity = "+cart.getQuantity());
                holder.txtProductName.setText("Name = "+cart.getPname());
                holder.txtProductPrice.setText("Price for one item Rs. "+cart.getPrice());

                int oneTimeProductTotalPrice = ((Integer.valueOf(cart.getPrice()))) * Integer.valueOf(cart.getQuantity());
                overTotalPrice += oneTimeProductTotalPrice;

                txtTotalAmount.setText("Total Price: Rs."+String.valueOf(overTotalPrice));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        CharSequence options[] = new CharSequence[]{
                                "Edit", "Remove"
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options");

                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(i == 0)
                                {

                                    //RootRef = FirebaseDatabase.getInstance().getReference().child(parentDbName);
//                                    if(dataSnapshot.child(parentDbName).child(phone).exists()){
//                                        Users userData = dataSnapshot.child(parentDbName).child(phone).getValue(Users.class);
                                    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference()
                                            .child("Cart List")
                                            .child("User view")
                                            .child(prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products");

                                    orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            Cart cartData = dataSnapshot.child(cart.getPid()).getValue(Cart.class);
                                            quantity = cartData.getQuantity();
                                            Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                            intent.putExtra("pid", cart.getPid());
                                            intent.putExtra("quantity", quantity);
                                            startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });


//                                    Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
//                                    Toast.makeText(CartActivity.this,"Quantity: "+quantity, Toast.LENGTH_SHORT).show();

//                                    intent.putExtra("pid", cart.getPid());
//                                    intent.putExtra("quantity", quantity);
//                                    startActivity(intent);
                                }
                                if(i == 1) {
                                    cartListRef.child("User view")
                                            .child(prevalent.CurrentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(cart.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        Toast.makeText(CartActivity.this, "Product Removed Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                        startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                        builder.show();
                    }
                });

            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();


    }

    private void CheckOrderState(){
        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevalent.CurrentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
//                    if(shippingState.equals("shipped")){
//                        txtTotalAmount.setText("Your order is shipped with total amount = Rs."+dataSnapshot.child("TotalPrice").getValue().toString() + "You will receive it shortly");
//                        recyclerView.setVisibility(View.GONE);
//
//                        txtmsg1.setVisibility(View.VISIBLE);
//                        nextProcessButton.setVisibility(View.GONE);
//
//                        Toast.makeText(CartActivity.this, "You can purchase next product after you receive your previous order", Toast.LENGTH_LONG).show();
//
//                    }
                    if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Order will be delivered soon");
                        recyclerView.setVisibility(View.GONE);


                        txtmsg1.setText("An order with total amount Rs. "+dataSnapshot.child("TotalPrice").getValue().toString()+" is placed successfully");
                        txtmsg1.setVisibility(View.VISIBLE);
                        nextProcessButton.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "You can purchase next product after you receive your previous order", Toast.LENGTH_LONG).show();

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
