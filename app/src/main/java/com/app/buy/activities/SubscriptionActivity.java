package com.app.buy.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.buy.Adapters.BankAdapter;
import com.app.buy.Helper.Constants;
import com.app.buy.Helper.OnImageSelectedListener;
import com.app.buy.Model.BankModel;
import com.app.buy.R;
import com.app.buy.activities.authentication.LoginActivity;
import com.app.buy.databinding.ActivitySubscriptionBinding;
import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class SubscriptionActivity extends AppCompatActivity implements OnImageSelectedListener {
    String selectedPlan = Constants.VIP_YEAR;
    MaterialCardView selectedCard;
  public  static   String selectedPrice = "€130";
    private static final int PICK_FROM_GALLERY = 1001;
    private RecyclerView recyclerView;
    private BankAdapter bankAdapter;
    private List<BankModel> bankList;
    private DatabaseReference databaseReference;
    AtomicReference<String> year = new AtomicReference<>("");
    AtomicReference<String> six_month = new AtomicReference<>("");
    AtomicReference<String> month = new AtomicReference<>("");

    ActivitySubscriptionBinding binding;

    @Override
    public void onImageSelected(Uri imageUri, int pos) {
        if (pos < bankList.size()) {
            bankList.get(pos).setImage(String.valueOf(imageUri));
            bankAdapter.notifyItemChanged(pos);

        }
    }


    enum TYPE {
        MANUAL, GOOGLE
    }

    TYPE paymentType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubscriptionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        selectedCard = binding.annual;
        Constants.databaseReference().child(Constants.PRICES).get().addOnFailureListener(e -> {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Constants.dismissDialog();
        }).addOnSuccessListener(dataSnapshot -> {
            Constants.dismissDialog();
            if (dataSnapshot.exists()) {
                year.set(dataSnapshot.child("YEAR").getValue(String.class));
                six_month.set(dataSnapshot.child("SIX_MONTH").getValue(String.class));
                month.set(dataSnapshot.child("MONTH").getValue(String.class));
                updatePricing(year.get(), six_month.get(), month.get());
            } else {
                updatePricing(year.get(), six_month.get(), month.get());
            }
        });

        showDialog();

        binding.back.setOnClickListener(v -> {
onBackPressed();        });

        binding.annual.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_YEAR;
            Stash.put("selectedPlan", selectedPlan);

            if (paymentType == TYPE.MANUAL) {
                selectedPrice = "CFA" + year.get();
            } else {
                String s = year.get();
                Stash.put("price", s);
                selectedPrice = "CFA" + year.get();

            }
            updateView(binding.annual);
        });

        binding.half.setOnClickListener(v -> {
            selectedPlan = Constants.VIP_6_MONTH;
            Stash.put("selectedPlan", selectedPlan);

            if (paymentType == TYPE.MANUAL) {
                selectedPrice = "GNF" + six_month.get();
            } else {
                selectedPrice = "GNF" + six_month.get();
            }
            updateView(binding.half);
        });


        binding.start.setOnClickListener(v -> {
            if (paymentType == TYPE.MANUAL) {
                showManualPaymentInfo();
            } else {
//startActivity(new Intent(this, PaymentPaypalActivity.class));
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Constants.initDialog(this);
    }

    private void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bs_get_image);
        dialog.setCancelable(false);
        MaterialCardView google = dialog.findViewById(R.id.google);
        MaterialCardView manual = dialog.findViewById(R.id.manual);
        MaterialCardView cancel = dialog.findViewById(R.id.back);
        cancel.setVisibility(View.GONE);
        cancel.setOnClickListener(v -> {
            dialog.cancel();
        });
        manual.setOnClickListener(v -> {
            paymentType = TYPE.MANUAL;
            dialog.cancel();
            getPricing();
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void getPricing() {
        Constants.showDialog();
        Constants.databaseReference().child(Constants.PRICES).child(Stash.getString(Constants.TYPE)).get().addOnFailureListener(e -> {
            Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            Constants.dismissDialog();
        }).addOnSuccessListener(dataSnapshot -> {
            Constants.dismissDialog();
            if (dataSnapshot.exists()) {
                String year = dataSnapshot.child("YEAR").getValue(String.class);
                String six_month = dataSnapshot.child("SIX_MONTH").getValue(String.class);
                String month = dataSnapshot.child("MONTH").getValue(String.class);
                selectedPrice = "CFA" + year;
                updatePricing(year, six_month, month);
            } else {
                updatePricing(year.get(), six_month.get(), month.get());
            }
        });
    }

    private void updatePricing(String year, String six_month, String month) {
        binding.priceYear.setText("CFA " + year + "/year");
        binding.priceSixMonth.setText("GNF " + six_month + "/year");
        binding.priceMonth.setText("€" + month + "/month");
    }

    private void showManualPaymentInfo() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.manual_pay_all);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);
        dialog.show();

        recyclerView = dialog.findViewById(R.id.bankDetailsRecyclerView);
        LinearLayout dotsContainer = dialog.findViewById(R.id.dotsContainer);

        LinearLayoutManager layoutManager = new LinearLayoutManager(dialog.getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        bankList = new ArrayList<>();
        bankAdapter = new BankAdapter(this, bankList, (OnImageSelectedListener) this);
        recyclerView.setAdapter(bankAdapter);

        databaseReference = Constants.databaseReference().child("Banks");
        loadBankData(dotsContainer); // Pass dots container

        MaterialCardView cancel = dialog.findViewById(R.id.back);
        cancel.setOnClickListener(v -> dialog.cancel());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
                updateDotsIndicator(dotsContainer, position);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FROM_GALLERY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            BankAdapter.image = data.getData();
            onImageSelected(BankAdapter.image, BankAdapter.pos);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finishAffinity();
    }

    private void updateView(MaterialCardView selected) {
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
        selectedCard.setStrokeColor(getResources().getColor(R.color.light_gray));
        selectedCard = selected;
        selectedCard.setCardBackgroundColor(getResources().getColor(R.color.greenLight));
        selectedCard.setStrokeColor(getResources().getColor(R.color.greenDark));
    }

    private void loadBankData(LinearLayout dotsContainer) {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bankList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    BankModel bank = data.getValue(BankModel.class);
                    if (bank != null) {
                        bankList.add(bank);
                    }
                }
                bankAdapter.notifyDataSetChanged();

                // Log count to check if dotsContainer is being updated
                Log.d("DotsDebug", "Banks Loaded: " + bankList.size());

                // Add dots only after data is loaded
                addDotsIndicator(dotsContainer, bankList.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
            }
        });
    }

    private void addDotsIndicator(LinearLayout dotsContainer, int count) {
        dotsContainer.removeAllViews(); // Clear previous dots
        for (int i = 0; i < count; i++) {
            ImageView dot = new ImageView(dotsContainer.getContext());
            dot.setImageDrawable(ContextCompat.getDrawable(dotsContainer.getContext(), R.drawable.dot_inactive));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30, 30);
            params.setMargins(8, 0, 8, 0);
            dot.setLayoutParams(params);
            dotsContainer.addView(dot);
        }
        if (count > 0) {
            ((ImageView) dotsContainer.getChildAt(0)).setImageDrawable(ContextCompat.getDrawable(dotsContainer.getContext(), R.drawable.dot_active));
        }
    }

    private void updateDotsIndicator(LinearLayout dotsContainer, int position) {
        for (int i = 0; i < dotsContainer.getChildCount(); i++) {
            ImageView dot = (ImageView) dotsContainer.getChildAt(i);
            if (i == position) {
                dot.setImageDrawable(ContextCompat.getDrawable(dotsContainer.getContext(), R.drawable.dot_active));
            } else {
                dot.setImageDrawable(ContextCompat.getDrawable(dotsContainer.getContext(), R.drawable.dot_inactive));
            }
        }
    }
    private void sendNotificationToAdmin() {
        DatabaseReference tokenRef = FirebaseDatabase.getInstance().getReference("AdminTokens");
        tokenRef.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String adminToken = snapshot.getValue(String.class);

                // Prepare FCM Notification Data
                JSONObject notification = new JSONObject();
                JSONObject data = new JSONObject();
                try {
                    data.put("title", "New Proof Uploaded");
                    data.put("body", "A user has uploaded proof. Tap to review.");
//                    data.put("proof_url", proofUrl);

                    notification.put("to", adminToken);
                    notification.put("data", data);

                    sendFCMNotification(notification);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // Send FCM Request to Firebase
    private void sendFCMNotification(JSONObject notification) {
        String FCM_API = "https://fcm.googleapis.com/fcm/send";
        String SERVER_KEY = "key=YOUR_SERVER_KEY_HERE";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM", "Notification Sent: " + response.toString()),
                error -> Log.e("FCM", "Error Sending Notification: " + error.toString())) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", SERVER_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }


}