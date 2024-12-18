package com.example.realestateapp.models;

public class User {

    private String name;
    private String email;

    public User() {
        // Default constructor required for Firebase
    }

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public static class UploadResponse {
        public String status;
        public String message;
        public String imageUrl; // Adjust based on the actual API response
    }
}

