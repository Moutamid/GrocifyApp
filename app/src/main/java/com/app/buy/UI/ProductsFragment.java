package com.app.buy.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.app.buy.Adapters.AdminOfferAdapter;
import com.app.buy.Adapters.AdminProductAdapter;
import com.app.buy.Model.Product;
import com.app.buy.R;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.List;

public class ProductsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView ProductsRecycler;
    private AdminProductAdapter adapter;
    private Button ProductsFloatingActionButton;
    private List<Product> adminProducts;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;


    public ProductsFragment() {
    }

    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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
        view=inflater.inflate(R.layout.fragment_products, container, false);

        ProductsRecycler= (RecyclerView)view.findViewById(R.id.ProductsRecycler);
        ProductsFloatingActionButton= (Button)view.findViewById(R.id.ProductsFloatingBtnId);

        bar = view.findViewById(R.id.productProgressBar);

        mDataBaseRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("product").child(FirebaseAuth.getInstance().getUid()).child(Stash.getString("current_category", "test"));
        adminProducts = new ArrayList<>();

        adapter = new AdminProductAdapter(getActivity(),adminProducts);
        ProductsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        ProductsRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminProducts.clear();

                    for(DataSnapshot snapshot2 : snapshot.getChildren())//name
                    {
                        adminProducts.add(new Product(
                                snapshot2.child("quantity").getValue(String.class),
                                snapshot2.child("price").getValue(String.class) ,
                                snapshot2.child("priceGNF").getValue(String.class) ,
                                snapshot2.child("details").getValue(String.class),
                                snapshot2.child("image").getValue(String.class) ,
                                snapshot2.child("category").getValue(String.class) ,
                                snapshot2.child("name").getValue(String.class),
                                snapshot2.child("pharmacy_id").getValue(String.class),
                                snapshot2.child("pharmacy_name").getValue(String.class),
                                snapshot2.child("lat").getValue(String.class),
                                snapshot2.child("lng").getValue(String.class),
                                snapshot2.child("country").getValue(String.class)


                        ));

                }
                adapter.notifyDataSetChanged();
                bar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter.setOnItemClickListener(new AdminOfferAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                Intent i = new Intent(getActivity() , EditProduct.class);
                Bundle b = new Bundle();
                b.putString("img" , adminProducts.get(pos).getImage());
                b.putString("name" , adminProducts.get(pos).getName());
                b.putString("category" , adminProducts.get(pos).getCategory());
                b.putString("details" , adminProducts.get(pos).getDetails());
                b.putString("price" , adminProducts.get(pos).getPrice());
                b.putString("quantity" , adminProducts.get(pos).getQuantity());
                b.putString("priceGNF" , adminProducts.get(pos).getPriceGNF());
                i.putExtras(b);
                startActivity(i);
            }
        });

        adapter.setOnLongClickListener(new AdminOfferAdapter.onLongClickListener() {
            @Override
            public void onItemLongClick(final int pos) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity()).setTitle("Confirmation").setMessage("Are You Sure You Want To Delete ?!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp")
                                .child("product")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(adminProducts.get(pos).category)
                                .child(adminProducts.get(pos).name);
                        z.removeValue();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
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