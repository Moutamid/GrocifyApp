package com.app.buy.UI;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.buy.Adapters.AdminCatogryAdapter;
import com.app.buy.Adapters.AdminOfferAdapter;
import com.app.buy.Model.CategoryModel;
import com.app.buy.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SalesMenFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private View view;
    private String mParam1;
    private String mParam2;

    //my variables
    private RecyclerView SalesMenRecycler;
    private AdminCatogryAdapter adapter;
    private FloatingActionButton SalesFloatingActionButton;
    private List<CategoryModel> adminCatogryList;
    private DatabaseReference mDataBaseRef;
    private ProgressBar bar;

    public SalesMenFragment() {
        // Required empty public constructor
    }

    public static SalesMenFragment newInstance(String param1, String param2) {
        SalesMenFragment fragment = new SalesMenFragment();
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
        view = inflater.inflate(R.layout.fragment_sales_men, container, false);

        SalesMenRecycler = (RecyclerView) view.findViewById(R.id.SalesMenRecycler);
        SalesFloatingActionButton = (FloatingActionButton) view.findViewById(R.id.SalesFloatingBtnId);

        mDataBaseRef = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("categories");
        bar = view.findViewById(R.id.CatogryProgressBar);

        adminCatogryList = new ArrayList<>();

        bar.setVisibility(View.VISIBLE);
        adapter = new AdminCatogryAdapter(getActivity(), adminCatogryList);
        SalesMenRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        SalesMenRecycler.setAdapter(adapter);

        mDataBaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adminCatogryList.clear();

                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    adminCatogryList.add(new CategoryModel(snapshot1.getKey(),
                            snapshot1.child("image").getValue(String.class)));
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
                Intent i = new Intent(getActivity(), EditCatogry.class);
                Bundle b = new Bundle();
                b.putString("img", adminCatogryList.get(pos).getImage());
                b.putString("name", adminCatogryList.get(pos).getName());

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

                        DatabaseReference z = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp").child("categories")
                                .child(adminCatogryList.get(pos).getName());
                        z.removeValue();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).setIcon(android.R.drawable.ic_dialog_alert);
                dialog.show();
            }
        });
        //on clicking to adding button
        SalesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //here add button
                startActivity(new Intent(getActivity(), AddCatogry.class));

            }
        });

        return view;
    }
}