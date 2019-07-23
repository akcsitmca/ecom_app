package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ecom_app.Prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserOrderActivity extends AppCompatActivity {
    
    private TextView userOrderName, userOrderPhoneNumber,userOrderTotalPrice, userOrderAddress, userOrderCity, userOrderDateTime, userOrderState, noOrder;
    private Button showDetail, cancelOrderBtn;
    private RelativeLayout existOrder;
    private DatabaseReference orderRef;
    private DatabaseReference AdminOrderRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_order);
        
        userOrderName = (TextView) findViewById(R.id.user_order_name);
        userOrderPhoneNumber = (TextView) findViewById(R.id.user_order_phone_number);
        userOrderTotalPrice = (TextView) findViewById(R.id.user_order_total_price);
        userOrderAddress = (TextView) findViewById(R.id.user_order_address);
        userOrderCity = (TextView) findViewById(R.id.user_order_city);
        userOrderDateTime = (TextView) findViewById(R.id.user_order_date_time);
        userOrderState = (TextView) findViewById(R.id.user_order_state);
        noOrder = (TextView) findViewById(R.id.no_order);
        
        showDetail = (Button) findViewById(R.id.user_order_details_btn);
        cancelOrderBtn = (Button) findViewById(R.id.user_order_cancel_btn);

        existOrder = (RelativeLayout) findViewById(R.id.list_undelivered);

        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders");
        AdminOrderRef = FirebaseDatabase.getInstance().getReference().child("Cart List").child("Admin view");
        
        showDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(UserOrderActivity.this, AdminUserProductActivity.class);
                intent.putExtra("uid", prevalent.CurrentOnlineUser.getPhone());
                startActivity(intent);
            }
        });

        cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CharSequence options[] = new CharSequence[]{
                        "Yes", "No"
                };

                final AlertDialog.Builder builder = new AlertDialog.Builder(UserOrderActivity.this);
                builder.setTitle("Do You Want To Cancel Your Order?");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0)
                        {
                            removeOrder();
                        }
                        else {
                        }
                    }
                });
                builder.show();
            }
        });
    }

    private void removeOrder() {
        orderRef.child(prevalent.CurrentOnlineUser.getPhone()).removeValue();
        AdminOrderRef.child(prevalent.CurrentOnlineUser.getPhone()).removeValue();
    }

    @Override
    protected void onStart() {
        super.onStart();

        CheckCurrentOrder();
    }

    private void CheckCurrentOrder() {

        //DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(prevalent.CurrentOnlineUser.getPhone());

        orderRef.child(prevalent.CurrentOnlineUser.getPhone()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    noOrder.setVisibility(View.GONE);
                    existOrder.setVisibility(View.VISIBLE);

                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();
                    String TotalPrice = dataSnapshot.child("TotalPrice").getValue().toString();
                    String address = dataSnapshot.child("address").getValue().toString();
                    String city = dataSnapshot.child("city").getValue().toString();
                    String date = dataSnapshot.child("date").getValue().toString();
                    String phone = dataSnapshot.child("phone").getValue().toString();
                    String time = dataSnapshot.child("time").getValue().toString();

                    //userOrderName, userOrderPhoneNumber,userOrderTotalPrice, userOrderAddress, userOrderCity, userOrderDateTime, userOrderState

                    userOrderName.setText("User Name: "+userName);
                    userOrderPhoneNumber.setText("Phone Number: "+phone);
                    userOrderTotalPrice.setText("Total Price: "+TotalPrice);
                    userOrderAddress.setText("Address: "+address);
                    userOrderCity.setText("City: "+city);
                    userOrderDateTime.setText("Date-Time: "+date+" "+time);
                    userOrderState.setText("State: "+shippingState);

                }
                else {
                    noOrder.setVisibility(View.VISIBLE);
                    existOrder.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
