package com.example.ecom_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String CategoryName, Description, Price, PName, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButton;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPic = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        CategoryName = getIntent().getExtras().get("category").toString();
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButton = (Button) findViewById(R.id.add_new_product);
        InputProductImage = (ImageView) findViewById(R.id.select_product_image);
        InputProductName = (EditText) findViewById(R.id.product_name);
        InputProductDescription = (EditText) findViewById(R.id.product_description);
        InputProductPrice = (EditText) findViewById(R.id.product_price);
        loadingBar = new ProgressDialog(this);


        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();

            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {
        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        PName = InputProductName.getText().toString();

        if(ImageUri == null){
            Toast.makeText(AdminAddNewProductActivity.this, "Add Product Image", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Description)){
            Toast.makeText(AdminAddNewProductActivity.this, "Please Write Product Description", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price)){
            Toast.makeText(AdminAddNewProductActivity.this, "Please add product price", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(PName)){
            Toast.makeText(AdminAddNewProductActivity.this, "Please Write Product Name", Toast.LENGTH_SHORT).show();
        }
        else {
            StoreProductInformation();
        }

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void StoreProductInformation() {


        loadingBar.setTitle("Adding New Product");
        loadingBar.setMessage("Please Wait, while we are addding new product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();


        Calendar calender = Calendar.getInstance();


        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy ");
        saveCurrentDate = currentDate.format(calender.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calender.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this, "Error " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Image Uploaded Successfully", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw  task.getException();
                        }
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(AdminAddNewProductActivity.this, "Product Image Url get Successfully", Toast.LENGTH_SHORT).show();
                            
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }

            private void SaveProductInfoToDatabase() {

                HashMap<String, Object> productMap = new HashMap<>();
                productMap.put("pid", productRandomKey);
                productMap.put("data", saveCurrentDate);
                productMap.put("time", saveCurrentTime);
                productMap.put("description", Description);
                productMap.put("image", downloadImageUrl);
                productMap.put("category", CategoryName);
                productMap.put("price", Price);
                productMap.put("pname", PName);

                ProductRef.child(productRandomKey).updateChildren(productMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){

                                    Intent intent = new Intent(AdminAddNewProductActivity.this,  AdminCategoryAdtivity.class);
                                    startActivity(intent);

                                    finish();

                                    loadingBar.dismiss();
                                    Toast.makeText(AdminAddNewProductActivity.this,"Product Added Successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    loadingBar.dismiss();
                                    String message = task.getException().toString();
                                    Toast.makeText(AdminAddNewProductActivity.this,"Error " + message, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void OpenGallery() {
        Intent galleryintent = new Intent();
        galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent, GalleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GalleryPic && resultCode == RESULT_OK && data != null){

            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
