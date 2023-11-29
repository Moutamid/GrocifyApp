package com.example.groceryshoppingsystem.UI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshoppingsystem.Model.Product;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AddProduct extends AppCompatActivity {
    private EditText name, quantity, price, productDescription;
    private Button add, choose;
    private ImageView img;
    private Uri imgUri;
    private String category;
    private StorageReference mStorageRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private DatabaseReference m;

    final List<String> lastmodels = new ArrayList<>();
    TextView name_cat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);
        name = findViewById(R.id.editTextName);
        name_cat = findViewById(R.id.name);
        quantity = findViewById(R.id.editTextAvailableAmount);
        add = (Button) findViewById(R.id.btnAddCatogry);
        choose = (Button) findViewById(R.id.btnChooseCatogryImage);
        img = (ImageView) findViewById(R.id.CatogryImage);
        price = findViewById(R.id.editTextPrice);
        productDescription = findViewById(R.id.editTextDescription);
        spinner = (Spinner) findViewById(R.id.spinner);

        m = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("categories");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    lastmodels.add(ds.getKey().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(AddProduct.this, android.R.layout.simple_spinner_item, lastmodels);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        category = adapterView.getItemAtPosition(i).toString();
//                        Log.d("AddProduct", "Selected category: " + category);  // Add this line for logging
//                        Toast.makeText(AddProduct.this, "Selected category: " + category, Toast.LENGTH_SHORT).show();
//                        name_cat.setText(category);
                        name_cat.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                        Log.d("AddProduct", "Nothing selected");  // Add this line for logging
                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);


        mStorageRef = FirebaseStorage.getInstance().getReference("products");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddProduct.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty() || imgUri == null) {
                    Toast.makeText(AddProduct.this, "Please fill blank fields", Toast.LENGTH_SHORT).show();
                } else {
                    uploadData();
                    Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
    }


    public void uploadData() {
        if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty() || imgUri == null) {
            Toast.makeText(AddProduct.this, "Please fill blank fields", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Product product = new Product(quantity.getText().toString().trim(),
                            price.getText().toString().trim(),
                            productDescription.getText().toString().trim(),
                            downloadUrl.toString(), category);
                    DatabaseReference z = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp")
                            .child("product")
                            .child(category)
                            .child(name.getText().toString());
                    z.setValue(product);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddProduct.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void openImage() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, RegisterActivity.GALARY_PICK);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imgUri = data.getData();
            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString(), e.getMessage().toString());
            }

        }
    }


    public void backPress(View view) {
        onBackPressed();
    }
}