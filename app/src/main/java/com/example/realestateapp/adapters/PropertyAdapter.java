package com.example.realestateapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.realestateapp.R;
import com.example.realestateapp.models.Property;
import com.example.realestateapp.screens.PropertyDetailsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.List;

public class PropertyAdapter extends RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder> {

    private List<Property> propertyList;
    private Context context;

    public PropertyAdapter(Context context, List<Property> propertyList) {
        this.context = context;
        this.propertyList = propertyList;
    }

    @NonNull
    @Override
    public PropertyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.property_item1, parent, false);
        return new PropertyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PropertyViewHolder holder, int position) {
        Property property = propertyList.get(position);

        holder.propertyTitle.setText(property.getTitle());
        holder.propertyPrice.setText("Price: " + property.getPrice());
        holder.propertyLocation.setText("Location: " + property.getLocation());
        holder.propertyDescription.setText("Description: " + property.getFulldescription());

        Glide.with(context)
                .load(property.getImageuri())
                .placeholder(R.drawable.bg_home) // Set your placeholder image here
                .error(R.drawable.error) // Set your error image here
                .into(holder.propertyImage);

        // Set OnClickListener for propertyImage to navigate to PropertyDetailsActivity
        holder.propertyImage.setOnClickListener(v -> {
            Intent intent = new Intent(context, PropertyDetailsActivity.class);
            intent.putExtra("property", property);
            context.startActivity(intent);
        });

        holder.favoriteTextView.setText(property.isFavorite() ? "Unfavourite" : "Favourite");

        holder.favoriteTextView.setOnClickListener(v -> {
            boolean isFavorite = !property.isFavorite();
            property.setFavorite(isFavorite);
            holder.favoriteTextView.setText(isFavorite ? "Unfavourite" : "Favourite");

            // Update favorite status in Firebase
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("properties").child(property.getTitle());
            dbRef.child("isFavorite").setValue(isFavorite);

            // Show toast message
            Toast.makeText(context, isFavorite ? "Added to Favourites" : "Removed from Favourites", Toast.LENGTH_SHORT).show();

            // Add or remove property from FavouriteFragment based on favorite status
            if (isFavorite) {
                addToFavorites(property);
            } else {
                removeFromFavorites(property);
            }
        });
    }

    @Override
    public int getItemCount() {
        return propertyList.size();
    }

    public static class PropertyViewHolder extends RecyclerView.ViewHolder {
        TextView propertyTitle, propertyPrice, propertyLocation, propertyDescription, favoriteTextView, propertyCategory;
        ImageView propertyImage;

        public PropertyViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyTitle = itemView.findViewById(R.id.propertyTitle);
            propertyPrice = itemView.findViewById(R.id.propertyPrice);
            propertyLocation = itemView.findViewById(R.id.propertyLocation);
            propertyCategory = itemView.findViewById(R.id.propertyCategory);
            propertyDescription = itemView.findViewById(R.id.propertyDescription);
            favoriteTextView = itemView.findViewById(R.id.favoriteTextView);
            propertyImage = itemView.findViewById(R.id.propertyImage);
        }
    }

    private void addToFavorites(Property property) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favourites").child(property.getTitle());
        favoritesRef.setValue(property)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Property added to favourites", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to add property to favourites", Toast.LENGTH_SHORT).show();
                });
    }

    private void removeFromFavorites(Property property) {
        DatabaseReference favoritesRef = FirebaseDatabase.getInstance().getReference().child("favourites").child(property.getTitle());
        favoritesRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Property removed from favourites", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to remove property from favourites", Toast.LENGTH_SHORT).show();
                });
    }
}
