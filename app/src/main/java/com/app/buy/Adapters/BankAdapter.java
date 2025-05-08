package com.app.buy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.buy.Helper.Config;
import com.app.buy.Helper.Constants;
import com.app.buy.Helper.FCMHelper;
import com.app.buy.Helper.OnImageSelectedListener;
import com.app.buy.Helper.UserModel;
import com.app.buy.Model.BankModel;
import com.app.buy.R;
import com.app.buy.activities.PaymentModel;
import com.app.buy.activities.SubscriptionActivity;
import com.app.buy.activities.authentication.LoginActivity;
import com.bumptech.glide.Glide;
import com.fxn.stash.Stash;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class BankAdapter extends RecyclerView.Adapter<BankAdapter.BankViewHolder> {
    private Context context;
    private List<BankModel> bankList;
    private DatabaseReference databaseReference;
    String selectedPlan = Constants.VIP_YEAR;
    MaterialCardView selectedCard;
    public static Uri image = null;
    public static int pos = 0;
    OnImageSelectedListener listener;
    static TextView bankName;
    static TextView accountHolder;
    static TextView accountNumber;
    static TextView extraInfo;

    private static final int PICK_FROM_GALLERY = 1001;

    public BankAdapter(Context context, List<BankModel> bankList, OnImageSelectedListener listener) {
        this.context = context;
        this.bankList = bankList;
        this.listener = listener;

        databaseReference = Constants.databaseReference().child("Banks");
    }

    @NonNull
    @Override
    public BankViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.manual_pay_info, parent, false);
        return new BankViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BankViewHolder holder, int position) {
        BankModel bank = bankList.get(position);
        bankName.setText("Bank Name: " + bank.getBankName());
        accountHolder.setText("Account Holder Name: " + bank.getAccountHolder());
        accountNumber.setText("Account Number: " + bank.getAccountNumber());
        extraInfo.setText("Additional Info: " + bank.getExtraInfo());

        if (!bank.getLogoUrl().isEmpty()) {
            Glide.with(context).load(bank.getLogoUrl()).into(holder.bankLogo);
        }

        if (bank.getImage() != null) {
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(context).load(bank.getImage()).into(holder.imageView);
        } else {
            holder.imageView.setVisibility(View.GONE);
        }

        holder.upload.setOnClickListener(v -> pickImage(position));
        holder.imageCard.setOnClickListener(v -> pickImage(position));


        holder.manual.setOnClickListener(v -> {
            if (image == null) {
                Toast.makeText(context, "Please upload a proof image", Toast.LENGTH_SHORT).show();
                return;
            }
            String selectedType = Stash.getString(Constants.TYPE);
            if (bank.getImage() != null) {
                uploadProof(selectedType);
            } else {
                Toast.makeText(context, "Please upload image", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bankList.size();
    }

    public static class BankViewHolder extends RecyclerView.ViewHolder {
        ImageView bankLogo;
        MaterialCardView manual;
        MaterialCardView upload;
        CardView imageCard;
        public static ImageView imageView, icon;

        public BankViewHolder(@NonNull View itemView) {
            super(itemView);
            bankName = itemView.findViewById(R.id.bankName);
            accountHolder = itemView.findViewById(R.id.accountHolder);
            accountNumber = itemView.findViewById(R.id.accountNumber);
            extraInfo = itemView.findViewById(R.id.extraInfo);
            bankLogo = itemView.findViewById(R.id.bankLogo);
            manual = itemView.findViewById(R.id.manual);
            upload = itemView.findViewById(R.id.upload);
            imageCard = itemView.findViewById(R.id.imageCard);
            imageView = itemView.findViewById(R.id.imageView);
            icon = itemView.findViewById(R.id.icon);
//
        }
    }

    private void uploadProof(String selectedType) {
        Constants.showDialog();
        String date = new SimpleDateFormat("ddMMyyyyhhmmss", Locale.getDefault()).format(new Date());
        Constants.storageReference(Constants.auth().getCurrentUser().getUid()).child("proof").child(date).putFile(image)
                .addOnSuccessListener(taskSnapshot -> {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                        upload(uri.toString(), selectedType);
                    });
                })
                .addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void upload(String imageLink, String selectedType) {
        String duration;
        if (selectedPlan.equals(Constants.VIP_YEAR)) {
            duration = "YEAR";
        } else if (selectedPlan.equals(Constants.VIP_6_MONTH)) {
            duration = "6 MONTH";
        } else {
            duration = "3 MONTH";
        }

        UserModel userModel = (UserModel) Stash.getObject(Constants.STASH_USER, UserModel.class);
        PaymentModel paymentModel = new PaymentModel(
                UUID.randomUUID().toString(),
                userModel.getKey(),
                userModel.getName(),
                userModel.getEmail(),
                SubscriptionActivity.selectedPrice,
                duration,
                imageLink,
                Stash.getString("type", "user"),
                new Date().getTime(),
                false,
                bankName.getText().toString(),
                accountHolder.getText().toString(),
                accountNumber.getText().toString());

        Constants.databaseReference().child(Constants.COURSE_PAYMENTS).child(FirebaseAuth.getInstance().getUid())
                .setValue(paymentModel)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(context, "Thank you for being our valuable user", Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
//                    context.finishAffinity();
                })
                .addOnFailureListener(e -> {
                    Constants.dismissDialog();
                    Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                });

//        Constants.databaseReference().child(Constants.PAYMENTS).child(userModel.getID()).child(paymentModel.getID())
//                .setValue(paymentModel)
//                .addOnSuccessListener(unused -> {
//
//                })
//                .addOnFailureListener(e -> {
//                    Constants.dismissDialog();
//                    Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
//                });
    }

    private void pickImage(int position) {
        sendFCMPush("c7mTheloSFOYSq6YYHrQag:APA91bEz-2ksAfVNa_aELjN6ZlMethR6Q6Srb0V4cAfhQ5Ou2I6XtGpVlCZ_hwdE2XwV7RhoKEyDtgkbKAdOBx5xaaRmEDMRmeONieXBPgd1xiOF5NnHeK0 ");

        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pos = position;
        ((SubscriptionActivity) context).startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_FROM_GALLERY);
    }

    private void sendNotificationToAdmin() {
        JSONObject notification = new JSONObject();
        JSONObject data = new JSONObject();

        try {
            data.put("title", "Hello User!");
            data.put("body", "This is a test notification.");
            data.put("click_action", "OPEN_ACTIVITY");

            notification.put("to", "c7mTheloSFOYSq6YYHrQag:APA91bEz-2ksAfVNa_aELjN6ZlMethR6Q6Srb0V4cAfhQ5Ou2I6XtGpVlCZ_hwdE2XwV7RhoKEyDtgkbKAdOBx5xaaRmEDMRmeONieXBPgd1xiOF5NnHeK0");
            notification.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        sendFCMNotification(context, notification);

    }
    public static void sendFCMNotification(Context context, JSONObject notification) {
        if (context == null) {
            Log.e("FCM", "Context is null, cannot send notification.");
            return;
        }
         String FCM_API = "https://fcm.googleapis.com/v1/projects/sos-app-63a86/messages:send";
        String SERVER_KEY = "BA4Uwvv-8obG-Nk4yMbIStVCtgDcsihGeAPxx6Hj-M1mBioCf7tVyzi8n_QyO7FkCaJvCJ7seIf_OW6oeNa88yE";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM", "Notification Sent Successfully: " + response.toString()),
                error -> {
                    Log.e("FCM", "Error Sending Notification: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("FCM", "Response Code: " + error.networkResponse.statusCode);
                        if (error.networkResponse.data != null) {
                            try {
                                String errorMessage = new String(error.networkResponse.data);
                                Log.e("FCM", "Response Data: " + errorMessage);
                                // Check if the error response is in JSON format
                                if (errorMessage.startsWith("{") && errorMessage.endsWith("}")) {
                                    try {
                                        JSONObject errorJson = new JSONObject(errorMessage);
                                        Log.e("FCM", "Error JSON: " + errorJson.toString());
                                    } catch (JSONException e) {
                                        Log.e("FCM", "Failed to parse error response as JSON", e);
                                    }
                                } else {
                                    Log.e("FCM", "Error response is not in JSON format");
                                }
                            } catch (Exception e) {
                                Log.e("FCM", "Failed to read error response", e);
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "key=" + SERVER_KEY);
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(context.getApplicationContext()); // Use application context
        queue.add(request);
    }

    private void sendFCMPush(String token) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Order Alert");
            notifcationBody.put("message", "You got a new Order");
            notification.put("to", token);
            notification.put("data", notifcationBody);

            Log.e("DATAAAAAA", notification.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.POST,
                Config.NOTIFICATIONAPIURL,
                notification,
                response -> {
                    Log.e("True", response + "");
                    Log.d("Response", response.toString());
                },
                error -> {
                    Log.e("False", error + "");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "key=" + Config.SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 1000 * 60; // 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

}
