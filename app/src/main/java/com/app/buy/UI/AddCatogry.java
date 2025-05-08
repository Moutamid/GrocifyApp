package com.app.buy.UI;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.buy.R;
import com.app.buy.activities.authentication.UserRegisterActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class AddCatogry extends AppCompatActivity {
    private EditText name;
    private Button add, choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private StorageTask mUploadTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_category);

        //tool bar
//        mToolBar = (Toolbar)findViewById(R.id.AddSalesMen_ToolBar);
//        setSupportActionBar(mToolBar);
//        getSupportActionBar().setTitle("Add Catogry");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mStorageRef = FirebaseStorage.getInstance("gs://sos-app-63a86.appspot.com").getReference().child("PharmacyApp").child("categories");
        mDataBaseRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("categories");

        name = findViewById(R.id.editTextCatogryName);
        add = findViewById(R.id.btnAddCatogry);
        choose = findViewById(R.id.btnChooseCatogryImage);
        img = findViewById(R.id.CatogryImage);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(AddCatogry.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else if (name.getText().toString().isEmpty()) {
                    name.setError("Please Enter");
                } else if (imgUri == null) {

                    Toast.makeText(AddCatogry.this, "Please choose at least one image ", Toast.LENGTH_SHORT).show();

                } else {
                    uploadData();
                    Toast.makeText(AddCatogry.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    public void uploadData() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Please Enter");
        } else if (imgUri == null) {

            Toast.makeText(this, "Please choose at least one image ", Toast.LENGTH_SHORT).show();

        } else
            uploadImage();
    }


    public void uploadImage() {
        if (imgUri != null) {
            StorageReference fileReference = mStorageRef.child(name.getText().toString().trim() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();
                    DatabaseReference z = mDataBaseRef.child(name.getText().toString().trim())
                            .child("image");
                    z.setValue(downloadUrl.toString());

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddCatogry.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
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

    public void backPress(View view) {
        onBackPressed();
    }
}