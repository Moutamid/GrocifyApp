package com.example.groceryshoppingsystem.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.groceryshoppingsystem.Adapters.My_Adapter;
import com.example.groceryshoppingsystem.Helper.Config;
import com.example.groceryshoppingsystem.Helper.CreateDeliveryRequest;
import com.example.groceryshoppingsystem.Helper.DoorDashService;
import com.example.groceryshoppingsystem.Model.favouritesClass;
import com.example.groceryshoppingsystem.Model.model;
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
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
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
        System.setProperty("javax.net.debug", "all");
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
                String externalDeliveryId = "1219";
                String pickupAddress = "600 4th Ave, Seattle, WA 98101";
                String pickupBusinessName = "Seattle City Hall";
                String pickupPhoneNumber = "+12065551212";
                String pickupInstructions = "Knock on the front door";
                String pickupReferenceTag = "Order for the Sounders Mascot";
                String dropoffAddress = "800 Occidental Ave S, Seattle, WA 98134";
                String dropoffBusinessName = "Lumen Field";
                String dropoffPhoneNumber = "+12065551212";
                String dropoffInstructions = "Enter gate code 1234 on the callbox.";
                String dropoffContactGivenName = "Sammy the";
                String dropoffContactFamilyName = "Sounder";
                boolean dropoffContactSendNotifications = true;
                String schedulingModel = "asap";
                int orderValue = 1999;
                int tip = 599;
                String currency = "USD";
                boolean contactlessDropoff = false;
                String actionIfUndeliverable = "return_to_pickup";

//                TODO API work with retofit
                // Create the CreateDeliveryRequest object
                CreateDeliveryRequest createDeliveryRequest = new CreateDeliveryRequest(
                        externalDeliveryId,
                        pickupAddress,
                        pickupBusinessName,
                        pickupPhoneNumber,
                        pickupInstructions,
                        pickupReferenceTag,
                        dropoffAddress,
                        dropoffBusinessName,
                        dropoffPhoneNumber,
                        dropoffInstructions,
                        dropoffContactGivenName,
                        dropoffContactFamilyName,
                        dropoffContactSendNotifications,
                        schedulingModel,
                        orderValue,
                        tip,
                        currency,
                        contactlessDropoff,
                        actionIfUndeliverable
                );
                createDelivery(Config.getJWT(), createDeliveryRequest);
//                startActivity(new Intent(MainActivity.this, CartActivity.class));
            }
        });
    }



    private static DoorDashService createService() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(50, TimeUnit.SECONDS) // Connection timeout
                .readTimeout(50, TimeUnit.SECONDS)    // Read timeout
                .build();

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(DoorDashService.class);
    }

    public void createDelivery(String jwt, CreateDeliveryRequest request) {
        Log.d("Response Body", jwt+"");

        DoorDashService doorDashService = createService();

        Call<Void> call = doorDashService.createDelivery(("Bearer "+ jwt), request);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.e("Response Body", "body "+ response.errorBody());
                } else {
                    try {
                        String errorBodyString = response.errorBody().string();
                        Log.e("Error Response Body", errorBodyString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.d("Error Response Body", "Network error" + "    " + t.toString() + "  " + t.getMessage() + "   " + t.getCause() + "   " + t.getLocalizedMessage());
//                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                System.out.println("Response" + t.getMessage());
            }
        });
    }

}
