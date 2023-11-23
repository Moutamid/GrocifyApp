package com.example.groceryshoppingsystem.Helper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface DoorDashService {
    @POST("drive/v2/deliveries")
    Call<Void> createDelivery(@Header("Authorization") String jwt, @Body CreateDeliveryRequest request);
}
