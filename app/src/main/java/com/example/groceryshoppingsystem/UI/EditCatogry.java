package com.example.groceryshoppingsystem.UI;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.groceryshoppingsystem.Model.Catogry;
import com.example.groceryshoppingsystem.R;
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

public class EditCatogry extends AppCompatActivity {
    private EditText name;
    private Button edit , choose;
    private ImageView img;
    private Uri imgUri;
    private StorageReference mStorageRef;
    private DatabaseReference mdataReference;
    private StorageTask mUploadTask;
    private String oldName , oldImagePath , oldQrPath;
    private byte[] oldImageBytes = null;
    private Toolbar mToolBar;
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales_man);

        mStorageRef = FirebaseStorage.getInstance("gs://grocery-delivery-app-22f4e.appspot.com").getReference().child("GrocaryApp").child("categories");
        mdataReference = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("categories");

        name = findViewById(R.id.editTextCatogryName);
        edit = findViewById(R.id.btnAddCatogryEdit);
        choose = findViewById(R.id.btnChooseCatogryImageEdit);
        img = findViewById(R.id.CatogryImage);

        Bundle b = getIntent().getExtras();

        oldName = b.getString("name");
        oldImagePath = b.getString("image");
        name.setText(oldName);
        Picasso.get().load(oldImagePath).fit().centerCrop().into(img);
        imgUri = Uri.parse(oldImagePath);

        StorageReference httpsRef = FirebaseStorage.getInstance("gs://grocery-delivery-app-22f4e.appspot.com").getReferenceFromUrl(oldImagePath);
        final long ONE_MEGABYTE = 1024 * 1024 * 1024;
        httpsRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                oldImageBytes = bytes;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(EditCatogry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUploadTask != null && mUploadTask.isInProgress())
                    Toast.makeText(EditCatogry.this, "Upload Is In Progress", Toast.LENGTH_SHORT).show();
                else {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(EditCatogry.this).setTitle("Confirmation").setMessage("Are You Sure You Want To Save ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

        choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImage();
            }
        });
    }



    public void uploadData()
    {
        if(name.getText().toString().isEmpty() || imgUri == null)
        {
            Toast.makeText(EditCatogry.this, "Empty Cells", Toast.LENGTH_SHORT).show();
        }
        else
            uploadImage();
    }

    public void deleteData()
    {
        DatabaseReference reference = mdataReference.child(oldName);
        reference.removeValue();
    }

    public void deleteImage()
    {

        StorageReference a = mStorageRef.child(oldName + ".jpg");
        a.delete();

        StorageReference z = mStorageRef.child(oldName);
        z.delete();

    }




    public void uploadImage()
    {
        if(imgUri.toString().equals(oldImagePath))
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString());
            mUploadTask = fileReference.putBytes(oldImageBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful()) ;
                    Uri downloadUrl = urlTask.getResult();

                    DatabaseReference a = mdataReference.child(name.getText().toString().trim());
                    DatabaseReference b = mdataReference.child(name.getText().toString().trim());
                    DatabaseReference c = mdataReference.child(name.getText().toString().trim());
                    a.child("image").setValue(downloadUrl.toString());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditCatogry.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(imgUri != null)
        {
            StorageReference fileReference = mStorageRef.child(name.getText().toString() + "." + getFileExtension(imgUri));
            mUploadTask = fileReference.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    Catogry man = new Catogry(  downloadUrl.toString());
                    DatabaseReference z = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("categories")
                            .child(name.getText().toString().trim());
                    z.setValue(man);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(EditCatogry.this, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void openImage()
    {
        Intent i =  new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i , RegisterActivity.GALARY_PICK);
    }

    public String getFileExtension(Uri uri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RegisterActivity.GALARY_PICK && resultCode == Activity.RESULT_OK && data.getData() != null && data != null)
        {
            imgUri = data.getData();

            try {
                Picasso.get().load(imgUri).fit().centerCrop().into(img);
            } catch (Exception e) {
                Log.e(this.toString() , e.getMessage().toString());
            }

        }
    }



    public void backPress(View view) {
        onBackPressed();
    }
}