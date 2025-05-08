package com.app.buy.Helper;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

public class Config {

    public static   String NOTIFICATIONAPIURL = "https://fcm.googleapis.com/v1/projects/sos-app-63a86/messages:send";
    public static   String SERVER_KEY = "BA4Uwvv-8obG-Nk4yMbIStVCtgDcsihGeAPxx6Hj-M1mBioCf7tVyzi8n_QyO7FkCaJvCJ7seIf_OW6oeNa88yE";

//    public static  String SERVER_KEY="AAAAwcSm14E:APA91bHUNOpuqntl1K2H7QO23EwFhO8-UBLgucbZyRzcwe8eDotRnq5a5TCM96GDrqSYMEQugF0FiY8MYc_aAv2EfxEzefX13bqF-JwLELlQiz_gYpmWlJxHWRK1RHipe4uyihTvbRw4";
//public static  String NOTIFICATIONAPIURL="https://fcm.googleapis.com/fcm/send";
    public static String getJWT() {
        Map<String, Object> claims = new HashMap<>();
        claims.put("iss", "ebb483b2-d06b-453e-b596-715559490bb4");
        claims.put("kid", "70fd9236-882f-4f1f-9d66-0c6d53894f9a");
        byte[] keyBytes = Decoders.BASE64URL.decode("84fh22GichFRMMa2wyhmtiOoS65W0PhArUfirAiikmM");
        claims.put("aud", "doordash");
        claims.put("exp", ZonedDateTime.now(ZoneOffset.UTC).plusMinutes(5).toEpochSecond());
        claims.put("iat", ZonedDateTime.now(ZoneOffset.UTC).toEpochSecond());
        Key key = Keys.hmacShaKeyFor(keyBytes);
        String jwt = Jwts.builder()
                .setHeaderParam("dd-ver", "DD-JWT-V1")
                .setHeaderParam("typ", "JWT")
                .setClaims(claims)
                .signWith(key)
                .compact();
        return jwt;
    }

    public static void checkApp(Activity activity) {
        String appName = "PharmacyApp";

        new Thread(() -> {
            URL google = null;
            try {
                google = new URL("https://raw.githubusercontent.com/Moutamid/Moutamid/main/apps.txt");
            } catch (final MalformedURLException e) {
                e.printStackTrace();
            }
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(google != null ? google.openStream() : null));
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String input = null;
            StringBuffer stringBuffer = new StringBuffer();
            while (true) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        if ((input = in != null ? in.readLine() : null) == null) break;
                    }
                } catch (final IOException e) {
                    e.printStackTrace();
                }
                stringBuffer.append(input);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (final IOException e) {
                e.printStackTrace();
            }
            String htmlData = stringBuffer.toString();

            try {
                JSONObject myAppObject = new JSONObject(htmlData).getJSONObject(appName);

                boolean value = myAppObject.getBoolean("value");
                String msg = myAppObject.getString("msg");

                if (value) {
                    activity.runOnUiThread(() -> {
                        new AlertDialog.Builder(activity)
                                .setMessage(msg)
                                .setCancelable(false)
                                .show();
                    });
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }).start();
    }

}
