package com.example.groceryshoppingsystem.AdminPanel;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.OffersFragment;
import com.example.groceryshoppingsystem.UI.OrderFregmant;
import com.example.groceryshoppingsystem.UI.ProductsFragment;
import com.example.groceryshoppingsystem.UI.SalesMenFragment;
import com.example.groceryshoppingsystem.UI.loginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminActivity extends AppCompatActivity {
    ImageView logout;
    private BottomNavigationView bottomNavigationView;
    private TextView FragmentTitle;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mAuth = FirebaseAuth.getInstance();

        logout = findViewById(R.id.logout);
        FragmentTitle = (TextView) findViewById(R.id.FragmentTitle);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.Bottom_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(naveListener);
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                Log.d("TAG", "FCM Token: " + token);
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("token", token);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                DatabaseReference z = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
                z.child("Admin").setValue(hashMap);


            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckLogout();
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new ProductsFragment()).commit();
        FragmentTitle.setText("All Products");


    }


    private BottomNavigationView.OnNavigationItemSelectedListener naveListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment SelectedFragment = null;
                    int id = item.getItemId();
                    if (id == R.id.ProductID) {
                        SelectedFragment = new ProductsFragment();
                        FragmentTitle.setText("All Products");
                    } else if (id == R.id.OffersID) {
                        SelectedFragment = new AdminOrderFregmant();
                        FragmentTitle.setText("All Orders");
                    } else if (id == R.id.SalesMenID) {
                        SelectedFragment = new SalesMenFragment();
                        FragmentTitle.setText("All Categories");
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, SelectedFragment).commit();
                    return true;
                }
            };


    private void CheckLogout() {
        AlertDialog.Builder checkAlert = new AlertDialog.Builder(AdminActivity.this);
        checkAlert.setMessage("Do you want to Logout?")
                .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(AdminActivity.this, loginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = checkAlert.create();
        alert.setTitle("LogOut");
        alert.show();

    }


}