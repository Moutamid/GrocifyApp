package com.example.groceryshoppingsystem.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.groceryshoppingsystem.AdminPanel.OrderDetailsActivity;
import com.example.groceryshoppingsystem.Helper.Config;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.ViewHolder> {
    private List<MyorderModel> orderItemList;
    private Context context;
    DatabaseReference root;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView orderDate, orderNums, orderPrice, orderProducts, OrderCheck, textView2;
        private TextView orderAddress, orderEmail, orderPhone, orderName;
        private Button ScanQrCode;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderAddress = itemView.findViewById(R.id.orderAddress);
            orderEmail = itemView.findViewById(R.id.orderEmail);
            orderPhone = itemView.findViewById(R.id.orderPhone);
            orderName = itemView.findViewById(R.id.orderName);
            orderDate = itemView.findViewById(R.id.orderDate);
            orderNums = itemView.findViewById(R.id.orderNums);
            orderPrice = itemView.findViewById(R.id.orderPrice);
            orderProducts = itemView.findViewById(R.id.orderProducts);
            OrderCheck = itemView.findViewById(R.id.OrderCheck);
            ScanQrCode = itemView.findViewById(R.id.ScanQRCode);
            textView2 = itemView.findViewById(R.id.textView2);
            root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");

        }
    }


    public AdminOrderAdapter(Context context, List<MyorderModel> orderItemList) {
        this.context = context;
        this.orderItemList = orderItemList;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View orderItemview = LayoutInflater.from(context).inflate(R.layout.admin_order_itemlayout, parent, false);
        return new ViewHolder(orderItemview);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyorderModel model = orderItemList.get(position);
        holder.textView2.setText(position + 1 + "");
        holder.orderDate.setText(model.getDate());
        holder.orderNums.setText(model.getOrderNums());
        holder.orderPrice.setText(model.getOrderPrice());
        holder.orderProducts.setText(model.getOrderProducts());
        holder.orderName.setText(model.getOrderName());
        holder.orderPhone.setText(model.getOrderPhone());
        holder.orderEmail.setText(model.getOrderEmail());
        holder.orderAddress.setText(model.getOrderAddress());
        if (model.getType() == 1) {
            holder.OrderCheck.setText("Order Confirm");
        } else if (model.getType() == 2) {
            holder.OrderCheck.setText("Order Deliver");
        } else if (model.getType() == 3) {
            holder.OrderCheck.setText("Order Complete");
        }
        else  if (model.getType() == 4)
        {
            holder.OrderCheck.setVisibility(View.GONE);
        }
        holder.OrderCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderItemList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, orderItemList.size());

                if (model.getType() == 1) {
                    sendFCMPush(model.getToken(), "Your order is confirmed and being ready for dispatch");
                    root.child("order").child(model.getUid()).child(model.getKey()).child("status").setValue("confirm");
                } else if (model.getType() == 2) {
                    sendFCMPush(model.getToken(), "Your order is dispatched, soon you will get");
                    root.child("order").child(model.getUid()).child(model.getKey()).child("status").setValue("deliver");
                } else if (model.getType() == 3) {
                    sendFCMPush(model.getToken(), "Your order is successfully delivered");
                    root.child("order").child(model.getUid()).child(model.getKey()).child("status").setValue("complete");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }

    private void sendFCMPush(String token, String message) {
        JSONObject notification = new JSONObject();
        JSONObject notifcationBody = new JSONObject();

        try {
            notifcationBody.put("title", "Order Alert");
            notifcationBody.put("message", message);

            notification.put("to", token); // Use the FCM token of the recipient device
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
