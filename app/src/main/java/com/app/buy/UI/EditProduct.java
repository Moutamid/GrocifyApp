package com.app.buy.UI;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

public class EditProduct extends AppCompatActivity {
    private static final double CFA_TO_GNF_RATE = 14.38;
    final List<String> lastmodels = new ArrayList<>();
    Bundle b;
    private EditText name, quantity, price, productDescription, editTextPriceGNF;
    private Button edit, choose;
    private ImageView img;
    private Uri imgUri;
    private String category, oldName, oldImagePath, oldCategory;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private Spinner spinner;
    private StorageTask mUploadTask;
    private byte[] oldImageBytes;
    private DatabaseReference m;
    private boolean isUpdating = false; // Flag to prevent infinite loop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        name = findViewById(R.id.editTextProductNameEdit);
        quantity = findViewById(R.id.editTextProductNumberEdit);
        edit = findViewById(R.id.btnAddEdit);
        choose = findViewById(R.id.btnChooseImgEdit);
        img = findViewById(R.id.imgProductEdit);
        price = findViewById(R.id.editTextProductPriceEdit);
        productDescription = findViewById(R.id.editTextProductExpireEdit);
        editTextPriceGNF = findViewById(R.id.editTextPriceGNF);
        spinner = findViewById(R.id.spinner);

        b = getIntent().getExtras();
        name.setText(b.getString("name"));
        oldName = b.getString("name");
        quantity.setText(b.getString("quantity"));
        Picasso.get().load(b.getString("img")).fit().centerCrop().into(img);
        price.setText(b.getString("price"));
        editTextPriceGNF.setText(b.getString("priceGNF"));
        productDescription.setText(b.getString("details"));
        imgUri = Uri.parse(b.getString("img"));
        oldImagePath = b.getString("img");
        oldCategory = b.getString("category");
        if(b.getString("priceGNF").equals("0"))
        {
            editTextPriceGNF.setVisibility(View.GONE);
            price.setVisibility(View.VISIBLE);

        }
        else
        {
            editTextPriceGNF.setVisibility(View.VISIBLE);
            price.setVisibility(View.GONE);
        }

        m = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("categories");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    lastmodels.add(ds.getKey().toString());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(EditProduct.this, android.R.layout.simple_spinner_item, lastmodels);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        category = adapterView.getItemAtPosition(i).toString();
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


        StorageReference httpsRef = FirebaseStorage.getInstance("gs://sos-app-63a86.appspot.com").getReferenceFromUrl(oldImagePath);
        final long ONE_MEGABYTE = 1024 * 1024 * 10;
        httpsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                oldImageBytes = bytes;
            }
        });

        mStorageRef = FirebaseStorage.getInstance("gs://sos-app-63a86.appspot.com").getReference().child("PharmacyApp").child("product");
        mDatabaseRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("product");


        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(EditProduct.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditProduct.this).setTitle("Confirmation").setMessage("Are You Sure You Want To Save ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            deleteImage();
                            deleteData();
                            uploadData();
                            finish();
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert);
                    dialog.show();
                }
            }
        });
    }


    public void deleteData() {
        DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp")
                .child("product")
                .child(FirebaseAuth.getInstance().getUid())
                .child(category)
                .child(b.getString("name"));
        z.removeValue();
    }

    public void deleteImage() {
        if (imgUri.toString().equals(oldImagePath)) {
            StorageReference x = mStorageRef.child(oldName);
            x.delete();
        } else {
            StorageReference z = mStorageRef.child(oldName + ".jpg");
            z.delete();
        }
    }

    public void uploadData() {
        if (name.getText().toString().isEmpty() || quantity.getText().toString().isEmpty() || price.getText().toString().isEmpty() || productDescription.getText().toString().isEmpty() || imgUri == null) {
            Toast.makeText(EditProduct.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        } else {
            uploadImage();
        }
    }

    public void uploadImage() {
        if (imgUri.toString().equals(oldImagePath)) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString());
            mUploadTask = fileReference.putBytes(oldImageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
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
                            downloadUrl.toString(), category,
                            name.getText().toString(), FirebaseAuth.getInstance().getUid(), Stash.getString("name"), userModel.getLat(), userModel.getLng(), userModel.getCountry());
                    DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp")
                            .child("product")
                            .child(FirebaseAuth.getInstance().getUid())
                            .child(category)
                            .child(name.getText().toString());
                    z.setValue(product);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProduct.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    Product product = new Product(quantity.getText().toString().trim(),
                            price.getText().toString().trim(),
                            editTextPriceGNF.getText().toString().trim(),
                            productDescription.getText().toString().trim(),
                            downloadUrl.toString(), category,
                            name.getText().toString(), FirebaseAuth.getInstance().getUid(), Stash.getString("name"), userModel.getLat(), userModel.getLng(), userModel.getCountry());
                    DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp")
                            .child("product")
                            .child(FirebaseAuth.getInstance().getUid())

                            .child(category)
                            .child(name.getText().toString());
                    z.setValue(product);
                    Toast.makeText(EditProduct.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditProduct.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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