package com.example.realestateapp.screens;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.realestateapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private TextView haveAccount;
    private EditText userName, userEmail, userPassword, confirmPassword;
    private AppCompatButton signupButton;

    // Firebase instances
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private DatabaseReference mDatabase;

    String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        haveAccount = findViewById(R.id.have_account);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        userPassword = findViewById(R.id.user_password);
        confirmPassword = findViewById(R.id.user_confirm_password);
        signupButton = findViewById(R.id.sign_up_button);

        // Initialize Firebase instances
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        haveAccount.setOnClickListener(v -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            finish();
        });

        signupButton.setOnClickListener(v -> {
            if (userName.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
            } else if (userEmail.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
            } else if (userPassword.getText().toString().trim().isEmpty()) {
                Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
            } else if (!userPassword.getText().toString().trim().equals(confirmPassword.getText().toString().trim())) {
                Toast.makeText(SignUpActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
            } else {
                if (emailChecker(userEmail.getText().toString().trim())) {
                    createUser(userEmail.getText().toString().trim(),
                            userPassword.getText().toString().trim(),
                            userName.getText().toString().trim());
                } else {
                    Toast.makeText(SignUpActivity.this, "Enter valid email", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    boolean emailChecker(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void createUser(String email, String password, String name) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                userId = user.getUid();
                                saveUserDataToFirestore(userId, name, email);
                                saveUserProfileToDatabase(userId, name, email);
                            }
                            Toast.makeText(SignUpActivity.this, "Sign up successful.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "Sign up failed.", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                        finish();
                    }
                });
    }

    private void saveUserDataToFirestore(String userId, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);

        mFirestore.collection("users").document(userId).set(userData)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User data successfully stored in Firestore");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing user data to Firestore", e);
                    }
                });
    }

    private void saveUserProfileToDatabase(String userId, String name, String email) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);

        mDatabase.child("users").child(userId).setValue(userData)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "User data successfully stored in Realtime Database"))
                .addOnFailureListener(e -> Log.w(TAG, "Error writing user data to Realtime Database", e));
    }
}
