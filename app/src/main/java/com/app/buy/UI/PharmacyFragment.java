package com.app.buy.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Adapters.PharmacyAdapter;
import com.app.buy.Model.PharmacyModel;
import com.app.buy.R;
import com.fxn.stash.Stash;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PharmacyFragment extends Fragment {


    final List<PharmacyModel> lastmodels = new ArrayList<>();
    EditText search;
    RecyclerView content_rcv;
    PharmacyAdapter my_adapter;
    private DatabaseReference m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        search = view.findViewById(R.id.search_bar);
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

        getData(view);

        return view;
    }

    public void getData(View view) {
        content_rcv = view.findViewById(R.id.product_layout_gridview);
        content_rcv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        m = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("Pharmacies");
        m.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Log.d("dataaaaaaa", ds.toString());
                    PharmacyModel category = ds.getValue(PharmacyModel.class);
                    if (category.getCountry().equals(Stash.getString("country"))) {
                        lastmodels.add(new PharmacyModel(category.getEmail(), category.getPhone(), category.getName(), category.getImage(), category.getLat(), category.getLng(), category.getAbout(), category.getAddress(), ds.getKey(), category.getStatus()));
                    }
                }

                my_adapter = new PharmacyAdapter(lastmodels, getContext());
                content_rcv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<PharmacyModel> filteredlist = new ArrayList<PharmacyModel>();

        for (PharmacyModel item : lastmodels) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv.setVisibility(View.GONE);
        } else {
            content_rcv.setVisibility(View.VISIBLE);
//            my_adapter.filterList(filteredlist);
            my_adapter = new PharmacyAdapter(filteredlist, getContext());
            content_rcv.setAdapter(my_adapter);
            my_adapter.notifyDataSetChanged();
        }
    }


}