package com.example.groceryshoppingsystem.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.groceryshoppingsystem.Adapters.GridproductAdapter;
import com.example.groceryshoppingsystem.Adapters.My_Adapter;
import com.example.groceryshoppingsystem.Helper.Config;
import com.example.groceryshoppingsystem.Helper.CreateDeliveryRequest;
import com.example.groceryshoppingsystem.Helper.DoorDashService;
import com.example.groceryshoppingsystem.Model.HorizontalProductModel;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.Model.model;
import com.example.groceryshoppingsystem.Model.user;
import com.example.groceryshoppingsystem.R;
import com.gauravk.bubblenavigation.BubbleNavigationLinearView;
import com.gauravk.bubblenavigation.listener.BubbleNavigationChangeListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //    private Toolbar mToolBar;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle mtoggle;


    private static final String BASE_URL = "https://openapi.doordash.com/";

    private CircleImageView image;
    private TextView mperson_name;
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
    //    private NavigationView navigationView;
    private ViewPager pager;
    private My_Adapter adapter;
    private List<model> models;
    private DatabaseReference m;
    //    private View mnavigationview;
    private static List<favouritesClass> favourites;
    //Custom Xml Views (cart Icon)
    private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;


    BubbleNavigationLinearView top_navigation_constraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top_navigation_constraint = findViewById(R.id.top_navigation_constraint);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();
        getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new HomeFregmant()).commit();
        top_navigation_constraint.setNavigationChangeListener(new BubbleNavigationChangeListener() {
            @Override
            public void onNavigationChanged(View view, int position) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new HomeFregmant()).commit();

                } else if (position == 1) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new CategoryFragment()).commit();
                } else if (position == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new FavouriteFregmant()).commit();
                } else if (position == 3) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new OrderFregmant()).commit();

                } else if (position == 4) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new ProfileFregmant()).commit();

                }

            }
        });

//        navigationView = findViewById(R.id.navegation_view);
//        navigationView.setNavigationItemSelectedListener(this);
//        mnavigationview = navigationView.getHeaderView(0);
//        mperson_name = mnavigationview.findViewById(R.id.persname);
//        image = mnavigationview.findViewById(R.id.circimage);
//        drawerLayout = findViewById(R.id.drawer);
//
//        mToolBar = findViewById(R.id.main_TooBar);
//        setSupportActionBar(mToolBar);
//        mtoggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close);
//        drawerLayout.addDrawerListener(mtoggle);
//        mtoggle.syncState();
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void onStart() {
        super.onStart();
         showCartIcon();
        HandleTotalPriceToZeroIfNotExist();
    }



    @Override
    public void onStop() {
        super.onStop();
    }

    private void setNumberOfItemsInCartIcon() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.getChildrenCount() == 1) {
                        CustomCartNumber.setVisibility(View.GONE);
                    } else {
                        CustomCartNumber.setVisibility(View.VISIBLE);
                        CustomCartNumber.setText(String.valueOf(dataSnapshot.getChildrenCount() - 1));
                    }
                } else {
                    CustomCartNumber.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);
    }


    private void HandleTotalPriceToZeroIfNotExist() {
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference m = root.child("cart").child(Uid);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("cart").child(Uid).child("totalPrice").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
        m.addListenerForSingleValueEvent(eventListener);

    }

    private void showCartIcon() {
        CustomCartContainer = (RelativeLayout) findViewById(R.id.CustomCartIconContainer);
        PageTitle = (TextView) findViewById(R.id.PageTitle);
        CustomCartNumber = (TextView) findViewById(R.id.CustomCartNumber);

        PageTitle.setText("Grocify");
        setNumberOfItemsInCartIcon();

        CustomCartContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                TODO API work with retofit
//                CreateDeliveryRequest createDeliveryRequest = new CreateDeliveryRequest(
//                        "123",                              // external_delivery_id
//                        "123 Main St",                       // pickup_address
//                        "Business Name",                     // pickup_business_name
//                        "+12065551212",                      // pickup_phone_number
//                        "Knock on the front door",           // pickup_instructions
//                        "1600 Amphitheatre Parkway",         // dropoff_address
//                        "Lumen Field",                       // dropoff_business_name
//                        "+12065551212",                      // dropoff_phone_number
//                        "Enter gate code 1234 on the callbox.", // dropoff_instructions
//                        "Sammy the",                         // dropoff_contact_given_name
//                        true,                                // dropoff_contact_send_notifications
//                        "asap",                              // scheduling_model
//                        1999,                                // order_value
//                        0,                                   // tip
//                        "USD",                               // currency
//                        false,                               // contactless_dropoff
//                        "return_to_pickup"                   // action_if_undeliverable
//                );
//                createDelivery(Config.getJWT(), createDeliveryRequest);
                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }



    private static DoorDashService createService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Connection timeout
            .readTimeout(30, TimeUnit.SECONDS)    // Read timeout
            .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DoorDashService.class);
    }

    public void createDelivery(String jwt, CreateDeliveryRequest request) {
        DoorDashService doorDashService = createService();

        Call<Void> call = doorDashService.createDelivery("Bearer " + jwt, request);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, retrofit2.Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "successfull" + response.code(), Toast.LENGTH_SHORT).show();
                    System.out.println("Delivery created successfully!");
                } else {
                    Toast.makeText(MainActivity.this, "error" + response.code(), Toast.LENGTH_SHORT).show();
                    System.out.println("Error: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

                Toast.makeText(MainActivity.this,"Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                System.out.println("Network error: " + t.getMessage());
            }
        });
    }

}
