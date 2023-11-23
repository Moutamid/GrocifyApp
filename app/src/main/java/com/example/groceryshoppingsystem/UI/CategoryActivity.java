package com.example.groceryshoppingsystem.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.CategoryProductInfoAdapter;
import com.example.groceryshoppingsystem.Adapters.CategoryproductAdapter;
import com.example.groceryshoppingsystem.Model.CategoryProductInfo;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ArrayList<CategoryProductInfo> CategoryProducts;
    private CategoryProductInfoAdapter adapter;
    TextView PageTitle;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;
    private CategoryProductInfoAdapter.RecyclerViewClickListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        PageTitle=findViewById(R.id.PageTitle);
        PageTitle.setText(CategoryproductAdapter.title_name);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        UserId = CurrentUser.getUid();
        onClickAnyProduct();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setCategoryData();

    }

    private void onClickAnyProduct() {
        listener = new CategoryProductInfoAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                CategoryProductInfo product = CategoryProducts.get(position);

                Intent intent = new Intent(CategoryActivity.this, ProductInfoActivity.class);
                intent.putExtra("Product Name", product.getProductName());
                intent.putExtra("Product Price", product.getProductPrice());
                intent.putExtra("Product Image", product.getProductImage());
                intent.putExtra("Product ExpiryDate", product.getProductExpiryDate());
                intent.putExtra("Product IsFavorite", String.valueOf(product.getIsFavorite()));
                intent.putExtra("Is Offered", "no");

                startActivity(intent);
            }
        };
    }


    private void setCategoryData() {
        //toolbar

        recyclerView = (RecyclerView) findViewById(R.id.CategoryRecycler);
        CategoryProducts = new ArrayList<>();

        adapter = new CategoryProductInfoAdapter(CategoryActivity.this, CategoryProducts, listener);
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
        recyclerView.setAdapter(adapter);
        getProductsData();


    }

    private void getProductsData() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference m = root.child("product").child(CategoryproductAdapter.title_name);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        final String ProductName = dataSnapshot.getKey().toString();
                        final String ProductPrice = dataSnapshot.child("price").getValue().toString();
                        final String ProductImage = dataSnapshot.child("image").getValue().toString();
                        final String ProductExpiryDate = dataSnapshot.child("details").getValue().toString();

                        //check favorites
                        DatabaseReference Root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
                        DatabaseReference x = Root.child("favourites").child(UserId).child(ProductName);
                        ValueEventListener vvalueEventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    CategoryProducts.add(new CategoryProductInfo(ProductImage, ProductName, ProductPrice, ProductExpiryDate, true));
                                } else {
                                    CategoryProducts.add(new CategoryProductInfo(ProductImage, ProductName, ProductPrice, ProductExpiryDate, false));
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        };
                        x.addListenerForSingleValueEvent(vvalueEventListener);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

    public void backPress(View view) {
        onBackPressed();
    }
}