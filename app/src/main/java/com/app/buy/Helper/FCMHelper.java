package com.app.buy.Helper;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class FCMHelper {
    private static final String FCM_API = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "AAAAXCqtD_4:APA91bHJIakS6WJcLMnubaLoG0C1ty5ppQGyyfAgKkg19MejI_6dBBNKQvs8h_BMJZVJwQbdKu0JzyyDfKV7ta7zEim2lTPI7-nnxkW-S5up1LBwOau3a0Ti_RiPNoGUd7q1ZMDyf-ks";

    public static void sendFCMNotification(Context context, JSONObject notification) {
        if (context == null) {
            Log.e("FCM", "Context is null, cannot send notification.");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, FCM_API, notification,
                response -> Log.d("FCM", "Notification Sent Successfully: " + response.toString()),
                error -> {
                    Log.e("FCM", "Error Sending Notification: " + error.toString());
                    if (error.networkResponse != null) {
                        Log.e("FCM", "Response Code: " + error.networkResponse.statusCode);
                        if (error.networkResponse.data != null) {
                            Log.e("FCM", "Response Data: " + new String(error.networkResponse.data));
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

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(request);
    }
}
