package com.example.groceryshoppingsystem.Helper;
import com.google.gson.annotations.SerializedName;

public class CreateDeliveryRequest {
    @SerializedName("external_delivery_id")
    private String externalDeliveryId;

    @SerializedName("pickup_address")
    private String pickupAddress;

    @SerializedName("pickup_business_name")
    private String pickupBusinessName;

    @SerializedName("pickup_phone_number")
    private String pickupPhoneNumber;

    @SerializedName("pickup_instructions")
    private String pickupInstructions;

    @SerializedName("dropoff_address")
    private String dropoffAddress;

    @SerializedName("dropoff_business_name")
    private String dropoffBusinessName;

    @SerializedName("dropoff_phone_number")
    private String dropoffPhoneNumber;

    @SerializedName("dropoff_instructions")
    private String dropoffInstructions;

    @SerializedName("dropoff_contact_given_name")
    private String dropoffContactGivenName;

    @SerializedName("dropoff_contact_send_notifications")
    private boolean dropoffContactSendNotifications;

    @SerializedName("scheduling_model")
    private String schedulingModel;

    @SerializedName("order_value")
    private int orderValue;

    @SerializedName("tip")
    private int tip;

    @SerializedName("currency")
    private String currency;

    @SerializedName("contactless_dropoff")
    private boolean contactlessDropoff;

    @SerializedName("action_if_undeliverable")
    private String actionIfUndeliverable;

    // Add other fields as needed

    // Constructor

    public CreateDeliveryRequest(String externalDeliveryId, String pickupAddress, String pickupBusinessName,
                                 String pickupPhoneNumber, String pickupInstructions, String dropoffAddress,
                                 String dropoffBusinessName, String dropoffPhoneNumber, String dropoffInstructions,
                                 String dropoffContactGivenName, boolean dropoffContactSendNotifications,
                                 String schedulingModel, int orderValue, int tip, String currency,
                                 boolean contactlessDropoff, String actionIfUndeliverable) {
        this.externalDeliveryId = externalDeliveryId;
        this.pickupAddress = pickupAddress;
        this.pickupBusinessName = pickupBusinessName;
        this.pickupPhoneNumber = pickupPhoneNumber;
        this.pickupInstructions = pickupInstructions;
        this.dropoffAddress = dropoffAddress;
        this.dropoffBusinessName = dropoffBusinessName;
        this.dropoffPhoneNumber = dropoffPhoneNumber;
        this.dropoffInstructions = dropoffInstructions;
        this.dropoffContactGivenName = dropoffContactGivenName;
        this.dropoffContactSendNotifications = dropoffContactSendNotifications;
        this.schedulingModel = schedulingModel;
        this.orderValue = orderValue;
        this.tip = tip;
        this.currency = currency;
        this.contactlessDropoff = contactlessDropoff;
        this.actionIfUndeliverable = actionIfUndeliverable;
        // Initialize other fields as needed
    }

    // Add getters for all fields

    public String getExternalDeliveryId() {
        return externalDeliveryId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public String getPickupBusinessName() {
        return pickupBusinessName;
    }

    public String getPickupPhoneNumber() {
        return pickupPhoneNumber;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public String getDropoffBusinessName() {
        return dropoffBusinessName;
    }

    public String getDropoffPhoneNumber() {
        return dropoffPhoneNumber;
    }

    public String getDropoffInstructions() {
        return dropoffInstructions;
    }

    public String getDropoffContactGivenName() {
        return dropoffContactGivenName;
    }

    public boolean isDropoffContactSendNotifications() {
        return dropoffContactSendNotifications;
    }

    public String getSchedulingModel() {
        return schedulingModel;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public int getTip() {
        return tip;
    }

    public String getCurrency() {
        return currency;
    }

    public boolean isContactlessDropoff() {
        return contactlessDropoff;
    }

    public String getActionIfUndeliverable() {
        return actionIfUndeliverable;
    }

    public void setExternalDeliveryId(String externalDeliveryId) {
        this.externalDeliveryId = externalDeliveryId;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public void setPickupBusinessName(String pickupBusinessName) {
        this.pickupBusinessName = pickupBusinessName;
    }

    public void setPickupPhoneNumber(String pickupPhoneNumber) {
        this.pickupPhoneNumber = pickupPhoneNumber;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public void setDropoffBusinessName(String dropoffBusinessName) {
        this.dropoffBusinessName = dropoffBusinessName;
    }

    public void setDropoffPhoneNumber(String dropoffPhoneNumber) {
        this.dropoffPhoneNumber = dropoffPhoneNumber;
    }

    public void setDropoffInstructions(String dropoffInstructions) {
        this.dropoffInstructions = dropoffInstructions;
    }

    public void setDropoffContactGivenName(String dropoffContactGivenName) {
        this.dropoffContactGivenName = dropoffContactGivenName;
    }

    public void setDropoffContactSendNotifications(boolean dropoffContactSendNotifications) {
        this.dropoffContactSendNotifications = dropoffContactSendNotifications;
    }

    public void setSchedulingModel(String schedulingModel) {
        this.schedulingModel = schedulingModel;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setContactlessDropoff(boolean contactlessDropoff) {
        this.contactlessDropoff = contactlessDropoff;
    }

    public void setActionIfUndeliverable(String actionIfUndeliverable) {
        this.actionIfUndeliverable = actionIfUndeliverable;
    }

    // Add getters for other fields as needed
}
