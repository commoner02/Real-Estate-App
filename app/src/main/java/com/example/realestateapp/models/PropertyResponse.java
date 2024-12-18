package com.example.realestateapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PropertyResponse {

    @SerializedName("properties")
    private List<Property> properties;

    public List<Property> getProperties() {
        return properties;
    }

    public void setProperties(List<Property> properties) {
        this.properties = properties;
    }

    public static class Property {
        @SerializedName("location")
        private String location;

        @SerializedName("type")
        private String type;

        @SerializedName("description")
        private String description;

        @SerializedName("shortdescription")
        private String shortDescription;

        @SerializedName("ownername")
        private String ownerName;

        @SerializedName("contactno")
        private String contactNo;

        @SerializedName("price")
        private String price;

        @SerializedName("category")
        private String category;

        @SerializedName("imageuri")
        private String imageUri;

        // Getters and Setters
        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getShortDescription() {
            return shortDescription;
        }

        public void setShortDescription(String shortDescription) {
            this.shortDescription = shortDescription;
        }

        public String getOwnerName() {
            return ownerName;
        }

        public void setOwnerName(String ownerName) {
            this.ownerName = ownerName;
        }

        public String getContactNo() {
            return contactNo;
        }

        public void setContactNo(String contactNo) {
            this.contactNo = contactNo;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getImageUri() {
            return imageUri;
        }

        public void setImageUri(String imageUri) {
            this.imageUri = imageUri;
        }

    }
}
