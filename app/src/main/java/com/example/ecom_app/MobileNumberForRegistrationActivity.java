package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MobileNumberForRegistrationActivity extends AppCompatActivity {

    private EditText editTextMobile;
    private Button continueBtn;
    private String parentDbName = "Users";
    private boolean exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_number_for_registration);

        editTextMobile = (EditText) findViewById(R.id.Mobile_for_registration);
        continueBtn = (Button) findViewById(R.id.buttonContinue);


        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = editTextMobile.getText().toString().trim();
                validatephonenumber(mobile);

                if(mobile.isEmpty() || mobile.length() < 10){
                    editTextMobile.setError("Enter a valid mobile");
                    editTextMobile.requestFocus();
                    return;
                }
                if(exist == true){
                    editTextMobile.setError("Mobile Number Already Exists");
                    editTextMobile.requestFocus();
                    return;
                }

                Intent intent = new Intent(MobileNumberForRegistrationActivity.this, VerificationCodeActivity.class);
                intent.putExtra("mobile", mobile);
                startActivity(intent);
            }
        });
    }

    private void validatephonenumber(final String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child(parentDbName);

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if ((dataSnapshot.child(parentDbName).child(phone).exists())) {
                    exist = true;
                }
                else {
                    exist = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        });
    }

//    private void validatephonenumber(final String phone) {
//        final DatabaseReference RootRef;
//        RootRef = FirebaseDatabase.getInstance().getReference().child(parentDbName);
//
//        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                if ((dataSnapshot.child(parentDbName).child(phone).exists())) {
//                    exist = true;
//                }
//                else {
//                    exist = false;
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//
//        });
//    }
}
