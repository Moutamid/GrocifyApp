package com.example.groceryshoppingsystem.UI;

import static com.example.groceryshoppingsystem.Helper.Config.checkApp;

import android.content.Intent;
import android.os.AsyncTask;
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

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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
        checkApp(MainActivity.this);
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
//                String externalDeliveryId = "12357898";
//                String pickupAddress = "600 4th Ave, Seattle, WA 98101";
//                String pickupBusinessName = "Seattle City Hall";
//                String pickupPhoneNumber = "+12065551212";
//                String pickupInstructions = "Knock on the front door";
//                String pickupReferenceTag = "Order for the Sounders Mascot";
//                String dropoffAddress = "800 Occidental Ave S, Seattle, WA 98134";
//                String dropoffBusinessName = "Lumen Field";
//                String dropoffPhoneNumber = "+12065551212";
//                String dropoffInstructions = "Enter gate code 1234 on the callbox.";
//                String dropoffContactGivenName = "Sammy the";
//                String dropoffContactFamilyName = "Sounder";
//                boolean dropoffContactSendNotifications = true;
//                String schedulingModel = "asap";
//                int orderValue = 1999;
//                int tip = 599;
//                String currency = "USD";
//                boolean contactlessDropoff = false;
//                String actionIfUndeliverable = "return_to_pickup";
//
////                TODO API work with retofit
//                // Create the CreateDeliveryRequest object
//                CreateDeliveryRequest createDeliveryRequest = new CreateDeliveryRequest();
//                     createDeliveryRequest.externalDeliveryId= externalDeliveryId;
//                createDeliveryRequest.externalDeliveryId = externalDeliveryId;
//                createDeliveryRequest.pickupAddress = pickupAddress;
//                createDeliveryRequest.pickupBusinessName = pickupBusinessName;
//                createDeliveryRequest.pickupPhoneNumber = pickupPhoneNumber;
//                createDeliveryRequest.pickupInstructions = pickupInstructions;
//                createDeliveryRequest.pickupReferenceTag = pickupReferenceTag;
//                createDeliveryRequest.dropoffAddress = dropoffAddress;
//                createDeliveryRequest.dropoffBusinessName = dropoffBusinessName;
//                createDeliveryRequest.dropoffPhoneNumber = dropoffPhoneNumber;
//                createDeliveryRequest.dropoffInstructions = dropoffInstructions;
//                createDeliveryRequest.dropoffContactGivenName = dropoffContactGivenName;
//                createDeliveryRequest.dropoffContactFamilyName = dropoffContactFamilyName;
//                createDeliveryRequest.dropoffContactSendNotifications = dropoffContactSendNotifications;
//                createDeliveryRequest.schedulingModel = schedulingModel;
//                createDeliveryRequest.orderValue = orderValue;
//                createDeliveryRequest.tip = tip;
//                createDeliveryRequest.currency = currency;
//                createDeliveryRequest.contactlessDropoff = contactlessDropoff;
//                createDeliveryRequest.actionIfUndeliverable = actionIfUndeliverable;
//
////                createDelivery(Config.getJWT(), createDeliveryRequest);
//                Log.d("API", Config.getJWT());
//                new NetworkTask().execute();


                startActivity(new Intent(MainActivity.this, CartActivity.class));
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

        Call<CreateDeliveryRequest> call = doorDashService.createDelivery(("Bearer "+ jwt), request);
        call.enqueue(new Callback<CreateDeliveryRequest>() {

            @Override
            public void onResponse(Call<CreateDeliveryRequest> call, Response<CreateDeliveryRequest> response) {
                if (response.isSuccessful()) {
                    CreateDeliveryRequest deliveryResponse = response.body();
                    // Handle the successful response with the non-null deliveryResponse object
                    Log.e("Response Body", "body " + deliveryResponse);
                } else {
                    // Handle unsuccessful response
                    try {
                        String errorBodyString = response.errorBody().string();
                        Log.e("Error Response Body", errorBodyString);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<CreateDeliveryRequest> call, Throwable t) {
                Log.e("Error Response Body","error"+ t.getMessage());
                Log.e("Error Response Body","error"+ t.getCause());
                Log.e("Error Response Body","error"+ t.getSuppressed());
                Log.e("Error Response Body","error"+ t.getStackTrace());
                Log.e("Error Response Body","error"+ t.toString());


            }
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Log.e("Response Body", "body " + response.body());
//                    Log.e("Response Body", "body " + response.message());
//                    Log.e("Response Body", "body " + response.code());
//                    Log.e("Response Body", "body " + response.errorBody());
//                    Log.e("Response Body", "body " + response.raw());
//                    Log.e("Response Body", "body " + response.headers());
//                    Log.e("Response Body", "body " + response.toString());
//                } else {
//                    try {
//                        String errorBodyString = response.errorBody().string();
//                        Log.e("Error Response Body", errorBodyString);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Log.d("Error Response Body", "Network error" + "    " + t.toString() + "  " + t.getMessage() + "   " + t.getCause() + "   " + t.getLocalizedMessage());
////                Toast.makeText(MainActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
////                System.out.println("Response" + t.getMessage());
//            }
        });
    }



    private static class NetworkTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            try {
                return String.valueOf(performNetworkRequest());
            } catch (IOException e) {
                e.printStackTrace();
                return "error"; // or handle error code appropriately
            }
        }

        @Override
        protected void onPostExecute(String responseCode) {
            Log.d("code", responseCode + " code");
        }

        private String performNetworkRequest() throws IOException {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            MediaType mediaType = MediaType.parse("application/json");
            RequestBody body = RequestBody.create(mediaType, "{\n  \"external_delivery_id\": \"1989dd721\",\n  \"pickup_address\": \"600 4th Ave, Seattle, WA 98101\",\n  \"pickup_business_name\": \"Seattle City Hall\",\n  \"pickup_phone_number\": \"+12065551212\",\n  \"pickup_instructions\": \"Knock on the front door\",\n  \"pickup_reference_tag\": \"Order for the Sounders Mascot\",\n  \"dropoff_address\": \"800 Occidental Ave S, Seattle, WA 98134\",\n  \"dropoff_business_name\": \"Lumen Field\",\n  \"dropoff_phone_number\": \"+12065551212\",\n  \"dropoff_instructions\": \"Enter gate code 1234 on the callbox.\",\n  \"dropoff_contact_given_name\": \"Sammy the\",\n  \"dropoff_contact_family_name\": \"Sounder\",\n  \"dropoff_contact_send_notifications\": true,\n  \"scheduling_model\": \"asap\",\n  \"order_value\": 1999,\n  \"tip\": 599,\n  \"currency\": \"USD\",\n  \"contactless_dropoff\": false,\n  \"action_if_undeliverable\": \"return_to_pickup\"\n}");
            Request request = new Request.Builder()
                    .url("https://openapi.doordash.com/drive/v2/deliveries")
                    .method("POST", body)
                    .addHeader("Authorization", "Bearer " + Config.getJWT())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Cookie", "__cf_bm=pI83KBJqPqQHl.BT8ZYTTVrXtB4QCmDcXq2WJx7NBH8-1700916181-0-AXFa7l/PrYfTXf/ucVm2pI5v9BIaeKWcXeqOYsq2eQeHeeymmuLuLxIv38F6c/44NgRqaKFRV06OYk+v0OChhCQ=")
                    .build();

            try {
                okhttp3.Response response = client.newCall(request).execute();

                return response.body().string();
            } catch (Exception e) {
                return "error"+ e.toString(); // or handle error code appropriately
            }
        }
    }
}
