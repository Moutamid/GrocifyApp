package com.example.groceryshoppingsystem.AdminPanel;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshoppingsystem.R;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        int type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new OrderPendingFregmant()).commit();
        } else if (type == 2) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new OrderConfirmFregmant()).commit();
        } else if (type == 3) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new OrderDeliverFregmant()).commit();
        }else if (type == 4) {
            getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new OrderCompleteFregmant()).commit();
        }
    }

    public void backPress(View view) {
        onBackPressed();
    }
}