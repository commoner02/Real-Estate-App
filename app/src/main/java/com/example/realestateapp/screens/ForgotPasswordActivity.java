package com.example.realestateapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.example.realestateapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String TAG = "ForgotPasswordActivity";

    private EditText usernameEditText;
    private EditText newPasswordEditText;
    private EditText confirmNewPasswordEditText;
    private AppCompatButton searchUsernameButton;
    private AppCompatButton updatePasswordButton;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dbRef = FirebaseDatabase.getInstance().getReference().child("users");
        mAuth = FirebaseAuth.getInstance();

        usernameEditText = findViewById(R.id.username);
        newPasswordEditText = findViewById(R.id.new_password);
        confirmNewPasswordEditText = findViewById(R.id.confirm_new_password);
        searchUsernameButton = findViewById(R.id.search_username_button);
        updatePasswordButton = findViewById(R.id.update_password_button);

        searchUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter username", Toast.LENGTH_SHORT).show();
                } else {
                    searchUsername(username);
                }
            }
        });

        updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = newPasswordEditText.getText().toString().trim();
                String confirmNewPassword = confirmNewPasswordEditText.getText().toString().trim();

                if (newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                } else if (!newPassword.equals(confirmNewPassword)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                } else {
                    String username = usernameEditText.getText().toString().trim();
                    updatePassword(username, newPassword);
                }
            }
        });
    }

    private void searchUsername(String username) {
        dbRef.orderByChild("name").equalTo(username)  // Ensure this field name matches your Firebase database
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d(TAG, "User found: " + dataSnapshot.getValue());
                            Toast.makeText(ForgotPasswordActivity.this, "Username found", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d(TAG, "User not found");
                            Toast.makeText(ForgotPasswordActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error querying database: " + databaseError.getMessage(), databaseError.toException());
                        Toast.makeText(ForgotPasswordActivity.this, "Error querying database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updatePassword(String username, String newPassword) {
        dbRef.orderByChild("name").equalTo(username)  // Ensure this field name matches your Firebase database
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Log.d(TAG, "DataSnapshot exists: " + dataSnapshot.getValue());  // Log the data snapshot
                            // Username found, get the associated email
                            DataSnapshot userSnapshot = dataSnapshot.getChildren().iterator().next();
                            String email = userSnapshot.child("email").getValue(String.class);
                            Log.d(TAG, "Updating user: " + userSnapshot.getKey() + ", Email: " + email);  // Log the user key and email being updated

                            // Update the password in the Realtime Database
                            userSnapshot.getRef().child("password").setValue(newPassword)
                                    .addOnSuccessListener(aVoid -> {
                                        Log.d(TAG, "Password updated successfully in Realtime Database for user: " + username);  // Log success message
                                        updateFirebaseAuthPassword(email, newPassword);
                                    })
                                    .addOnFailureListener(e -> {
                                        Log.e(TAG, "Error updating password in Realtime Database", e);
                                        Toast.makeText(ForgotPasswordActivity.this, "Error updating password in Realtime Database", Toast.LENGTH_SHORT).show();
                                    });
                        } else {
                            Log.d(TAG, "User not found");
                            Toast.makeText(ForgotPasswordActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e(TAG, "Error querying database: " + databaseError.getMessage(), databaseError.toException());
                        Toast.makeText(ForgotPasswordActivity.this, "Error querying database: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateFirebaseAuthPassword(String email, String newPassword) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null && user.getEmail().equals(email)) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Password updated successfully in Firebase Authentication for user: " + email);
                            Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.e(TAG, "Error updating password in Firebase Authentication", task.getException());
                            Toast.makeText(ForgotPasswordActivity.this, "Error updating password in Firebase Authentication", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // Reauthenticate the user if necessary
            reauthenticateAndChangePassword(email, newPassword);
        }
    }

    private void reauthenticateAndChangePassword(String email, String newPassword) {
        // Replace "currentPassword" with the user's current password.
        // For security reasons, you should securely get the user's current password.
        String currentPassword = "userCurrentPassword";

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            mAuth.signInWithEmailAndPassword(email, currentPassword)
                    .addOnCompleteListener(authTask -> {
                        if (authTask.isSuccessful()) {
                            FirebaseUser reauthenticatedUser = mAuth.getCurrentUser();
                            if (reauthenticatedUser != null) {
                                reauthenticatedUser.updatePassword(newPassword)
                                        .addOnCompleteListener(task -> {
                                            if (task.isSuccessful()) {
                                                Log.d(TAG, "Password updated successfully in Firebase Authentication for user: " + email);
                                                Toast.makeText(ForgotPasswordActivity.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            } else {
                                                Log.e(TAG, "Error updating password in Firebase Authentication", task.getException());
                                                Toast.makeText(ForgotPasswordActivity.this, "Error updating password in Firebase Authentication", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.e(TAG, "Reauthentication failed", authTask.getException());
                            Toast.makeText(ForgotPasswordActivity.this, "Reauthentication failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
