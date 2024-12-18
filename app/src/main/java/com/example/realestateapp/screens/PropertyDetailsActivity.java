package com.example.realestateapp.screens;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.realestateapp.R;
import com.example.realestateapp.models.Property;

public class PropertyDetailsActivity extends AppCompatActivity {

    private TextView propertyTitle, propertyPrice, propertyLocation, propertyDescription, propertyOwner, propertyContact, propertyCategory;
    private ImageView propertyImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        propertyTitle = findViewById(R.id.propertyTitle);
        propertyPrice = findViewById(R.id.propertyPrice);
        propertyLocation = findViewById(R.id.propertyLocation);
        propertyDescription = findViewById(R.id.propertyDescription);
        propertyOwner = findViewById(R.id.propertyOwner);
        propertyContact = findViewById(R.id.propertyContact);
        propertyImage = findViewById(R.id.propertyImage);
        propertyCategory = findViewById(R.id.propertyCategory);

        Property property = (Property) getIntent().getSerializableExtra("property");

        if (property != null) {
            propertyTitle.setText(property.getTitle());
            propertyPrice.setText("Price: " + property.getPrice());
            propertyLocation.setText("Location: " + property.getLocation());
            propertyDescription.setText("Description: " + property.getFulldescription());
            propertyOwner.setText("Owner: " + property.getOwnername());
            propertyCategory.setText("Category: " + property.getCategory());
            propertyContact.setText("Contact: " + property.getContactno());

            Glide.with(this)
                    .load(property.getImageuri())
                    .placeholder(R.drawable.bg_home) // Set your placeholder image here
                    .error(R.drawable.error) // Set your error image here
                    .into(propertyImage);
        }
    }
}
