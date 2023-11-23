package com.example.groceryshoppingsystem.AdminPanel;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.groceryshoppingsystem.R;

public class AdminOrderFregmant extends Fragment {
    Button order_confirm, order_deliver, order_complete;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_admin, container, false);
        order_complete = view.findViewById(R.id.order_complete);
        order_deliver = view.findViewById(R.id.order_deliver);
        order_confirm = view.findViewById(R.id.order_confirm);
        order_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), OrderDetailsActivity.class));

            }
        });
        return view;
    }
}