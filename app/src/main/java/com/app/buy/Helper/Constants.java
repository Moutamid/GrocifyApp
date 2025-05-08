package com.app.buy.Helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.view.Window;

import androidx.appcompat.app.AlertDialog;

import com.app.buy.R;
import com.fxn.stash.Stash;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {

    static Dialog dialog;
    public static final String DATEFORMAT = "dd/MM/yyyy";
    public static final String MODEL_PAPERS = "MODEL_PAPERS";
    public static final String USER = "USER";
    public static final String PRICES = "PRICES";
    public static final String count = "count";

    public static final String HINDI = "HINDI";
    public static final String ARABIC = "ARABIC";
    public static final String Nouns = "Nouns";
    public static final String PDF = "PDF";
    public static final String AUDIO = "AUDIO";
    public static final String ROW = "ROW";
    public static final String CHART = "CHART";
    public static final String PRONOUN = "PRONOUN";
    public static final String DATIVE = "DATIVE";
    public static final String ACCUSATIVE = "ACCUSATIVE";
    public static final String LIST = "LIST";
    public static final String ID = "ID";
    public static final String TYPE = "type";
    public static final String STASH_USER = "STASH_USER";
    public static final String STASH_USER_PURCHASE_TYPE = "STASH_USER_PURCHASE_TYPE";
    public static final String TOPIC_ID = "TOPIC_ID";
    public static final String IS_VIP = "IS_VIP";
    public static final String VIP_SCHEDULE = "VIP_SCHEDULE";
    public static final String PAPER = "PAPER";
    public static final String PAYMENTS = "PAYMENTS";
    public static final String COURSE_PAYMENTS = "PAYMENT";
    public static final String VOICEOVERS = "VOICEOVERS";
    public static final String LETTER = "LETTER";
    public static final String EXERCISE_LIST = "EXERCISE_LIST";
    public static final String PAPER_1 = "PAPER_1";
    public static final String PAPER_2 = "PAPER_2";
    public static final String PAPER_3 = "PAPER_3";
    public static final String PAPER_4 = "PAPER_4";
    public static final String LEVEL = "LEVEL";
    public static final String exercise = "exercise";
    public static final String TOPIC = "TOPIC";
    public static final String SELECT = "SELECT";
    public static final String TOPICS = "TOPICS";
    public static final String URDU = "URDU";
    public static final String CONTENT = "CONTENT";
    public static final String EXERCISE = "EXERCISE";
    public static final String PASS = "PASS";
    public static final String Speaking = "Speaking";
    public static final String READING = "READING";
    public static final String VOCABULARY = "VOCABULARY";
    public static final String WRITING = "WRITING";
    public static final String SHOW_TOOLBAR = "SHOW_TOOLBAR";
    public static final String TRIAL_QUESTIONS = "TRIAL_QUESTIONS";
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAr/SDXnNuzAByJvH7404Tdjy5cHW+Ksa+UAQqyVv9yD1XEcAt1QkYZTEU5jUyV6pgvV5BixSNuptY+6rsotH0nhq42Dyh9HyGpybkRQUOLEexgTBEfMszKQ+jZZT2PhU5hmW3G8NLiJpZ3qCbZwYPNaTb7136TwGPe/IFL35DAo/oix+mvaGJBHdsoOW5RKEo/is6sCOyWI8htWWH7nGEfrOYLg3oRF5KCu8NIfCq6UQ52aBQ/gvaIng9oTLHM9uus0LO7IGLDNbNllZYnbJ2byFCjcGE2Cx+3aMz0vKAWD4CG3QLto/cYxoMF1M60EmSZ3eMIvMhXMfQ00U2TwcvywIDAQAB";
    public static final String VIP_6_MONTH = "vip.six.month.com.moutamid.sprache";
    public static final String VIP_3_MONTH = "vip.three.month.com.moutamid.sprache";
    public static final String VIP_YEAR = "vip.yearly.com.moutamid.sprache";
    // A1
public static final String A1_VIDEOS = "PharmacyApp/A1/videos";
public static final String A1_TRAIL_VIDEOS = "PharmacyApp/A1/trial_videos";
    public static final String TITLE = "title";
    public static final String VIDEO_URL = "video_url";
    public static final String VIDEO_KEY = "video_key";
    public static final String VIDEO_DATA = "videoData";

    public static String getFormattedDate(long date) {
        return new SimpleDateFormat(DATEFORMAT, Locale.getDefault()).format(date);
    }

    public static void initDialog(Context context) {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
    }

    public static void showDialog() {
        dialog.show();
    }

    public static void dismissDialog() {
        dialog.dismiss();
    }

    public static String getLanguage() {
        return Stash.getString(Constants.SELECT, Constants.URDU);
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

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("PharmacyApp");
        db.keepSynced(true);
        return db;
    }

    public static StorageReference storageReference(String auth) {
        StorageReference sr = FirebaseStorage.getInstance().getReference().child("PharmacyApp").child(auth);
        return sr;
    }
    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
        }
        return false;
    }
}
