package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.ecom_app.Prevalent.prevalent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmFinalOrder extends AppCompatActivity {

    private EditText nameEditTxt, phoneEditTxt, addressEditTxt, cityEditTxt, amountEditTxt;
    private Button confirmOrderBtn;
    private String overTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_final_order);

        overTotalPrice = getIntent().getStringExtra("Total Price");

        confirmOrderBtn = (Button) findViewById(R.id.final_order_confirm_btn);
        nameEditTxt = (EditText) findViewById(R.id.final_order_name);
        phoneEditTxt = (EditText) findViewById(R.id.final_order_phone);
        addressEditTxt = (EditText) findViewById(R.id.final_order_address);
        cityEditTxt = (EditText) findViewById(R.id.final_order_city);
        amountEditTxt = (EditText) findViewById(R.id.final_order_amount);

        amountEditTxt.setText("Rs. "+overTotalPrice);

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check();
            }
        });

        nameEditTxt.setText(prevalent.CurrentOnlineUser.getName());
        phoneEditTxt.setText(prevalent.CurrentOnlineUser.getPhone());
    }

    private void check() {
        if(TextUtils.isEmpty(nameEditTxt.getText().toString())){

            Toast.makeText(ConfirmFinalOrder.this,"Please provide your name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditTxt.getText().toString())){

            Toast.makeText(ConfirmFinalOrder.this,"Please provide your phone number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditTxt.getText().toString())){

            Toast.makeText(ConfirmFinalOrder.this,"Please provide your shipping address", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(cityEditTxt.getText().toString())){

            Toast.makeText(ConfirmFinalOrder.this,"Please provide your city", Toast.LENGTH_SHORT).show();
        }
        else {
            confirmOrder();
        }
    }

    private void confirmOrder() {


        final String saveCurrentTime, saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentDate.format(calForDate.getTime());

        final DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(prevalent.CurrentOnlineUser.getPhone());



        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("TotalPrice", overTotalPrice);
        orderMap.put("name", nameEditTxt.getText().toString());
        orderMap.put("phone", phoneEditTxt.getText().toString());
        orderMap.put("address", addressEditTxt.getText().toString());
        orderMap.put("city", cityEditTxt.getText().toString());
        orderMap.put("date", saveCurrentDate);
        orderMap.put("time", saveCurrentTime);
        orderMap.put("state", "not shipped");

        orderRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User view")
                            .child(prevalent.CurrentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(ConfirmFinalOrder.this,"Your Order is placed successfully", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ConfirmFinalOrder.this, HomeActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });

    }



}
