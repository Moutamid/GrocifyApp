package com.example.groceryshoppingsystem.UI;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshoppingsystem.Helper.Config;
import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class CartCheckActivity extends AppCompatActivity {
    String ttlPrice;
    DatabaseReference root;
    private EditText UserName, UserEmail, UserPhone, editTextAddress, editTextProvince, editTextComments;

    String CurrentUser;
    TextView totalPrice, DelivaryPrice, totalPrice2, savedamount;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUsr;
    private String UserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_check);
        mAuth = FirebaseAuth.getInstance();
        CurrentUsr = mAuth.getCurrentUser();
        UserId = CurrentUsr.getUid();
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextProvince = findViewById(R.id.editTextProvince);
        editTextComments = findViewById(R.id.editTextComments);
        UserName = findViewById(R.id.editTextName);
        UserEmail = findViewById(R.id.editTextEmail);
        UserPhone = findViewById(R.id.editTextPhone);
        Button Done = findViewById(R.id.Done);
        Button Cancel = findViewById(R.id.Cancel);
        totalPrice = findViewById(R.id.Ttl_ItemsPrice);
        DelivaryPrice = findViewById(R.id.DelivaryPrice);
        totalPrice2 = findViewById(R.id.TotalAmount);
        savedamount = findViewById(R.id.SavedAmount);
        getUserProfileData();
        editTextProvince.setVisibility(View.GONE);

        Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject deliveryData = createDeliveryRequestData();

                // Create HTTP request
                sendDeliveryRequest(CartCheckActivity.this, Config.getJWT(), deliveryData);

//                savedDate();
//                CartActivity.fa.finish();
//                finish();
            }
        });
        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getCheckData();

    }


    private void getCheckData() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        FirebaseAuth mAuth;
        mAuth = FirebaseAuth.getInstance();
        String CurrentUser = mAuth.getCurrentUser().getUid();
        DatabaseReference m = root.child("cart").child(CurrentUser);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    ttlPrice = dataSnapshot.child("totalPrice").getValue().toString();
                    Log.d("ttl", ttlPrice);
                    totalPrice.setText(ttlPrice);
                    DelivaryPrice.setText("Free");
                    totalPrice2.setText(ttlPrice);
                    savedamount.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }

    private void savedDate() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                    return;
                }
                String token = task.getResult();
                Log.d("TAG", "FCM Token: " + token);
                root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
                FirebaseAuth mAuth;
                mAuth = FirebaseAuth.getInstance();
                CurrentUser = mAuth.getCurrentUser().getUid();
                DatabaseReference x = root.child("cart").child(CurrentUser);
                x.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseDatabase t = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/");
                        String key = t.getReference("order").push().getKey();
                        assert key != null;
                        root.child("order").child(CurrentUser).child(key).child("name").setValue(UserName.getText().toString());
                        root.child("order").child(CurrentUser).child(key).child("phonenumber").setValue(UserPhone.getText().toString());
                        root.child("order").child(CurrentUser).child(key).child("email").setValue(UserEmail.getText().toString());
                        root.child("order").child(CurrentUser).child(key).child("address").setValue(editTextAddress.getText().toString());
                        root.child("order").child(CurrentUser).child(key).child("comments").setValue(editTextComments.getText().toString());
                        root.child("order").child(CurrentUser).child(key).child("orderproducts").setValue(snapshot.getValue());
                        root.child("order").child(CurrentUser).child(key).child("totalPrice").setValue(snapshot.child("totalPrice").getValue());
                        root.child("order").child(CurrentUser).child(key).child("orderproducts").child("totalPrice").removeValue();
                        root.child("order").child(CurrentUser).child(key).child("Date").setValue(String.valueOf(new SimpleDateFormat("dd MMM yyyy hh:mm a").format(Calendar.getInstance().getTime())));
                        root.child("order").child(CurrentUser).child(key).child("IsChecked").setValue("false");
                        root.child("order").child(CurrentUser).child(key).child("status").setValue("pending");
                        root.child("order").child(CurrentUser).child(key).child("token").setValue(token);
                        root.child("order").child(CurrentUser).child(key).child("uid").setValue(CurrentUser);
                        root.child("order").child(CurrentUser).child(key).child("key").setValue(key);
                        Toast.makeText(getApplicationContext(), "Order is successfully placed", Toast.LENGTH_LONG).show();
                        root.child("cart").child(CurrentUser).removeValue();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }

    private static JSONObject createDeliveryRequestData() {
        try {
            JSONObject requestData = new JSONObject();
            requestData.put("external_delivery_id", "71206");
            requestData.put("pickup_address", "1565 Charleston Rd, Mountain View, CA 94043");
            requestData.put("pickup_business_name", "Seattle City Hall");
            requestData.put("pickup_phone_number", "+12065551212");
            requestData.put("pickup_instructions", "Knock on the front door");
            requestData.put("dropoff_address", "1600 Amphitheatre Parkway, Mountain View, CA 94043");
            requestData.put("dropoff_business_name", "Lumen Field");
            requestData.put("dropoff_phone_number", "+12065551212");
            requestData.put("dropoff_instructions", "Enter gate code 1234 on the callbox.");
            requestData.put("dropoff_contact_given_name", "Sammy the");
            requestData.put("dropoff_contact_send_notifications", true);
            requestData.put("scheduling_model", "asap");
            requestData.put("order_value", 1999);
            requestData.put("tip", 0);
            requestData.put("currency", "USD");
            requestData.put("contactless_dropoff", false);
            requestData.put("action_if_undeliverable", "return_to_pickup");

            return requestData;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void sendDeliveryRequest(Context context, String jwt, JSONObject requestData) {
        if (requestData == null) {
            // Handle the case where the request data is not valid
            return;
        }
        Log.d("API", jwt + "");


        // Create a RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Define the URL for creating deliveries
        String url = "https://openapi.doordash.com/drive/v2/deliveries/";

        // Send JSON data in the request body
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Handle the successful response
                        Log.d("API", "Delivery created successfully!");
//                        Log.d("API", "" + response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle errors
                        Log.d("API", "Error creating delivery: " );
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                // Include the JWT token in the Authorization header
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + jwt);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        // Add the request to the RequestQueue
        queue.add(request);
        int socketTimeout = 30000; // 30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

    }

    private void getUserProfileData() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference m = root.child("users").child(UserId);
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String Name = snapshot.child("Name").getValue().toString();
                    String Phone = snapshot.child("Phone").getValue().toString();
                    UserName.setText(Name);
                    UserPhone.setText(Phone);
                    UserEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail().toString());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }


    public void backPress(View view) {
        onBackPressed();
    }

}
