package com.app.buy.AdminPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.app.buy.Helper.Constants;
import com.app.buy.Helper.UserModel;
import com.app.buy.R;
import com.app.buy.UI.AllCategoryFragment;
import com.app.buy.UI.ProductsFragment;
import com.app.buy.UI.SalesMenFragment;
import com.app.buy.activities.SubscriptionActivity;
import com.app.buy.activities.authentication.LoginActivity;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    ImageView logout;
    private BottomNavigationView bottomNavigationView;
    public static TextView FragmentTitle;
    private FirebaseAuth mAuth;
    final int PERMISSION_REQUEST_CODE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();
        logout = findViewById(R.id.logout);
        FragmentTitle = (TextView) findViewById(R.id.FragmentTitle);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.Bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(naveListener);
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
        Button btnChangeStatus = findViewById(R.id.btnChangeStatus);
        btnChangeStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showStatusDialog();
            }
        });

        getUserProfileData();
             logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogout();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new AllCategoryFragment()).commit();
        FragmentTitle.setText("Toutes les Catégories");


    }


    private BottomNavigationView.OnNavigationItemSelectedListener naveListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment SelectedFragment = null;
                    int id = item.getItemId();
                    if (id == R.id.ProductID) {
                        SelectedFragment = new AllCategoryFragment();
                        FragmentTitle.setText("Toutes les Catégories");
//                    } else if (id == R.id.OffersID) {
//                        SelectedFragment = new AdminOrderFregmant();
//                        FragmentTitle.setText("All Orders");
                    } else if (id == R.id.SalesMenID) {
                        SelectedFragment = new SalesMenFragment();
                        FragmentTitle.setText("Toutes les Catégories");
                    }else if (id == R.id.Profile) {
                        SelectedFragment = new AdminProfileFregmant();
                        FragmentTitle.setText(getString(R.string.my_profile));
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, SelectedFragment).commit();
                    return true;
                }
            };


    private void CheckLogout() {
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(AdminActivity.this);
        checkAlert.setMessage("Voulez-vous vous déconnecter ?")
                .setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(AdminActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("Déconnexion");
        alert.show();

    }


    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                } else {
                    Toast.makeText(this, "Permisssion denied", Toast.LENGTH_SHORT).show();
                }
                return;

        }

    }

    private void getUserProfileData() {

       DatabaseReference root = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp");
        DatabaseReference m = root.child("Pharmacies").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("vip").getValue().equals(false)) {
                    Log.d("dataaa",snapshot+"---1");
                    UserModel userModel=snapshot.getValue(UserModel.class);
                    Stash.put(Constants.STASH_USER, userModel);
                    Stash.put("name", snapshot.child("name").getValue().toString());
                    startActivity(new Intent(AdminActivity.this, SubscriptionActivity.class));
                    finish();
                }
                else
                {
                    UserModel userModel=snapshot.getValue(UserModel.class);
                    Stash.put(Constants.STASH_USER, userModel);
                    Stash.put("name", snapshot.child("name").getValue().toString());
                    Log.d("dataaa",snapshot+"---2");

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AdminActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
    private void showStatusDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_pharmacy_status, null);
        builder.setView(dialogView);

        RadioGroup radioGroup = dialogView.findViewById(R.id.radioGroupStatus);
        Button btnSubmit = dialogView.findViewById(R.id.btnSubmitStatus);

        AlertDialog dialog = builder.create();
        dialog.show();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                String status = "";

                if (selectedId == R.id.radioOpen) {
                    status = "OUVERT";
                } else if (selectedId == R.id.radioClosed) {
                    status = "FERMÉ";
                } else if (selectedId == R.id.radioOnDuty) {
                    status = "DE GARDE";
                }

                if (!status.isEmpty()) {
                    updatePharmacyStatus(status);
                    dialog.dismiss();
                } else {
                    Toast.makeText(AdminActivity.this, "Please select a status", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void updatePharmacyStatus(String status) {
        DatabaseReference pharmacyRef = FirebaseDatabase.getInstance()
                .getReference("PharmacyApp")
                .child("Pharmacies")
                .child(FirebaseAuth.getInstance().getUid());

        pharmacyRef.child("status").setValue(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AdminActivity.this, getString(R.string.status_updated_to) + status, Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminActivity.this, R.string.failed_to_update_status, Toast.LENGTH_SHORT).show();
                    }
                });
    }

}