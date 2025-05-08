package com.app.buy.UI;

import static com.app.buy.Helper.Config.checkApp;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.app.buy.R;
import com.app.buy.activities.SubscriptionActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {
    //    private Toolbar mToolBar;
//    private DrawerLayout drawerLayout;
//    private ActionBarDrawerToggle mtoggle;

    final int PERMISSION_REQUEST_CODE = 112;

    private static final String BASE_URL = "https://openapi.doordash.com/";
    private FirebaseAuth mAuth;
    private String Uid, name, photo;
    private FirebaseUser CurrentUser;
     private RelativeLayout CustomCartContainer;
    private TextView PageTitle;
    private TextView CustomCartNumber;
    SmoothBottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT > 32) {
            if (!shouldShowRequestPermissionRationale("112")) {
                getNotificationPermission();
            }
        }
        bottomBar = findViewById(R.id.bottomBar);
        mAuth = FirebaseAuth.getInstance();
        CurrentUser = mAuth.getCurrentUser();
        Uid = CurrentUser.getUid();
        getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new HomeFregmant()).commit();
//        System.setProperty("javax.net.debug", "all");
        getUserProfileData();
        checkApp(MainActivity.this);
        bottomBar.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public boolean onItemSelect(int i) {
                if (i == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new HomeFregmant()).commit();
                }

                else if (i == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new PharmacyFragment()).commit();
                }  else if (i == 2) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.Orderframe, new ProfileFregmant()).commit();

                }
                return false;
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onStop() {
        super.onStop();
    }



    public void getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        PERMISSION_REQUEST_CODE);
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // allow

                } else {
                    Toast.makeText(this, "Permisssion denied", Toast.LENGTH_SHORT).show();
                }
                return;

        }

    }

    private void getUserProfileData() {


        DatabaseReference root = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp");
        DatabaseReference m = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
       m.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               if (snapshot.hasChild("vip")) {
                   Log.d("dtaa", snapshot+"------");
                   if (snapshot.child("vip").getValue().equals(false)) {
                       startActivity(new Intent(MainActivity.this, SubscriptionActivity.class));
                       finish();
                   }
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {
               Toast.makeText(MainActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
               finish();

           }
       });

    }
}
