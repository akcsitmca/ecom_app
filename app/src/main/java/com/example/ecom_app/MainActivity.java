package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button btn_login, btn_signup;
    private int INTERNET_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = (Button) findViewById(R.id.main_login);
        btn_signup = (Button) findViewById(R.id.main_signup);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){


                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                else {
                    requestInternetPermission();

                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }

//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED){

                    Toast.makeText(MainActivity.this, "have Permission", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
                else {
                    requestInternetPermission();

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }

//                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
//                startActivity(intent);
            }
        });
    }

    private void requestInternetPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("To create or access your account internet access is required")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);

                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.INTERNET}, INTERNET_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == INTERNET_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();

            }
        }
    }
}
