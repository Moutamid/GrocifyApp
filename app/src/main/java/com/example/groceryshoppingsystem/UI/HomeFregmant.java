package com.example.groceryshoppingsystem.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.GridproductAdapter;
import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.Model.user;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFregmant extends Fragment {

    final List<HorizontalProductModel> lastmodels = new ArrayList<>();

    private DatabaseReference m;
    private static List<favouritesClass> favourites;
    EditText search;
    String lcode = "en-US";
    RecyclerView content_rcv;
    GridproductAdapter my_adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.home_fragment, container, false);
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

        content_rcv = view.findViewById(R.id.product_layout_gridview);
        content_rcv.setLayoutManager(new GridLayoutManager(getContext(), 3));
        my_adapter = new GridproductAdapter(lastmodels, favourites, getContext());
        m = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("product");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot ds : categorySnapshot.getChildren()) {
                        user my_user = new user();
                        my_user = ds.getValue(user.class);
                        my_user.setCategory(ds.getKey().toString());
                        lastmodels.add(new HorizontalProductModel(my_user.getImage(), my_user.getCategory(), my_user.getPrice(), false, my_user.getdetails(), categorySnapshot.getKey()));
                    }
                }
                content_rcv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

        return view;
    }


    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<HorizontalProductModel> filteredlist = new ArrayList<HorizontalProductModel>();

        // running a for loop to compare elements.
        for (HorizontalProductModel item : lastmodels) {
            if (item.getProducttitle().toLowerCase().contains(text.toLowerCase())||item.getCategory_name().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv.setVisibility(View.GONE);
        } else {
            content_rcv.setVisibility(View.VISIBLE);
//            my_adapter.filterList(filteredlist);
            my_adapter = new GridproductAdapter(filteredlist, favourites, getContext());
content_rcv.setAdapter(my_adapter);
            my_adapter.notifyDataSetChanged();
        }
    }

}