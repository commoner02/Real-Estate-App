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
import com.example.realestateapp.models.FavouriteProperties;
import com.example.realestateapp.screens.PropertyDetailsActivity;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.List;

public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {
    private Context context;
    private List<FavouriteProperties> favouriteList;
    private FirebaseFirestore firestore;

    public FavouritesAdapter(Context context, List<FavouriteProperties> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
        firestore = FirebaseFirestore.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.property_item2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavouriteProperties favoriteProperty = favouriteList.get(position);

        // Bind data to ViewHolder views
        holder.locationTextView.setText(favoriteProperty.getLocation());
        holder.propertyTitleTextView.setText(favoriteProperty.getShortDescription());
        holder.priceTextView.setText(favoriteProperty.getPrice());
        holder.rent_sellTextView.setText(favoriteProperty.getType());

        // Load image using Glide
        Glide.with(context).load(favoriteProperty.getFavImageUrl()).into(holder.propertyImageView);

        // Set onClickListener for propertyImageView
        holder.propertyImageView.setOnClickListener(v -> {
            // Navigate to property detail page with property details
            Intent intent = new Intent(context, PropertyDetailsActivity.class);
            intent.putExtra("location", favoriteProperty.getLocation());
            intent.putExtra("price", favoriteProperty.getPrice());
            intent.putExtra("shortdescription", favoriteProperty.getShortDescription());
            intent.putExtra("imageuri", favoriteProperty.getFavImageUrl());
            intent.putExtra("contactno", favoriteProperty.getContactno());
            intent.putExtra("description", favoriteProperty.getDescription());
            intent.putExtra("type", favoriteProperty.getType());
            intent.putExtra("ownername", favoriteProperty.getOwnername());
            context.startActivity(intent);
        });

        holder.RemoveFavourite.setOnClickListener(v -> {
            // Query the "Favorites" collection based on the contactno field
            firestore.collection("Favorites")
                    .whereEqualTo("contactno", favoriteProperty.getContactno())
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            DocumentReference docRef = documentSnapshot.getReference();
                            docRef.delete()
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show());
                        }
                    })
                    .addOnFailureListener(e ->
                            Toast.makeText(context, "Failed to remove from favorites", Toast.LENGTH_SHORT).show());
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView propertyImageView;
        private TextView RemoveFavourite, propertyTitleTextView, locationTextView, priceTextView, rent_sellTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            propertyTitleTextView = itemView.findViewById(R.id.propertyTitleTextView);
            locationTextView = itemView.findViewById(R.id.locationTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            rent_sellTextView = itemView.findViewById(R.id.rent_sellTextView);
            propertyImageView = itemView.findViewById(R.id.propertyImageView);
            RemoveFavourite = itemView.findViewById(R.id.remove_fav_button);
        }
    }
}
