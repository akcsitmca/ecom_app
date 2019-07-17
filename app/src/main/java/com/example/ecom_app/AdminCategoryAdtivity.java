package com.example.ecom_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryAdtivity extends AppCompatActivity {

    private ImageView t_shirts, sports_t_shirts, female_dress, sweaters, glasses, purse_bag, hat, shoes, headphone, laptop, watch, mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category_adtivity);

        t_shirts = (ImageView) findViewById(R.id.t_shirts);
        sports_t_shirts = (ImageView) findViewById(R.id.sports_t_shirts);
        female_dress = (ImageView) findViewById(R.id.female_dress);
        sweaters = (ImageView) findViewById(R.id.sweaters);
        glasses = (ImageView) findViewById(R.id.glasses);
        purse_bag = (ImageView) findViewById(R.id.purse_bag);
        hat = (ImageView) findViewById(R.id.hat);
        shoes = (ImageView) findViewById(R.id.shoes);
        headphone = (ImageView) findViewById(R.id.headphone);
        laptop = (ImageView) findViewById(R.id.laptop);
        watch = (ImageView) findViewById(R.id.watch);
        mobile = (ImageView) findViewById(R.id.mobile);


        t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "t_shirts");
                startActivity(intent);
            }
        });

        sports_t_shirts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "sports_t_shirts");
                startActivity(intent);
            }
        });

        female_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "female_dress");
                startActivity(intent);
            }
        });

        sweaters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "sweaters");
                startActivity(intent);
            }
        });

        glasses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "glasses");
                startActivity(intent);
            }
        });

        purse_bag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "purse_bag");
                startActivity(intent);
            }
        });

        hat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "hat");
                startActivity(intent);
            }
        });

        shoes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "shoes");
                startActivity(intent);
            }
        });

        headphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "headphone");
                startActivity(intent);
            }
        });

        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "laptop");
                startActivity(intent);
            }
        });

        watch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "watch");
                startActivity(intent);
            }
        });

        mobile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(AdminCategoryAdtivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "mobile");
                startActivity(intent);
            }
        });
    }
}
