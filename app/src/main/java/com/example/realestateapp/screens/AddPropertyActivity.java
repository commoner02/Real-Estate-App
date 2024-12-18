package com.example.realestateapp.screens;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.realestateapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class AddPropertyActivity extends AppCompatActivity {

    private EditText propertyTitle, propertyLocation, propertyShortDescription, propertyFullDescription, propertyOwnerName, propertyContactNo, propertyPrice, propertyCategory, imageUrl;
    private Button buttonSubmit;
    private DatabaseReference dbRef;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        // Initialize views
        propertyTitle = findViewById(R.id.property_title);
        propertyLocation = findViewById(R.id.property_location);
        propertyShortDescription = findViewById(R.id.property_short_description);
        propertyFullDescription = findViewById(R.id.property_full_description);
        propertyOwnerName = findViewById(R.id.property_ownername);
        propertyContactNo = findViewById(R.id.property_contactno);
        propertyPrice = findViewById(R.id.property_price);
        propertyCategory = findViewById(R.id.property_category);
        imageUrl = findViewById(R.id.image_url);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        backButton = findViewById(R.id.back_button);

        // Initialize Firebase Realtime Database
        dbRef = FirebaseDatabase.getInstance().getReference().child("properties");

        // Set onClickListener for backButton
        backButton.setOnClickListener(v -> finish());

        // Set onClickListener for buttonSubmit
        buttonSubmit.setOnClickListener(v -> {
            // Get input values
            String title = propertyTitle.getText().toString().trim();
            String location = propertyLocation.getText().toString().trim();
            String shortDescription = propertyShortDescription.getText().toString().trim();
            String fullDescription = propertyFullDescription.getText().toString().trim();
            String ownerName = propertyOwnerName.getText().toString().trim();
            String contactNo = propertyContactNo.getText().toString().trim();
            String price = propertyPrice.getText().toString().trim();
            String category = propertyCategory.getText().toString().trim();
            String imageUrlString = imageUrl.getText().toString().trim();

            // Validate input
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(location) || TextUtils.isEmpty(shortDescription) || TextUtils.isEmpty(fullDescription) ||
                    TextUtils.isEmpty(shortDescription) || TextUtils.isEmpty(ownerName) || TextUtils.isEmpty(contactNo) || TextUtils.isEmpty(price) ||
                    TextUtils.isEmpty(category) || TextUtils.isEmpty(imageUrlString)) {
                Toast.makeText(AddPropertyActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Create a map to store property data
                Map<String, Object> propertyMap = new HashMap<>();
                propertyMap.put("title", title);
                propertyMap.put("location", location);
                propertyMap.put("shortDescription", shortDescription);
                propertyMap.put("fullDescription", fullDescription);
                propertyMap.put("shortdescription", shortDescription);
                propertyMap.put("ownerName", ownerName);
                propertyMap.put("contactNo", contactNo);
                propertyMap.put("price", price);
                propertyMap.put("category", category);
                propertyMap.put("imageUrl", imageUrlString);

                // Add property data to Firebase Realtime Database
                dbRef.push().setValue(propertyMap)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AddPropertyActivity.this, "Property added successfully", Toast.LENGTH_SHORT).show();
                            clearInputFields();
                        })
                        .addOnFailureListener(e -> Toast.makeText(AddPropertyActivity.this, "Failed to add property", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void clearInputFields() {
        propertyTitle.setText("");
        propertyLocation.setText("");
        propertyShortDescription.setText("");
        propertyFullDescription.setText("");
        propertyOwnerName.setText("");
        propertyContactNo.setText("");
        propertyPrice.setText("");
        propertyCategory.setText("");
        imageUrl.setText("");
    }
}
