package com.example.groceryshoppingsystem.UI;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.groceryshoppingsystem.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class FeedbackActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String uId;
    RatingBar ratingbar;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        mAuth = FirebaseAuth.getInstance();
        ratingbar = (RatingBar) findViewById(R.id.ratingBar);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingbar.getRating());
                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("rating", rating);
                FirebaseUser currentUser = mAuth.getCurrentUser();
                uId = currentUser.getUid();
                DatabaseReference z = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp").child("Rating");
                z.child(uId).push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(FeedbackActivity.this, "Successfully Submitted", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });

    }
}