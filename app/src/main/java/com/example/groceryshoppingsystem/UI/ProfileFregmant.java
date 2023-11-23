package com.example.groceryshoppingsystem.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.groceryshoppingsystem.Adapters.OrderAdapter;
import com.example.groceryshoppingsystem.Model.MyorderModel;
import com.example.groceryshoppingsystem.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileFregmant extends Fragment {

    //----------------------------
    private ImageView UserImage;
    private TextView UserName, UserEmail, UserPhone, logout;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    public ProfileFregmant() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_user_profile, container, false);
        UserImage =view.findViewById(R.id.profile_pic);
        UserName = view.findViewById(R.id.name);
        UserEmail =view.findViewById(R.id.email);
        UserPhone =view.findViewById(R.id.phone_number);
        logout =view.findViewById(R.id.logout);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser =mAuth.getCurrentUser();
        UserId =CurrentUser.getUid();
        getUserProfileData();
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder checkAlert = new AlertDialog.Builder(getContext());
                checkAlert.setMessage("Do you want to Logout?")
                        .setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), loginActivity.class);
                                startActivity(intent);
                               getActivity().finish();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = checkAlert.create();
                alert.setTitle("LogOut");
                alert.show();

            }
        });
        getUserProfileData();
        return view;
    }
    private void getUserProfileData(){
        DatabaseReference root = FirebaseDatabase.getInstance("https://grocery-delivery-app-22f4e-default-rtdb.firebaseio.com/").getReference().child("GrocaryApp");
        DatabaseReference m = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String Name = snapshot.child("Name").getValue().toString();
                    String Image = snapshot.child("Image").getValue().toString();
                    String Phone = snapshot.child("Phone").getValue().toString();
                    UserName.setText(Name);
                    UserPhone.setText(Phone);
                    UserEmail.setText(CurrentUser.getEmail().toString());

                    if (Image.equals("default")) {
                        Picasso.get().load(R.drawable.defualt_image).into(UserImage);
                    } else
                        Picasso.get().load(Image).placeholder(R.drawable.defualt_image).into(UserImage);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }

}