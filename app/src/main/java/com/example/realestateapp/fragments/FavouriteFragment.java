package com.example.realestateapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class FavouriteFragment extends Fragment {

    private RecyclerView recyclerViewFavorites;
    private PropertyAdapter adapter;
    private List<Property> favoriteList;
    private DatabaseReference dbRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites);
        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        favoriteList = new ArrayList<>();
        adapter = new PropertyAdapter(getContext(), favoriteList);
        recyclerViewFavorites.setAdapter(adapter);

        dbRef = FirebaseDatabase.getInstance().getReference().child("favourites");

        fetchFavorites();

        return view;
    }

    private void fetchFavorites() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favoriteList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Property property = snapshot.getValue(Property.class);
                    if (property != null) {
                        favoriteList.add(property);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }
}
