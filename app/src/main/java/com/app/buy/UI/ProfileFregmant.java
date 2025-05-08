package com.app.buy.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.app.buy.R;
import com.app.buy.activities.authentication.LoginActivity;
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

public class ProfileFregmant extends Fragment {

    private ImageView UserImage;
    private TextView UserName, UserEmail, UserPhone, logout, order;
    private FirebaseAuth mAuth;
    private FirebaseUser CurrentUser;
    private String UserId;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private StorageReference storageReference;

    public ProfileFregmant() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.activity_user_profile, container, false);
        order =view.findViewById(R.id.order);
        UserImage =view.findViewById(R.id.profile_pic);
        UserName = view.findViewById(R.id.name);
        UserEmail =view.findViewById(R.id.email);
        UserPhone =view.findViewById(R.id.phone_number);
        logout =view.findViewById(R.id.logout);
        mAuth=FirebaseAuth.getInstance();
        CurrentUser =mAuth.getCurrentUser();
        UserId =CurrentUser.getUid();
        getUserProfileData();
        storageReference = FirebaseStorage.getInstance().getReference("profile_images");

        UserImage.setOnClickListener(v -> openGallery());
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder checkAlert = new AlertDialog.Builder(getContext());
                checkAlert.setMessage("Voulez-vous vous déconnecter ?")
                        .setCancelable(false)
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = checkAlert.create();
                alert.setTitle("Déconnexion");
                alert.show();


            }
        });
        getUserProfileData();
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getContext(), OrderActivity.class));
            }
        });
        return view;
    }
    private void getUserProfileData(){
        DatabaseReference root = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp");
        DatabaseReference m = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String Name = snapshot.child("name").getValue().toString();
                    String Image = snapshot.child("image").getValue().toString();
                    String Phone = snapshot.child("phone").getValue().toString();
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
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToFirebase();
        }
    }
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference fileRef = storageReference.child(userId + ".jpg");

            fileRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String downloadUrl = uri.toString();

                        // Save URL in Firebase Database
                        DatabaseReference dbRef = FirebaseDatabase.getInstance()
                                .getReference().child("PharmacyApp").child("users").child(userId);

                        dbRef.child("image").setValue(downloadUrl)
                                .addOnSuccessListener(aVoid -> {
                                    Picasso.get().load(downloadUrl).into(UserImage);
                                    Toast.makeText(getContext(), "Profile image updated", Toast.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> Toast.makeText(getContext(), "Database error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                    }))
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

}