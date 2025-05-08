package com.app.buy.UI;

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

import com.app.buy.Helper.Constants;
import com.app.buy.Helper.UserModel;
import com.app.buy.Model.Product;
import com.app.buy.R;
import com.app.buy.activities.authentication.UserRegisterActivity;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
    private static final double CFA_TO_GNF_RATE = 14.38;
    final List<String> lastmodels = new ArrayList<>();
    TextView name_cat;
    private EditText name, quantity, price, editTextPriceGNF, productDescription;
    private Button add, choose;
    private ImageView img;
    private Uri imgUri;
    private String category;
    private StorageReference mStorageRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private DatabaseReference m;
    private boolean isUpdating = false; // Flag to prevent infinite loop

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
        editTextPriceGNF = findViewById(R.id.editTextPriceGNF);
        productDescription = findViewById(R.id.editTextDescription);
        spinner = (Spinner) findViewById(R.id.spinner);

        m = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("categories");
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
//        price.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!isUpdating) {
//                    isUpdating = true;
//                    convertCfaToGnf(s.toString());
//                    isUpdating = false;
//                }
//            }
//        });
//
//        editTextPriceGNF.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {}
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (!isUpdating) {
//                    isUpdating = true;
//                    convertGnfToCfa(s.toString());
//                    isUpdating = false;
//                }
//            }
//        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddProduct.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty()|| productDescription.getText().toString().isEmpty() || imgUri == null) {
                    Toast.makeText(AddProduct.this, "Please fill blank fields", Toast.LENGTH_SHORT).show();
                } else {
                    uploadData();

                }
            }
        });
    }


    public void uploadData() {
        Log.d("Testttt", name.getText().toString()+"   "+quantity.getText().toString()+"    "+ productDescription.getText().toString()+"   "+imgUri);
        if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty() || imgUri == null) {
            Toast.makeText(AddProduct.this, "Please fill blank fields", Toast.LENGTH_SHORT).show();
        } else {
            if (price.getText().toString().isEmpty() && !editTextPriceGNF.getText().toString().isEmpty()) {
                price.setText("0");
                uploadImage();

            } else if (editTextPriceGNF.getText().toString().isEmpty() && !price.getText().toString().isEmpty()) {
                editTextPriceGNF.setText("0");
                uploadImage();

            } else if (editTextPriceGNF.getText().toString().isEmpty() && price.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please enter price", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
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
                    UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
                    Product product = new Product(quantity.getText().toString().trim(),
                            price.getText().toString().trim(),
                            editTextPriceGNF.getText().toString().trim(),
                            productDescription.getText().toString().trim(),
                            downloadUrl.toString(), category, name.getText().toString(), FirebaseAuth.getInstance().getUid(), Stash.getString("name"), userModel.getLat(), userModel.getLng(), userModel.getCountry());
                    DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp")
                            .child("product")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(category)
                            .child(name.getText().toString());
                    z.setValue(product).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(AddProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
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
        startActivityForResult(i, UserRegisterActivity.GALARY_PICK);
    }

    public String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UserRegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null) {
            imgUri = data.getData();
            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString(), e.getMessage().toString());
            }

        }
    }

    private void convertCfaToGnf(String value) {
        if (!value.isEmpty()) {
            double cfa = Double.parseDouble(value);
            double gnf = cfa * CFA_TO_GNF_RATE;
            editTextPriceGNF.setText(String.valueOf(gnf));
        } else {
            editTextPriceGNF.setText("");
        }
    }

    private void convertGnfToCfa(String value) {
        if (!value.isEmpty()) {
            double gnf = Double.parseDouble(value);
            double cfa = gnf / CFA_TO_GNF_RATE;
            price.setText(String.valueOf(cfa));
        } else {
            price.setText("");
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}