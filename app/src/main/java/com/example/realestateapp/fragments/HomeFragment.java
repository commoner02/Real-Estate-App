package com.example.realestateapp.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.realestateapp.R;
import com.example.realestateapp.screens.AllPropertiesActivity;
import com.example.realestateapp.screens.DatabasePropertiesActivity;

public class HomeFragment extends Fragment {

    private TextView showAllTextView, onlinePropertyTextView;
    private ProgressBar progressBar;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        showAllTextView = view.findViewById(R.id.show_all_txtview);
        onlinePropertyTextView = view.findViewById(R.id.online_property_txtview);
        progressBar = view.findViewById(R.id.progressBar);

        onlinePropertyTextView.setOnClickListener(v -> {
            showLoading(true);
            // Simulate loading delay
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), AllPropertiesActivity.class);
                startActivity(intent);
                showLoading(false);
            }, 2000); // 2 seconds delay for demonstration
        });

        showAllTextView.setOnClickListener(v -> {
            showLoading(true);
            // Simulate loading delay
            new Handler().postDelayed(() -> {
                Intent intent = new Intent(getActivity(), DatabasePropertiesActivity.class);
                startActivity(intent);
                showLoading(false);
            }, 2000); // 2 seconds delay for demonstration
        });

        return view;
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            showAllTextView.setEnabled(false);
            onlinePropertyTextView.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            showAllTextView.setEnabled(true);
            onlinePropertyTextView.setEnabled(true);
        }
    }
}
