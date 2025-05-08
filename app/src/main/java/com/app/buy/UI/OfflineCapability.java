package com.app.buy.UI;

import android.app.Application;
import com.google.firebase.database.FirebaseDatabase;

public class OfflineCapability extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //offline
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/");
        database.setPersistenceEnabled(true);
        /*Picasso*/
//        Picasso.Builder builder = new Picasso.Builder(this);
//        builder.downloader(new OkHttp3Downloader(this,Integer.MAX_VALUE));
//        Picasso built =builder.build();
//        built.setIndicatorsEnabled(true);
//        built.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(built);

    }

}
