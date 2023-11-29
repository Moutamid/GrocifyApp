package com.example.groceryshoppingsystem.Helper;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface

DoorDashService {
    @POST("drive/v2/deliveries")
    Call<CreateDeliveryRequest> createDelivery(@Header("Authorization") String jwt, @Body CreateDeliveryRequest request);

    @GET("drive/v2/deliveries/{external_delivery_id}")
    Call<String> getDelivery(
            @Header("Authorization") String authorization,
            @Path("external_delivery_id") String externalDeliveryId
    );
}
