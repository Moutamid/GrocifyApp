package com.example.groceryshoppingsystem.Helper;

import com.google.gson.annotations.SerializedName;

public class CreateDeliveryRequest {
    @SerializedName("external_delivery_id")
    public String externalDeliveryId;

    @SerializedName("pickup_address")
    public String pickupAddress;

    @SerializedName("pickup_business_name")
    public String pickupBusinessName;

    @SerializedName("pickup_phone_number")
    public String pickupPhoneNumber;

    @SerializedName("pickup_instructions")
    public String pickupInstructions;

    @SerializedName("pickup_reference_tag")
    public String pickupReferenceTag;

    @SerializedName("dropoff_address")
    public String dropoffAddress;

    @SerializedName("dropoff_business_name")
    public String dropoffBusinessName;

    @SerializedName("dropoff_phone_number")
    public String dropoffPhoneNumber;

    @SerializedName("dropoff_instructions")
    public String dropoffInstructions;

    @SerializedName("dropoff_contact_given_name")
    public String dropoffContactGivenName;

    @SerializedName("dropoff_contact_family_name")
    public String dropoffContactFamilyName;

    @SerializedName("dropoff_contact_send_notifications")
    public boolean dropoffContactSendNotifications;

    @SerializedName("scheduling_model")
    public String schedulingModel;

    @SerializedName("order_value")
    public int orderValue;

    @SerializedName("tip")
    public int tip;

    @SerializedName("currency")
    public String currency;

    @SerializedName("contactless_dropoff")
    public boolean contactlessDropoff;

    @SerializedName("action_if_undeliverable")
    public String actionIfUndeliverable;

    // Constructor

    public CreateDeliveryRequest() {
    }
}