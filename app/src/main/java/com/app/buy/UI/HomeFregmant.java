package com.app.buy.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Helper.Constants;
import com.app.buy.Model.Medicine;
import com.app.buy.R;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFregmant extends Fragment {

    EditText search;
    RecyclerView categoryRecyclerView;
    CategoryAdapter categoryAdapter;
    List<String> categories = new ArrayList<>();
    String selectedCategory = "ALL"; // Default category
    String country;
    private RecyclerView recyclerView;
    private MedicineAdapter adapter;
    private List<Medicine> medicineList;
    private DatabaseReference databaseReference;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        search = view.findViewById(R.id.search_bar);
        categoryRecyclerView = view.findViewById(R.id.category_recyclerview);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(categories, this::onCategorySelected);
        categoryRecyclerView.setAdapter(categoryAdapter);
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
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        medicineList = new ArrayList<>();
        adapter = new MedicineAdapter(getContext(), medicineList);
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/")
                .getReference().child("PharmacyApp").child("product");
        ;
        Constants.initDialog(getContext());
        Constants.showDialog();
        getUserProfileData();
        return view;
    }


    private void onCategorySelected(String category) {
        selectedCategory = category;
        filter(search.getText().toString());
    }

    private void filter(String text) {
        List<Medicine> filteredList = new ArrayList<>();
        for (Medicine item : medicineList) {
            Log.d("FilterDebug", "Selected Category: " + selectedCategory + " | Medicine Category: " + item.getCategory());
            if ((selectedCategory.equals("ALL") || item.getCategory().trim().equalsIgnoreCase(selectedCategory)) &&
                    (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                            item.getCategory().toLowerCase().contains(text.toLowerCase()))) {
                filteredList.add(item);
            }
        }

        // Instead of creating a new adapter, update the existing one
        adapter.updateList(filteredList);
    }

    private void fetchMedicines(String country) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                medicineList.clear();
                categories.clear(); // Reset to prevent duplicates
                categories.add("ALL"); // Ensure "ALL" is always present

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot categorySnapshot : userSnapshot.getChildren()) {
                        String category = categorySnapshot.getKey().trim();

                        if (!categories.contains(category)) {
                            categories.add(category);
                        }

                        for (DataSnapshot medicineSnapshot : categorySnapshot.getChildren()) {
                            Log.d("FirebaseData", medicineSnapshot + "-----");
                            Medicine medicine = medicineSnapshot.getValue(Medicine.class);
                            if (medicine != null) {
                                if (medicine.getCountry().equals(country)) {
                                    medicineList.add(medicine);
                                }
                            }
                        }
                    }
                }
                Constants.dismissDialog();
                Collections.sort(categories); // Sort the categories
                categoryAdapter.notifyDataSetChanged(); // Notify category adapter
                adapter.notifyDataSetChanged(); // Notify medicine adapter
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void getUserProfileData() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp");
        DatabaseReference m = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    if (snapshot.hasChild("country")) {
                        country = snapshot.child("country").getValue().toString();
                        Stash.put("country", country);
                        fetchMedicines(country);
                    }
                }
                Constants.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Constants.dismissDialog();

                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

}

