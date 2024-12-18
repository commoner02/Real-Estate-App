package com.example.realestateapp.models;

import java.io.Serializable;

public class Property implements Serializable {
    private String price;
    private String category;
    private String imageuri;
    private String location;
    private String contactno;
    private String ownername;
    private String shortdescription;
    private String title;
    private boolean isFavorite;
    private String fulldescription;


    public String getFulldescription() {
        return fulldescription;
    }

    public void setFulldescription(String fulldescription) {
        this.fulldescription = fulldescription;
    }



    public Property() {
        // Default constructor required for calls to DataSnapshot.getValue(Property.class)
    }

    // Getters and setters
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageuri() { return imageuri; }
    public void setImageuri(String imageuri) { this.imageuri = imageuri; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getContactno() { return contactno; }
    public void setContactno(String contactno) { this.contactno = contactno; }
    public String getOwnername() { return ownername; }
    public void setOwnername(String ownername) { this.ownername = ownername; }
    public String getShortdescription() { return shortdescription; }
    public void setShortdescription(String shortdescription) { this.shortdescription = shortdescription; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
