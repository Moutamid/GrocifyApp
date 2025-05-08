package com.app.buy.activities.authentication;

import static android.view.View.GONE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.buy.Helper.ApiService;
import com.app.buy.Helper.Constants;
import com.app.buy.Helper.Country;
import com.app.buy.Helper.RetrofitClient;
import com.app.buy.Helper.UserModel;
import com.app.buy.R;
import com.app.buy.activities.SubscriptionActivity;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRegisterActivity extends AppCompatActivity {

    public static final int GALARY_PICK = 1;
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    String selectedCountry;
    private FirebaseAuth mAuth;
    private StorageReference mStorageRef;
    private Uri resultURI = null;
    private String uId;
    private Bitmap bitmap;
    private ProgressBar progressBar1;
    private ImageView image;
    private Spinner spinner;
    private List<String> countryNames = new ArrayList<>();

    public static boolean isValidPassword(final String password) {
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference().child("PharmacyApp");

        progressBar1 = findViewById(R.id.progressBar1);
        image = findViewById(R.id.image);

        final EditText email = findViewById(R.id.email);
        final EditText name = findViewById(R.id.name);
        final EditText pass1 = findViewById(R.id.pass1);
        final EditText pass2 = findViewById(R.id.pass2);
        final EditText num = findViewById(R.id.num);
        final Button finish = findViewById(R.id.finish);
        LinearLayout login = findViewById(R.id.login);

        ImageView togglePassword = findViewById(R.id.ivTogglePassword);
        togglePassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword.setImageResource(R.drawable.blind);
                } else {
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword.setImageResource(R.drawable.visible);
                }
                pass1.setSelection(pass1.length());
                isPasswordVisible = !isPasswordVisible;
            }
        });

        ImageView togglePassword1 = findViewById(R.id.ivTogglePassword1);
        togglePassword1.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible1 = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible1) {
                    pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword1.setImageResource(R.drawable.blind);
                } else {
                    pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword1.setImageResource(R.drawable.visible);
                }
                pass2.setSelection(pass2.length());
                isPasswordVisible1 = !isPasswordVisible1;
            }
        });

        image.setOnClickListener(view -> choosePhoto());

        Constants.initDialog(this);
        Constants.showDialog();

        login.setOnClickListener(view -> {
            startActivity(new Intent(UserRegisterActivity.this, LoginActivity.class));
            finish();
        });

        spinner = findViewById(R.id.spinnerCountries);
        fetchCountries();

        finish.setOnClickListener(view -> registerUser(email, name, pass1, pass2, num));
    }

    private void registerUser(EditText email, EditText name, EditText pass1, EditText pass2, EditText num) {
        if (email.getText().toString().isEmpty() || name.getText().toString().isEmpty() ||
                pass1.getText().toString().isEmpty() || pass2.getText().toString().isEmpty() || num.getText().toString().isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }

        if (!pass1.getText().toString().equals(pass2.getText().toString())) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show();
            pass1.setText("");
            pass2.setText("");
            return;
        }

        if (!email.getText().toString().matches(EMAIL_REGEX)) {
            email.setError("Please write email in correct format");
            return;
        }

        if (!isValidPassword(pass1.getText().toString())) {
            pass1.setError("Password must be at least 8 characters, contain uppercase, lowercase, number, and special character.");
            return;
        }

        progressBar1.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass1.getText().toString())
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        saveUserData(email, name, num);
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Registration failed";
                        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
                        progressBar1.setVisibility(GONE);
                    }
                });
    }

    private void saveUserData(EditText email, EditText name, EditText num) {
        uId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        String imageUrl = "default";
        if (resultURI != null) {
            imageUrl = ""; // temporary, will be updated after upload
        }

        UserModel userModel = new UserModel(
                uId,
                name.getText().toString(),
                email.getText().toString(),
                num.getText().toString(),
                imageUrl,
                selectedCountry
        );

        Stash.put(Constants.STASH_USER, userModel);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("PharmacyApp/users").child(uId);
        userRef.setValue(userModel).addOnCompleteListener(task -> {
            if (resultURI != null) {
                uploadImage(resultURI);
            } else {
                sendVerificationEmail();
            }
            Stash.put(Constants.TYPE, "user");
        });
    }

    private void uploadImage(Uri uri) {
        StorageReference filePath = mStorageRef.child("users_image").child(uId + ".jpg");
        filePath.putFile(uri).addOnSuccessListener(taskSnapshot -> filePath.getDownloadUrl().addOnSuccessListener(downloadUri -> {
            UserModel userModel1 = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);

            UserModel updatedUserModel = new UserModel(
                    uId,
                    userModel1.getName(),
                    userModel1.getEmail(),
                    userModel1.getPhone(),
                    downloadUri.toString(),
                    selectedCountry
            );

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("PharmacyApp/users").child(uId);
            userRef.setValue(updatedUserModel).addOnCompleteListener(task -> sendVerificationEmail());
        }));
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Successfully Registered", Toast.LENGTH_LONG).show();
                startActivity(new Intent(this, SubscriptionActivity.class));
                finish();
            }
        });
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Image"), GALARY_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_PICK && resultCode == RESULT_OK && data != null) {
            try {
                resultURI = data.getData();
                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(resultURI, "r");
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                descriptor.close();
                image.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e("ImageError", "File Not Found", e);
            }
        }
    }

    private void fetchCountries() {
        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<List<Country>> call = apiService.getCountries();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Country country : response.body()) {
                        countryNames.add(country.getCountryName());
                    }
                    Collections.sort(countryNames);
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(UserRegisterActivity.this,
                            android.R.layout.simple_spinner_item, countryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
                    Constants.dismissDialog();
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedCountry = countryNames.get(position);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Constants.dismissDialog();
            }
        });
    }
}
