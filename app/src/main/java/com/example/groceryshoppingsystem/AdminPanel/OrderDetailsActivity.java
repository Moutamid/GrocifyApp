package com.example.groceryshoppingsystem.AdminPanel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.groceryshoppingsystem.R;
import com.example.groceryshoppingsystem.UI.OrderFregmant;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe,new OrderDetailsFregmant()).commit();

    }
}