package com.example.groceryshoppingsystem.UI;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.CategoryProductInfoAdapter;
import com.example.groceryshoppingsystem.Adapters.Favourite_Recycler_View;
import com.example.groceryshoppingsystem.Adapters.MyAdapter_Recycler_View;
import com.example.groceryshoppingsystem.Adapters.OrderAdapter;
import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.Model.user;
import com.example.groceryshoppingsystem.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavouriteFregmant extends Fragment {

    private Favourite_Recycler_View my_adapter;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.favourite_recycler_view, container, false);
Retrieve_fav(view);
        return view;
    }

    public void Retrieve_fav(View view) {
        final RecyclerView rc = view.findViewById(R.id.recyclerView);
        // rc.setHasFixedSize(true);
        // rc.setLayoutManager(new LinearLayoutManager(this));
        GridLayoutManager mGridLayoutManager;
        mGridLayoutManager = new GridLayoutManager(getContext(), 1, GridLayoutManager.VERTICAL, false);
        rc.setLayoutManager(mGridLayoutManager);
        final List<favouritesClass> favourite_list = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("favourites")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("data", ds.toString());

                    favouritesClass fav = new favouritesClass();
                    fav = ds.getValue(favouritesClass.class);
                    favourite_list.add(fav);
                }
                my_adapter = new Favourite_Recycler_View(favourite_list, getContext());
                rc.setAdapter(my_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent(eventListener);


    }

}