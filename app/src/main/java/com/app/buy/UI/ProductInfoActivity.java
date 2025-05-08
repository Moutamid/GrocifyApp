package com.app.buy.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.buy.Helper.Constants;
import com.app.buy.Model.Medicine;
import com.app.buy.Model.PharmacyModel;
import com.app.buy.R;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductInfoActivity extends AppCompatActivity {

    TextView details;
    Switch switch_currency;
    //xml views
    private TextView PName, PCategory, PAmount, PPrice, tv_price_label, pharmacy_name;
    private ImageView Back, ProductImage;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();
//        Constants.initDialog(this);
//        Constants.showDialog();
        DefineXmlViews();
        Medicine currentMedicine = (Medicine) Stash.getObject("current_medicine", Medicine.class);
        Log.d("FirebaseData", currentMedicine.getName() + "-----");
        Log.d("FirebaseData", currentMedicine.getPharmacy_name() + "-----");
        Log.d("FirebaseData", currentMedicine.getCountry() + "-----");
        Log.d("FirebaseData", currentMedicine.getPharmacy_id() + "-----");
        Log.d("FirebaseData", currentMedicine.getPrice() + "-----");
        Log.d("FirebaseData", currentMedicine.getQuantity() + "-----");
        Log.d("FirebaseData", currentMedicine.getPriceGNF() + "-----");
        Log.d("FirebaseData", currentMedicine.getImage() + "-----");
        Log.d("FirebaseData", currentMedicine.getCategory() + "-----");
        Log.d("FirebaseData", currentMedicine.getDetails() + "-----");
        Log.d("FirebaseData", currentMedicine.getLat() + "-----");

        pharmacy_name.setText(currentMedicine.getPharmacy_name());
        getData(currentMedicine.getPharmacy_id());
    }


    private void DefineXmlViews() {
        pharmacy_name = (TextView) findViewById(R.id.pharmacy_name);
        PName = (TextView) findViewById(R.id.ProductName);
        PCategory = (TextView) findViewById(R.id.ProductCategory);
        PAmount = (TextView) findViewById(R.id.ProductAvailableAmount);
        tv_price_label = (TextView) findViewById(R.id.tv_price_label);
        PPrice = (TextView) findViewById(R.id.tv_price_value);
        details = (TextView) findViewById(R.id.details);
        Back = (ImageView) findViewById(R.id.BackBtn);
        switch_currency = (Switch) findViewById(R.id.switch_currency);
        ProductImage = (ImageView) findViewById(R.id.ProductImage);
        Medicine currentMedicine = (Medicine) Stash.getObject("current_medicine", Medicine.class);
        PName.setText(currentMedicine.getName());
        PCategory.setText(currentMedicine.getCategory());
        PAmount.setText(currentMedicine.getQuantity());
        PPrice.setText(currentMedicine.getPrice());
        details.setText(currentMedicine.getDetails());
        Glide.with(this).load(currentMedicine.getImage()).into(ProductImage);

        if (currentMedicine.getPrice().equals("0")) {
            tv_price_label.setText("Price in GNF");
            PPrice.setText(currentMedicine.getPriceGNF());

        } else {
            tv_price_label.setText("Price in CFA");
            PPrice.setText(currentMedicine.getPrice());


        }


        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductInfoActivity.this, FullImageActivity.class);
                intent.putExtra("ProductImage", currentMedicine.getImage());
                startActivity(intent);
            }
        });
    }


    public void backPress(View view) {
        onBackPressed();

    }

    public void pharmacy(View view) {
        Intent intent = new Intent(ProductInfoActivity.this, PharmacyDetailsActivity.class);
        startActivity(intent);

    }

    public void getData(String id) {
        FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("Pharmacies").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PharmacyModel category = snapshot.getValue(PharmacyModel.class);
                    Stash.put("currentPharmacyModel", category);
                    Stash.put("pharmacy_id", id);
                    Log.d("dtaaaa", category.getName() + "   name");
             }
                else
                {
                    Toast.makeText(ProductInfoActivity.this, "Pharmacy is not exist", Toast.LENGTH_SHORT).show();
                }
                    Constants.dismissDialog();
                }

                @Override
                public void onCancelled (@NonNull DatabaseError error){
                    Toast.makeText(ProductInfoActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }

    }