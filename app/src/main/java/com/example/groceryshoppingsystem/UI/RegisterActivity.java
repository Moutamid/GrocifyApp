package com.example.groceryshoppingsystem.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    ImageView image;
    private Bitmap bitmap;
    public static final int GALARY_PICK = 1;
    private StorageReference mStorageRef;
    private Uri ResultURI;
    private String uId;
    private Animation animation;
    ProgressBar progressBar1;
    String emailRegex = "^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance("gs://grocery-delivery-app-22f4e.appspot.com").getReference().child("GrocaryApp");
        progressBar1 =  findViewById(R.id.progressBar1);
        final EditText email = (EditText) findViewById(R.id.email);
        final EditText name = (EditText) findViewById(R.id.name);
        final EditText pass1 = (EditText) findViewById(R.id.pass1);
        final EditText pass2 = (EditText) findViewById(R.id.pass2);
        final EditText num = (EditText) findViewById(R.id.num);


        image = findViewById(R.id.image);
        final Button finish = (Button) findViewById(R.id.finish);
        LinearLayout login = (LinearLayout) findViewById(R.id.login);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosephoto();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RegisterActivity.this, loginActivity.class);
                startActivity(i);
                finish();
            }
        });


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (email.getText().toString().isEmpty() || name.getText().toString().isEmpty() || pass1.getText().toString().isEmpty()
                        || pass2.getText().toString().isEmpty() || num.getText().toString().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else if (!pass1.getText().toString().equals(pass2.getText().toString())) {
                    Toast.makeText(RegisterActivity.this, "you must write same password", Toast.LENGTH_LONG).show();
                    pass1.setText("");
                    pass2.setText("");
                } else {
                    String mailtxt = email.getText().toString();
                    String passtxt = pass1.getText().toString();
                    boolean isValidEmail = mailtxt.matches(emailRegex);
                    if (isValidEmail) {
                        if (pass1.getText().toString().length() < 8 && !isValidPassword(pass1.getText().toString())) {
                            pass1.setError("Minimum length of 8 characters\n" +
                                    "Contains at least one uppercase letter\n" +
                                    "Contains at least one lowercase letter\n" +
                                    "Contains at least one number\n" +
                                    "Contains at least one special character");
                        } else {
                            progressBar1.setVisibility(View.VISIBLE);

                            mAuth.createUserWithEmailAndPassword(mailtxt, passtxt).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("Name", name.getText().toString());
                                        hashMap.put("Image", "default");
                                        hashMap.put("Phone", num.getText().toString());
                                        hashMap.put("Email", email.getText().toString());

                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        uId = currentUser.getUid();
                                        DatabaseReference z = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("users");
                                        z.child(uId).setValue(hashMap);

                                        if (ResultURI != null)
                                            UploadImageInStorageDataBase(ResultURI);
                                        sendVerificationEmail();

                                    } else {
                                        Toast.makeText(RegisterActivity.this, "registration failed", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    } else {
                        email.setError("Please write email in correct format");
                    }
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void choosephoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT IMAGE"), GALARY_PICK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //to crop image
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK) {
            try {
                Uri ImageUri = data.getData();
                CropImage.activity(ImageUri)
                        .setAspectRatio(1, 1)
                        .start(this);

                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(ImageUri, "r");
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);

                descriptor.close();
            } catch (IOException e) {
                Log.e("B2ala", "fileNotFound", e);
            }
            image.setImageBitmap(bitmap);
        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                ResultURI = resultUri;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private void UploadImageInStorageDataBase(Uri resultUri) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        uId = currentUser.getUid();

        //upload image in storage database
        final StorageReference FilePath = mStorageRef.child("users_image").child(uId + "jpg");
        FilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("users").child(uId);
                        mUserDatabase.child("Image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //
                            }
                        });
                    }
                });

            }
        });


    }

    public void login(View view) {
        startActivity(new Intent(RegisterActivity.this, loginActivity.class));
        finish();
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            progressBar1.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Successfully Registered", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }

}