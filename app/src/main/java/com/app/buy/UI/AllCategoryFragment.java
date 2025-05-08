package com.app.buy.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Adapters.AdminCategoryAdapter;
import com.app.buy.AdminPanel.AdminActivity;
import com.app.buy.Model.Product;
import com.app.buy.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllCategoryFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView ProductsRecycler;
    private AdminCategoryAdapter adapter;
    private Button ProductsFloatingActionButton;
    private List<Product> adminProducts;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;


    public AllCategoryFragment() {
    }

    public static AllCategoryFragment newInstance(String param1, String param2) {
        AllCategoryFragment fragment = new AllCategoryFragment();
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
        view = inflater.inflate(R.layout.fragment_products, container, false);

        ProductsRecycler = (RecyclerView) view.findViewById(R.id.ProductsRecycler);
        ProductsFloatingActionButton = (Button) view.findViewById(R.id.ProductsFloatingBtnId);

        bar = view.findViewById(R.id.productProgressBar);

        mDataBaseRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("product").child(FirebaseAuth.getInstance().getUid());
        adminProducts = new ArrayList<>();

        adapter = new AdminCategoryAdapter(getActivity(), adminProducts);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProductsRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminProducts.clear();
                Set<String> existingCategories = new HashSet<>(); // To keep track of unique categories

                for (DataSnapshot snapshot1 : snapshot.getChildren()) { // category level
                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) { // product level
                        String category = snapshot2.child("category").getValue(String.class);

                        if (category != null && !existingCategories.contains(category)) {
                            existingCategories.add(category); // Add to set so duplicates are skipped

                            adminProducts.add(new Product(
                                    snapshot2.child("quantity").getValue(String.class),
                                    snapshot2.child("price").getValue(String.class),
                                    snapshot2.child("priceGNF").getValue(String.class),
                                    snapshot2.child("details").getValue(String.class),
                                    snapshot2.child("image").getValue(String.class),
                                    category,
                                    snapshot2.child("name").getValue(String.class),
                                    snapshot2.child("pharmacy_id").getValue(String.class),
                                    snapshot2.child("pharmacy_name").getValue(String.class),
                                    snapshot2.child("lat").getValue(String.class),
                                    snapshot2.child("lng").getValue(String.class),
                                    snapshot2.child("country").getValue(String.class)
                            ));
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle error
            }
        });


        adapter.setOnItemClickListener(new AdminCategoryAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.FrameLayout, new ProductsFragment()).commit();
                AdminActivity.FragmentTitle.setText("Tous les Produits");

            }
        });


        //on clicking to adding button
        ProductsFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(), AddProduct.class));

            }
        });


        return view;
    }


}