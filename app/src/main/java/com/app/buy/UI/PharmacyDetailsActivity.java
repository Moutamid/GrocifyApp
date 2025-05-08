package com.app.buy.UI;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Model.Medicine;
import com.app.buy.Model.PharmacyModel;
import com.app.buy.R;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PharmacyDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {
    EditText search;

    RelativeLayout phone;
    PharmacyModel currentPharmacyModel;
    ImageView red_dot, green_dot, yellow_dot;
    private GoogleMap mMap;
    private TextView name, description, location, status;
    private ImageView mainImage;
    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> medicineList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_details);
        search = findViewById(R.id.search_bar);

        red_dot = findViewById(R.id.red_dot);
        green_dot = findViewById(R.id.green_dot);
        yellow_dot = findViewById(R.id.yellow_dot);
        location = findViewById(R.id.location);
        name = findViewById(R.id.name);
        status = findViewById(R.id.status);
        description = findViewById(R.id.description);
        phone = findViewById(R.id.call);
        mainImage = findViewById(R.id.image);
        recyclerView = findViewById(R.id.recyclerView);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        currentPharmacyModel = (PharmacyModel) Stash.getObject("currentPharmacyModel", PharmacyModel.class);
        displayData();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Set up phone call feature
        phone.setOnClickListener(view -> {
            openWhatsAppChat(currentPharmacyModel.getPhone());
        });
        recyclerView = findViewById(R.id.product_layout_gridview);
        recyclerView.setLayoutManager(new GridLayoutManager(PharmacyDetailsActivity.this, 2));

        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(this, medicineList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("product").child(currentPharmacyModel.getKey());
        ;


        fetchMedicines();
    }

    private void loadData() {


//        Map<String, String> imageMap = new HashMap<>();
//        imageMap.put("Front View", "https://firebasestorage.googleapis.com/v0/b/childfr-35a43.appspot.com/o/download.jpg?alt=media&token=36ac89bb-f216-4e5b-bead-1a9b2786781a");
//        imageMap.put("Interior", "https://firebasestorage.googleapis.com/v0/b/childfr-35a43.appspot.com/o/2.jpg?alt=media&token=ee0f9790-88a6-40f5-888f-b810bbdfdb45");
//        pharmacyModel.setImages(imageMap);
    }

    private void displayData() {


        name.setText(this.currentPharmacyModel.getName());
        description.setText(this.currentPharmacyModel.getAbout());
        status.setText(this.currentPharmacyModel.getStatus());
        if (currentPharmacyModel.getStatus().equals("OUVERT")) {
            green_dot.setVisibility(VISIBLE);
            yellow_dot.setVisibility(GONE);
            red_dot.setVisibility(GONE);
        } else if (currentPharmacyModel.getStatus().equals("FERMÉ")) {
            green_dot.setVisibility(GONE);
            yellow_dot.setVisibility(GONE);
            red_dot.setVisibility(VISIBLE);
        } else if (currentPharmacyModel.getStatus().equals("DE GARDE")) {
            green_dot.setVisibility(GONE);
            yellow_dot.setVisibility(VISIBLE);
            red_dot.setVisibility(GONE);
        }
//        phone.setText(this.currentPharmacyModel.getPhone());
        location.setText(this.currentPharmacyModel.getAddress());
        Glide.with(this).load(this.currentPharmacyModel.getImage()).into(mainImage);

//        List<String> imageUrls = new ArrayList<>(pharmacyModel.getImages().values());
//        imageAdapter = new ImageAdapter(imageUrls);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(imageAdapter);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        currentPharmacyModel = (PharmacyModel) Stash.getObject("currentPharmacyModel", PharmacyModel.class);

        if (googleMap != null) {
            mMap = googleMap;
            Log.d("dataaaa", currentPharmacyModel.getLat() + "-----" + currentPharmacyModel.getLng());
            LatLng location = new LatLng(Double.parseDouble(currentPharmacyModel.getLat()), Double.parseDouble(currentPharmacyModel.getLng()));

            mMap.clear(); // Clear previous markers
            mMap.addMarker(new MarkerOptions().position(location).title(currentPharmacyModel.getName()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        } else {
            Toast.makeText(this, "Error loading map", Toast.LENGTH_SHORT).show();
        }
    }

    private void openWhatsAppChat(String phoneNumber) {
        try {
            // Ensure the number is in international format (without +)
            String url = "https://wa.me/" + phoneNumber;
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void map_(View view) {
        Log.d("latlng", currentPharmacyModel.getLat() + "   " + currentPharmacyModel.getLng());

        if (Double.parseDouble(currentPharmacyModel.getLat()) > -90 && Double.parseDouble(currentPharmacyModel.getLat()) < 90 && Double.parseDouble(currentPharmacyModel.getLng()) > -180 && Double.parseDouble(currentPharmacyModel.getLng()) < 180) {
            Intent intent = new Intent(PharmacyDetailsActivity.this, MapActivity.class);
            intent.putExtra("lat", Double.parseDouble(currentPharmacyModel.getLat()));
            intent.putExtra("lng", Double.parseDouble(currentPharmacyModel.getLng()));
            intent.putExtra("name", currentPharmacyModel.getName());
            startActivity(intent);

        } else {
            Toast.makeText(PharmacyDetailsActivity.this, "Invalid Coordinates to show marker", Toast.LENGTH_SHORT).show();
        }
    }

    public void onBack(View view) {
        onBackPressed();
    }

    private void fetchMedicines() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();

                for (DataSnapshot categorySnapshot : snapshot.getChildren()) {
                    String category = categorySnapshot.getKey().trim();

                    for (DataSnapshot medicineSnapshot : categorySnapshot.getChildren()) {
                        Log.d("FirebaseData", medicineSnapshot + "-----");
                        Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                        if (medicine != null) {
                            medicineList.add(medicine);

                        }
                    }
                }
                adapter.notifyDataSetChanged(); // Notify medicine adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void showRatingDialog(String medicineId) {
        View view = getLayoutInflater().inflate(R.layout.bottom_rating_dialog, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        RatingBar ratingBar = view.findViewById(R.id.ratingBar);
        Button submitBtn = view.findViewById(R.id.submitRatingBtn);

        submitBtn.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            if (rating > 0) {
                saveRatingToFirebase(medicineId, rating);
                bottomSheetDialog.dismiss();
            } else {
                Toast.makeText(this, "Please select a rating", Toast.LENGTH_SHORT).show();
            }
        });

        bottomSheetDialog.show();
    }

    private void saveRatingToFirebase(String medicineId, float rating) {
        long timestamp = System.currentTimeMillis();

        DatabaseReference ratingRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("ratings").child(medicineId);
        ratingRef.child(FirebaseAuth.getInstance().getUid()).child("rating").setValue(rating).addOnSuccessListener(aVoid -> Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show()).addOnFailureListener(e -> Toast.makeText(this, "Failed to submit rating", Toast.LENGTH_SHORT).show());
    }


    public void rate_us(View view) {
        showRatingDialog(currentPharmacyModel.getKey());

    }

    private void filter(String text) {
        List<Medicine> filteredList = new ArrayList<>();
        for (Medicine item : medicineList) {
            if ((item.getName().toLowerCase().contains(text.toLowerCase()) ||
                    item.getCategory().toLowerCase().contains(text.toLowerCase()))) {
                filteredList.add(item);
            }
        }

        // Instead of creating a new adapter, update the existing one
        adapter.updateList(filteredList);
    }

}
