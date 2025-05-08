package com.app.buy.activities.authentication;

import static android.view.View.GONE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.Settings;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.buy.Helper.ApiService;
import com.app.buy.Helper.Constants;
import com.app.buy.Helper.Country;
import com.app.buy.Helper.RetrofitClient;
import com.app.buy.Helper.UserModel;
import com.app.buy.R;
import com.app.buy.activities.SubscriptionActivity;
import com.fxn.stash.Stash;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PharmacyRegisterActivity extends AppCompatActivity {
    public static final int GALARY_PICK = 1;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    public static String address;
    public static double lat;
    public static double lng;
    ImageView image;
    ProgressBar progressBar1;
    AutoCompleteTextView autoCompleteTextView;
    String emailRegex = "^[a-zA-Z0-9.+_-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private FirebaseAuth mAuth;
    private Bitmap bitmap;
    private StorageReference mStorageRef;
    private Uri ResultURI = null;
    private String uId;
    private Animation animation;
    private EditText about;
    private EditText latEditText;
    private EditText lngEditText;
    private EditText locationTitleEditText;
    private Button getAdress;
    private FusedLocationProviderClient fusedLocationClient;
    private Spinner spinner;
    private List<String> countryNames = new ArrayList<>();
String selectedCountry;
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pharmacy);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance("gs://sos-app-63a86.appspot.com").getReference().child("PharmacyApp");
        progressBar1 = findViewById(R.id.progressBar1);
        final EditText email = findViewById(R.id.email);
        final EditText name = findViewById(R.id.name);
        final EditText pass1 = findViewById(R.id.pass1);
        final EditText pass2 = findViewById(R.id.pass2);
        final EditText num = findViewById(R.id.num);
        about = findViewById(R.id.about);
        getAdress = findViewById(R.id.getAdress);
        latEditText = findViewById(R.id.lat_edit_text);
        lngEditText = findViewById(R.id.lng_edit_text);
        locationTitleEditText = findViewById(R.id.location_title_edit_text);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ImageView togglePassword = findViewById(R.id.ivTogglePassword);
        togglePassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword.setImageResource(R.drawable.blind); // icon for hidden
                } else {
                    // Show password
                    pass1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword.setImageResource(R.drawable.visible); // icon for visible
                }
                // Move cursor to the end
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
                    // Hide password
                    pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword1.setImageResource(R.drawable.blind); // icon for hidden
                } else {
                    // Show password
                    pass2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword1.setImageResource(R.drawable.visible); // icon for visible
                }
                // Move cursor to the end
                pass2.setSelection(pass2.length());
                isPasswordVisible1 = !isPasswordVisible1;
            }
        });
        // Check permissions
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            getCurrentLocation();
        }
        String apiKey = "AIzaSyAuIxeEpQQgN84bBitDRksZTcLHtIKSAeY";
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apiKey);
        }


        initAutoCompleteTextView();
        Constants.initDialog(this);
        Constants.showDialog();
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
                Intent i = new Intent(PharmacyRegisterActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        spinner = findViewById(R.id.spinnerCountries);

        fetchCountries();

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (email.getText().toString().isEmpty() || name.getText().toString().isEmpty() || pass1.getText().toString().isEmpty()
                        || pass2.getText().toString().isEmpty() || num.getText().toString().isEmpty() || autoCompleteTextView.getText().toString().isEmpty()) {
                    Toast.makeText(PharmacyRegisterActivity.this, "Please fill all fields", Toast.LENGTH_LONG).show();
                } else if (!pass1.getText().toString().equals(pass2.getText().toString())) {
                    Toast.makeText(PharmacyRegisterActivity.this, "you must write same password", Toast.LENGTH_LONG).show();
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
                        }
//                        else if (ResultURI == null) {
//                            Toast.makeText(PharmacyRegisterActivity.this, "Please upload images before creating account", Toast.LENGTH_SHORT).show();
//                            return;
//                        }
                        else {
                            progressBar1.setVisibility(View.VISIBLE);

                            mAuth.createUserWithEmailAndPassword(mailtxt, passtxt).addOnCompleteListener(PharmacyRegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseUser currentUser = mAuth.getCurrentUser();
                                        if (currentUser != null) {
                                            String uId = currentUser.getUid();

                                            UserModel userModel = new UserModel(uId,
                                                    name.getText().toString(),
                                                    email.getText().toString(),
                                                    num.getText().toString(),
                                                    "default",
                                                    autoCompleteTextView.getText().toString(),
                                                    about.getText().toString(),
                                                    latEditText.getText().toString(),
                                                    lngEditText.getText().toString(),
                                                    getDeviceId(PharmacyRegisterActivity.this),
                                                    false,
                                                    false,
                                                    "N/A" ,
                                                    selectedCountry);
                                            Stash.put(Constants.STASH_USER, userModel);
                                            // Store data in Firebase
                                            DatabaseReference dbRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/")
                                                    .getReference().child("PharmacyApp").child("Pharmacies");

                                            dbRef.child(uId).setValue(userModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        // Upload image if available
                                                        if (ResultURI != null) {
                                                            UploadImageInStorageDataBase(ResultURI, uId);
                                                        }
                                                        else
                                                        {
                                                            Stash.put("type", "owner");
                                                            Stash.put(Constants.TYPE, "owner");
                                                            Constants.databaseReference().child("Pharmacies").child(uId).child("vip").setValue(false);
                                                            progressBar1.setVisibility(GONE);
                                                            startActivity(new Intent(PharmacyRegisterActivity.this, SubscriptionActivity.class));
                                                            finish();
                                                        }
                                                    } else {
                                                        Toast.makeText(PharmacyRegisterActivity.this, "Data upload failed", Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                        }
                                    } else {
                                        Toast.makeText(PharmacyRegisterActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                        progressBar1.setVisibility(GONE);
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
                ResultURI = ImageUri;
                ParcelFileDescriptor descriptor = getContentResolver().openFileDescriptor(ImageUri, "r");
                FileDescriptor fileDescriptor = descriptor.getFileDescriptor();
                bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                descriptor.close();
            } catch (IOException e) {
                Log.e("B2ala", "fileNotFound", e);
            }
            image.setImageBitmap(bitmap);
        }


    }

    private void UploadImageInStorageDataBase(Uri resultUri, String uid) {
        //upload image in storage database
        final StorageReference FilePath = mStorageRef.child("users_image").child(uId + "jpg");
        FilePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                FilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference mUserDatabase = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("Pharmacies").child(uid);
                        mUserDatabase.child("image").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Stash.put("type", "owner");
                                Stash.put(Constants.TYPE, "owner");
                                Constants.databaseReference().child("Pharmacies").child(uid).child("vip").setValue(false);
                                progressBar1.setVisibility(GONE);
                                startActivity(new Intent(PharmacyRegisterActivity.this, SubscriptionActivity.class));
                                finish();
                            }
                        });
                    }
                });

            }
        });


    }

    public void login(View view) {
        startActivity(new Intent(PharmacyRegisterActivity.this, LoginActivity.class));
        finish();
    }

    private void initAutoCompleteTextView() {
        autoCompleteTextView = findViewById(R.id.location_title_edit_text);
        autoCompleteTextView.setThreshold(1);
    }

    public String getDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)
                .setFastestInterval(5000);

        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String locationString = "Lat: " + latitude + ", Lng: " + longitude;
                    latEditText.setText(latitude + "");
                    lngEditText.setText(longitude + "");

                    // Get address from coordinates
                    getAddress(latitude, longitude);
                }
            }
        }, null);
    }

    private void getAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                autoCompleteTextView.setText(address);
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Unable to get address", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
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
                    // Set up Spinner Adapter
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(PharmacyRegisterActivity.this,
                            android.R.layout.simple_spinner_item, countryNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner.setAdapter(adapter);
Constants.dismissDialog();
                    // Set Spinner item selection listener
                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                             selectedCountry = countryNames.get(position);
//                            Toast.makeText(PharmacyRegisterActivity.this, "Selected: " + selectedCountry, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // Do nothing
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
//                Toast.makeText(PharmacyRegisterActivity.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                Constants.dismissDialog();
            }
        });
    }
}


