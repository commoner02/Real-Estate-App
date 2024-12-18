package com.example.realestateapp.screens;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import com.example.realestateapp.R;
import com.example.realestateapp.fragments.AccountFragment;
import com.example.realestateapp.fragments.FavouriteFragment;
import com.example.realestateapp.fragments.HomeFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {
    private BottomNavigationView bottomNavigationView;
    private String userId;
    private String userEmail;
    private String userName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(this);

        // Retrieve user info from Intent
        userId = getIntent().getStringExtra("USER_ID");
        userEmail = getIntent().getStringExtra("USER_EMAIL");
        userName = getIntent().getStringExtra("USER_NAME");

        // Pass user info to the HomeFragment
        HomeFragment homeFragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", userId);
        bundle.putString("USER_EMAIL", userEmail);
        bundle.putString("USER_NAME", userName);
        homeFragment.setArguments(bundle);

        loadFragment(homeFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        bundle.putString("USER_ID", userId);
        bundle.putString("USER_EMAIL", userEmail);
        bundle.putString("USER_NAME", userName);

        int id = item.getItemId();
        if (id == R.id.menu_home) {
            fragment = new HomeFragment();
            fragment.setArguments(bundle);
        } else if (id == R.id.menu_favourite) {
            fragment = new FavouriteFragment();
            fragment.setArguments(bundle);
        } else if (id == R.id.menu_account) {
            fragment = new AccountFragment();
            fragment.setArguments(bundle);
        }
        return loadFragment(fragment);
    }

    boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
