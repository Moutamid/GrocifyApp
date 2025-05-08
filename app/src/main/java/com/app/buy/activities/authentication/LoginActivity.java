package com.app.buy.activities.authentication;

import static android.view.View.GONE;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.app.buy.AdminPanel.AdminActivity;
import com.app.buy.Helper.Constants;
import com.app.buy.Helper.UserModel;
import com.app.buy.R;
import com.app.buy.UI.MainActivity;
import com.fxn.stash.Stash;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText mPassword;
    EditText mEmail;
    Button mlogin;
    TextView mforgerpassword, tvLogin;
    TextView mCreateBtn;
    FirebaseAuth fauth;
    ProgressBar mprogresspar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = (EditText) findViewById(R.id.EmailLogin);
        mPassword = findViewById(R.id.PasswordLogin);
        fauth = FirebaseAuth.getInstance();
        tvLogin = findViewById(R.id.tvLogin);
        mlogin = (Button) findViewById(R.id.Login);
        mCreateBtn = (TextView) findViewById(R.id.SignUPtext);
        mprogresspar = (ProgressBar) findViewById(R.id.progressBar1);
        mforgerpassword = (TextView) findViewById(R.id.ForgetPassword);
        // Checking if the user is logging in or log out ! ;
        ImageView togglePassword = findViewById(R.id.ivTogglePassword);
        togglePassword.setOnClickListener(new View.OnClickListener() {
            boolean isPasswordVisible = false;

            @Override
            public void onClick(View v) {
                if (isPasswordVisible) {
                    // Hide password
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    togglePassword.setImageResource(R.drawable.blind); // icon for hidden
                } else {
                    // Show password
                    mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    togglePassword.setImageResource(R.drawable.visible); // icon for visible
                }
                // Move cursor to the end
                mPassword.setSelection(mPassword.length());
                isPasswordVisible = !isPasswordVisible;
            }
        });

        mlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email = mEmail.getText().toString().trim();
                final String Password = mPassword.getText().toString();
                if (TextUtils.isEmpty(Email)) {
                    mEmail.setError("Email is required");
                    return;
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.setError("Password is required");
                    return;
                }
                if (Password.length() < 6) {
                    mPassword.setError("Password must be bigger than or equal 6 characters");
                    return;
                }
                // progress in background and i make it here visible.
                mprogresspar.setVisibility(View.VISIBLE);

                // Authenticate
                fauth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            getUserProfileData();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong User name Or Password", Toast.LENGTH_SHORT).show();
                            mprogresspar.setVisibility(GONE);
                        }
                    }
                });

            }
        });
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, UserRegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });

        mforgerpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Here we will send a verification message
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
            }
        });

    }


    private void getUserProfileData() {

        DatabaseReference root = FirebaseDatabase.getInstance("https://sos-app-63a86-default-rtdb.firebaseio.com/").getReference().child("PharmacyApp");
        DatabaseReference m = root.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mprogresspar.setVisibility(GONE);
                if (snapshot.exists()) {
                    UserModel userModel=snapshot.getValue(UserModel.class);
                    Stash.put(Constants.STASH_USER, userModel);
//                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else {

//                    Toast.makeText(LoginActivity.this, "Logged in Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                    finish();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        };
        m.addListenerForSingleValueEvent(valueEventListener);
    }
    public void login(View view) {
        startActivity(new Intent(LoginActivity.this, UserRegisterActivity.class));
    }

    public void pharmacy(View view) {
        startActivity(new Intent(LoginActivity.this, PharmacyRegisterActivity.class));
    }

}