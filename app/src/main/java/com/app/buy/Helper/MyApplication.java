package com.app.buy.Helper;

import android.app.Application;

import com.fxn.stash.Stash;
import com.google.firebase.database.FirebaseDatabase;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Stash.init(this);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/");
        database.setPersistenceEnabled(true);

    }
}
