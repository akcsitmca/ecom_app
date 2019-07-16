package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Hashtable;

public class RegisterActivity extends AppCompatActivity {

    private Button CreateAccountBtn;
    private EditText InputName, InputPhoneNumber, InputPassword;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        CreateAccountBtn = (Button) findViewById(R.id.register_btn);
        InputName = (EditText) findViewById(R.id.register_username);
        InputPhoneNumber = (EditText) findViewById(R.id.register_ph_no);
        InputPassword = (EditText) findViewById(R.id.register_password);
        loadingBar = new ProgressDialog(this);

        CreateAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });
    }

    private void createAccount() {
        String name = InputName.getText().toString();
        String phone = InputPhoneNumber.getText().toString();
        String password = InputPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(RegisterActivity.this, "Please write your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(phone)) {
            Toast.makeText(RegisterActivity.this, "Please write phone number", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(RegisterActivity.this, "Please write password", Toast.LENGTH_SHORT).show();
        } else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please Wait, we are checking credentials");
            loadingBar.setCancelable(false);
            loadingBar.show();

//            final DatabaseReference RootRef;
//            RootRef = FirebaseDatabase.getInstance().getReference();
//
//            Hashtable<String, Object> userdateMap = new Hashtable<>();
//            Toast.makeText(RegisterActivity.this,"userdatemap created",Toast.LENGTH_SHORT).show();
//            userdateMap.put("phone", phone);
//            Toast.makeText(RegisterActivity.this,"phone updated",Toast.LENGTH_SHORT).show();
//            userdateMap.put("password", password);
//            Toast.makeText(RegisterActivity.this,"password updated",Toast.LENGTH_SHORT).show();
//            userdateMap.put("name", name);
//            Toast.makeText(RegisterActivity.this,"userdatemap updated",Toast.LENGTH_SHORT).show();
//
//            RootRef.child("phone").child(phone).updateChildren(userdateMap)
//                    .addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                            if (task.isSuccessful()){
//                                Toast.makeText(RegisterActivity.this, "Congratulation, your account is created", Toast.LENGTH_SHORT).show();
//                                loadingBar.dismiss();
//
//                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                                startActivity(intent);
//
//                                finish();
//                            }
//                            else {
//                                loadingBar.dismiss();
//                                Toast.makeText(RegisterActivity.this, "Network Issue, Please try again", Toast.LENGTH_SHORT).show();
//
//                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                                startActivity(intent);
//
//                                finish();
//                            }
//                        }
//                    });


            validatephonenumber(name, phone, password);
        }
    }

    private void validatephonenumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference().child("Users");

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!(dataSnapshot.child("Users").child(phone).exists())){
                    HashMap<String, Object> userdateMap = new HashMap<>();
                    userdateMap.put("phone", phone);
                    userdateMap.put("password", password);
                    userdateMap.put("name", name);

                    RootRef.child("Users").child(phone).updateChildren(userdateMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulation, your account is created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        startActivity(intent);

                                        finish();
                                    }
                                    else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Network Issue, Please try again", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);

                                        finish();
                                    }
                                }
                            });
                }
                else {
                    Toast.makeText(RegisterActivity.this, "Phone Number " + phone + " already exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Toast.makeText(RegisterActivity.this, "Please try with another phone number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
