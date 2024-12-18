package com.example.realestateapp.screens;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.realestateapp.R;
import com.example.realestateapp.adapters.PropertyAdapter;
import com.example.realestateapp.models.Property;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class DatabasePropertiesActivity extends AppCompatActivity {

    private static final String TAG = "AllPropertiesActivity";
    private RecyclerView recyclerViewProperties;
    private PropertyAdapter adapter;
    private List<Property> propertyList;
    private ProgressBar progressBar;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_properties);

        recyclerViewProperties = findViewById(R.id.recyclerViewProperties);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewProperties.setLayoutManager(new LinearLayoutManager(this));

        propertyList = new ArrayList<>();
        adapter = new PropertyAdapter(this, propertyList);
        recyclerViewProperties.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference().child("properties");

        fetchPropertiesFromDatabase();
    }

    private void fetchPropertiesFromDatabase() {
        // Show progress bar before fetching the data
        progressBar.setVisibility(View.VISIBLE);

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                propertyList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Property property = snapshot.getValue(Property.class);
                    if (property != null && property.getTitle() != null && !property.getTitle().isEmpty()) {
                        propertyList.add(property);
                    }
                }
                // Hide the progress bar after receiving the data
                progressBar.setVisibility(View.GONE);
                // Notify the adapter that data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Hide the progress bar in case of an error
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(DatabasePropertiesActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
