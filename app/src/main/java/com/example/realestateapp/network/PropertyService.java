package com.example.realestateapp.network;

import com.example.realestateapp.models.PropertyResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface PropertyService {
    @GET("v1/records/a4ab8cc2-74b8-430e-baa0-5044a4364a77")
    Call<PropertyResponse> getProperties();
}
