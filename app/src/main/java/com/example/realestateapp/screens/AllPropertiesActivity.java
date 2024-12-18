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
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.realestateapp.R;
import com.example.realestateapp.adapters.PropertyAdapter;
import com.example.realestateapp.models.Property;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AllPropertiesActivity extends AppCompatActivity {

    private static final String TAG = "AllPropertiesActivity";
    private RecyclerView recyclerViewProperties;
    private PropertyAdapter adapter;
    private List<Property> propertyList;
    private RequestQueue requestQueue;
    private ProgressBar progressBar;
    private DatabaseReference dbRef;
    private Set<String> fetchedPropertyTitles = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_properties);

        recyclerViewProperties = findViewById(R.id.recyclerViewProperties);
        progressBar = findViewById(R.id.progressBar);
        recyclerViewProperties.setLayoutManager(new LinearLayoutManager(this));

        propertyList = new ArrayList<>();
        adapter = new PropertyAdapter(this, propertyList);
        recyclerViewProperties.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);
        dbRef = FirebaseDatabase.getInstance().getReference().child("properties");

        // Fetch properties from the web every time the activity starts
        fetchPropertiesFromWeb();
    }

    private void fetchPropertiesFromWeb() {
        String url = "https://api.myjson.online/v1/records/a4ab8cc2-74b8-430e-baa0-5044a4364a77";

        // Show progress bar before making the request
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject jsonObj = response.getJSONObject("data");
                            JSONArray jsonArray = jsonObj.getJSONArray("properites");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject propertyJson = jsonArray.getJSONObject(i);
                                Property property = new Property();

                                // Assigning values to Property object
                                property.setTitle(propertyJson.getString("title"));
                                property.setPrice(propertyJson.getString("price"));
                                property.setCategory(propertyJson.getString("category"));
                                property.setImageuri(propertyJson.getString("imageuri"));
                                property.setLocation(propertyJson.getString("location"));
                                property.setShortdescription(propertyJson.getString("shortdescription"));
                                property.setFulldescription(propertyJson.getString("fulldescription"));
                                property.setOwnername(propertyJson.getString("ownername"));
                                property.setContactno(propertyJson.getString("contactno"));

                                // Check if the property has already been fetched
                                checkAndAddProperty(property);
                            }

                            // Hide the progress bar after receiving the response
                            progressBar.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            // Hide the progress bar in case of an error
                            progressBar.setVisibility(View.GONE);
                            Log.e(TAG, "JSON parsing error: " + e.getMessage());
                            Toast.makeText(AllPropertiesActivity.this, "JSON parsing error.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Hide the progress bar in case of an error
                progressBar.setVisibility(View.GONE);
                Log.e(TAG, "Volley error: " + error.getMessage());
                Toast.makeText(AllPropertiesActivity.this, "Failed to fetch data.", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private void checkAndAddProperty(Property property) {
        dbRef.orderByChild("title").equalTo(property.getTitle()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    // Property does not exist, add to database
                    addPropertyToDatabase(property);
                } else {
                    // Property already exists, no need to add to database
                    Log.d(TAG, "Property already exists: " + property.getTitle());
                }
                // Adding the property to the list regardless of its existence in the database
                propertyList.add(property);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(TAG, "Database error: " + databaseError.getMessage());
                Toast.makeText(AllPropertiesActivity.this, "Failed to fetch data from database.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addPropertyToDatabase(Property property) {
        DatabaseReference newRef = dbRef.push();
        newRef.setValue(property)
                .addOnSuccessListener(aVoid -> Log.d(TAG, "Property added with ID: " + newRef.getKey()))
                .addOnFailureListener(e -> Log.w(TAG, "Error adding property", e));
    }
}
