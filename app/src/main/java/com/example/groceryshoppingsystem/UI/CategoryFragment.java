package com.example.groceryshoppingsystem.UI;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.CategoryproductAdapter;
import com.example.groceryshoppingsystem.Adapters.GridproductAdapter;
import com.example.groceryshoppingsystem.Adapters.MyAdapter_Recycler_View;
import com.example.groceryshoppingsystem.Model.HorizontalCategoryModel;
import com.example.groceryshoppingsystem.Model.HorizontalCategoryModel;
import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.category;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.Model.user;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {


    private DatabaseReference m;
    private static List<favouritesClass> favourites;
    EditText search;
    RecyclerView content_rcv;
    CategoryproductAdapter my_adapter;
    final List<HorizontalCategoryModel> lastmodels = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_category, container, false);
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

        Retrieve_Fruits(view);

        return view;
    }

    public void Retrieve_Fruits(View view) {
       content_rcv = view.findViewById(R.id.product_layout_gridview);
        content_rcv.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));

        my_adapter = new CategoryproductAdapter(lastmodels, favourites, getContext());
        m = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("categories");
        ValueEventListener eventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    category my_user = new category();
                    my_user = ds.getValue(category.class);
                    lastmodels.add(new HorizontalCategoryModel(my_user.getImage(), ds.getKey().toString()));
                }
                content_rcv.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

       
    }
    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<HorizontalCategoryModel> filteredlist = new ArrayList<HorizontalCategoryModel>();

        // running a for loop to compare elements.
        for (HorizontalCategoryModel item : lastmodels) {
            if (item.getProducttitle().toLowerCase().contains(text.toLowerCase())) {

                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            content_rcv.setVisibility(View.GONE);
        } else {
            content_rcv.setVisibility(View.VISIBLE);
//            my_adapter.filterList(filteredlist);
            my_adapter = new CategoryproductAdapter(filteredlist, favourites, getContext());
            content_rcv.setAdapter(my_adapter);
            my_adapter.notifyDataSetChanged();
        }
    }


}