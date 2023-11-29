package com.example.groceryshoppingsystem.AdminPanel;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.AdminOrderAdapter;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class OrderPendingFregmant extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    ArrayList<MyorderModel> orderItemList;
    AdminOrderAdapter adapter;
    private FirebaseAuth mAuth;
    private String CurrentUser;
    private DatabaseReference m, root;
    public static Activity fa;

    public OrderPendingFregmant() {
        // Required empty public constructor
    }

    private RecyclerView OrderItemRecyclerView;

    public static OrderPendingFregmant newInstance(String param1, String param2) {
        OrderPendingFregmant fragment = new OrderPendingFregmant();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_fregmant, container, false);
        fa = getActivity();
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser().getUid();
        OrderItemRecyclerView = view.findViewById(R.id.orderrecycler);
        orderItemList = new ArrayList<MyorderModel>();
        adapter = new AdminOrderAdapter(getActivity(), orderItemList);
        OrderItemRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        OrderItemRecyclerView.setAdapter(adapter);
        DatabaseReference roott = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference x = roott.child("order");

        x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    orderItemList.clear();

                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        Log.d("datasnap", snapshot1 + " ");

                        for (DataSnapshot dataSnapshot : snapshot1.getChildren()) {
                            Log.d("datasnap_details", dataSnapshot + " ");

                            if (dataSnapshot.hasChild("status") && dataSnapshot.hasChild("token")) {
                                String status = dataSnapshot.child("status").getValue(String.class);
                                String token = dataSnapshot.child("token").getValue(String.class);

                                if (status != null && status.equals("pending")) {
                                    String Date = dataSnapshot.child("Date").getValue(String.class);
                                    int nums = (int) dataSnapshot.child("orderproducts").getChildrenCount();
                                    String totalPrice = dataSnapshot.child("totalPrice").getValue(String.class);
                                    String OrderCheck = dataSnapshot.child("IsChecked").getValue(String.class);
                                    String phonenumber = dataSnapshot.child("phonenumber").getValue(String.class);
                                    String address = dataSnapshot.child("address").getValue(String.class);
                                    String email = dataSnapshot.child("email").getValue(String.class);
                                    String name = dataSnapshot.child("name").getValue(String.class);
                                    String key = dataSnapshot.child("key").getValue(String.class);
                                    String uid = dataSnapshot.child("uid").getValue(String.class);

                                    String products = "";
                                    for (DataSnapshot data : dataSnapshot.child("orderproducts").getChildren()) {
                                        products += "        " + data.getKey() + "\n                 Price: " + data.child("productPrice").getValue().toString() + " $\n                Quantity: " + data.child("quantity").getValue().toString() + "\n";
                                    }

                                    orderItemList.add(new MyorderModel(dataSnapshot.getKey(), "" + Date, String.valueOf(nums), totalPrice + " $", "   " + products, OrderCheck, address, email, phonenumber, name, token, uid, key, 1));
                                }
                            }
                        }
                    }
                } else {
                    orderItemList.clear();
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Failed to read value.", error.toException());
            }
        });

        return view;
    }
}