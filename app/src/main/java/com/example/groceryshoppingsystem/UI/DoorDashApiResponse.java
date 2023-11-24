package com.example.groceryshoppingsystem.UI;
import com.google.gson.annotations.SerializedName;

public class DoorDashApiResponse {
    @SerializedName("external_delivery_id")
    private String externalDeliveryId;

    @SerializedName("currency")
    private String currency;

    @SerializedName("delivery_status")
    private String deliveryStatus;

    @SerializedName("fee")
    private int fee;

    @SerializedName("pickup_address")
    private String pickupAddress;

    @SerializedName("pickup_business_name")
    private String pickupBusinessName;

    @SerializedName("pickup_phone_number")
    private String pickupPhoneNumber;

    @SerializedName("pickup_instructions")
    private String pickupInstructions;

    @SerializedName("pickup_reference_tag")
    private String pickupReferenceTag;

    @SerializedName("pickup_external_business_id")
    private String pickupExternalBusinessId;

    @SerializedName("pickup_external_store_id")
    private String pickupExternalStoreId;

    @SerializedName("dropoff_address")
    private String dropoffAddress;

    @SerializedName("dropoff_business_name")
    private String dropoffBusinessName;

    @SerializedName("dropoff_location")
    private DropoffLocation dropoffLocation;

    @SerializedName("dropoff_phone_number")
    private String dropoffPhoneNumber;

    @SerializedName("dropoff_instructions")
    private String dropoffInstructions;

    @SerializedName("dropoff_contact_given_name")
    private String dropoffContactGivenName;

    @SerializedName("dropoff_contact_family_name")
    private String dropoffContactFamilyName;

    @SerializedName("dropoff_contact_send_notifications")
    private boolean dropoffContactSendNotifications;

    @SerializedName("dropoff_options")
    private DropoffOptions dropoffOptions;

    @SerializedName("order_value")
    private int orderValue;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("pickup_time_estimated")
    private String pickupTimeEstimated;

    @SerializedName("dropoff_time_estimated")
    private String dropoffTimeEstimated;

    @SerializedName("fee_components")
    private FeeComponent[] feeComponents;

    @SerializedName("tax")
    private int tax;

    @SerializedName("tax_components")
    private TaxComponent[] taxComponents;

    @SerializedName("support_reference")
    private String supportReference;

    @SerializedName("tracking_url")
    private String trackingUrl;

    @SerializedName("contactless_dropoff")
    private boolean contactlessDropoff;

    @SerializedName("action_if_undeliverable")
    private String actionIfUndeliverable;

    @SerializedName("tip")
    private int tip;

    @SerializedName("order_contains")
    private OrderContains orderContains;

    @SerializedName("dropoff_requires_signature")
    private boolean dropoffRequiresSignature;

    @SerializedName("dropoff_cash_on_delivery")
    private int dropoffCashOnDelivery;

    // Add other fields as needed

    // Add getters for other fields

    // You can also add setters if needed

    // Nested classes for nested JSON objects

    public static class DropoffLocation {
        @SerializedName("lat")
        private double lat;

        @SerializedName("lng")
        private double lng;

        public double getLat() {
            return lat;
        }

        public double getLng() {
            return lng;
        }
    }

    public static class DropoffOptions {
        @SerializedName("signature")
        private String signature;

        @SerializedName("id_verification")
        private String idVerification;

        @SerializedName("proof_of_delivery")
        private String proofOfDelivery;

        public String getSignature() {
            return signature;
        }

        public String getIdVerification() {
            return idVerification;
        }

        public String getProofOfDelivery() {
            return proofOfDelivery;
        }
    }

    public static class FeeComponent {
        // Add fields for FeeComponent
    }

    public static class TaxComponent {
        // Add fields for TaxComponent
    }

    public static class OrderContains {
        @SerializedName("alcohol")
        private boolean alcohol;

        @SerializedName("pharmacy_items")
        private boolean pharmacyItems;

        @SerializedName("age_restricted_pharmacy_items")
        private boolean ageRestrictedPharmacyItems;

        public boolean isAlcohol() {
            return alcohol;
        }

        public boolean isPharmacyItems() {
            return pharmacyItems;
        }

        public boolean isAgeRestrictedPharmacyItems() {
            return ageRestrictedPharmacyItems;
        }
    }

    public String getExternalDeliveryId() {
        return externalDeliveryId;
    }

    public void setExternalDeliveryId(String externalDeliveryId) {
        this.externalDeliveryId = externalDeliveryId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getPickupBusinessName() {
        return pickupBusinessName;
    }

    public void setPickupBusinessName(String pickupBusinessName) {
        this.pickupBusinessName = pickupBusinessName;
    }

    public String getPickupPhoneNumber() {
        return pickupPhoneNumber;
    }

    public void setPickupPhoneNumber(String pickupPhoneNumber) {
        this.pickupPhoneNumber = pickupPhoneNumber;
    }

    public String getPickupInstructions() {
        return pickupInstructions;
    }

    public void setPickupInstructions(String pickupInstructions) {
        this.pickupInstructions = pickupInstructions;
    }

    public String getPickupReferenceTag() {
        return pickupReferenceTag;
    }

    public void setPickupReferenceTag(String pickupReferenceTag) {
        this.pickupReferenceTag = pickupReferenceTag;
    }

    public String getPickupExternalBusinessId() {
        return pickupExternalBusinessId;
    }

    public void setPickupExternalBusinessId(String pickupExternalBusinessId) {
        this.pickupExternalBusinessId = pickupExternalBusinessId;
    }

    public String getPickupExternalStoreId() {
        return pickupExternalStoreId;
    }

    public void setPickupExternalStoreId(String pickupExternalStoreId) {
        this.pickupExternalStoreId = pickupExternalStoreId;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public String getDropoffBusinessName() {
        return dropoffBusinessName;
    }

    public void setDropoffBusinessName(String dropoffBusinessName) {
        this.dropoffBusinessName = dropoffBusinessName;
    }

    public DropoffLocation getDropoffLocation() {
        return dropoffLocation;
    }

    public void setDropoffLocation(DropoffLocation dropoffLocation) {
        this.dropoffLocation = dropoffLocation;
    }

    public String getDropoffPhoneNumber() {
        return dropoffPhoneNumber;
    }

    public void setDropoffPhoneNumber(String dropoffPhoneNumber) {
        this.dropoffPhoneNumber = dropoffPhoneNumber;
    }

    public String getDropoffInstructions() {
        return dropoffInstructions;
    }

    public void setDropoffInstructions(String dropoffInstructions) {
        this.dropoffInstructions = dropoffInstructions;
    }

    public String getDropoffContactGivenName() {
        return dropoffContactGivenName;
    }

    public void setDropoffContactGivenName(String dropoffContactGivenName) {
        this.dropoffContactGivenName = dropoffContactGivenName;
    }

    public String getDropoffContactFamilyName() {
        return dropoffContactFamilyName;
    }

    public void setDropoffContactFamilyName(String dropoffContactFamilyName) {
        this.dropoffContactFamilyName = dropoffContactFamilyName;
    }

    public boolean isDropoffContactSendNotifications() {
        return dropoffContactSendNotifications;
    }

    public void setDropoffContactSendNotifications(boolean dropoffContactSendNotifications) {
        this.dropoffContactSendNotifications = dropoffContactSendNotifications;
    }

    public DropoffOptions getDropoffOptions() {
        return dropoffOptions;
    }

    public void setDropoffOptions(DropoffOptions dropoffOptions) {
        this.dropoffOptions = dropoffOptions;
    }

    public int getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(int orderValue) {
        this.orderValue = orderValue;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPickupTimeEstimated() {
        return pickupTimeEstimated;
    }

    public void setPickupTimeEstimated(String pickupTimeEstimated) {
        this.pickupTimeEstimated = pickupTimeEstimated;
    }

    public String getDropoffTimeEstimated() {
        return dropoffTimeEstimated;
    }

    public void setDropoffTimeEstimated(String dropoffTimeEstimated) {
        this.dropoffTimeEstimated = dropoffTimeEstimated;
    }

    public FeeComponent[] getFeeComponents() {
        return feeComponents;
    }

    public void setFeeComponents(FeeComponent[] feeComponents) {
        this.feeComponents = feeComponents;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public TaxComponent[] getTaxComponents() {
        return taxComponents;
    }

    public void setTaxComponents(TaxComponent[] taxComponents) {
        this.taxComponents = taxComponents;
    }

    public String getSupportReference() {
        return supportReference;
    }

    public void setSupportReference(String supportReference) {
        this.supportReference = supportReference;
    }

    public String getTrackingUrl() {
        return trackingUrl;
    }

    public void setTrackingUrl(String trackingUrl) {
        this.trackingUrl = trackingUrl;
    }

    public boolean isContactlessDropoff() {
        return contactlessDropoff;
    }

    public void setContactlessDropoff(boolean contactlessDropoff) {
        this.contactlessDropoff = contactlessDropoff;
    }

    public String getActionIfUndeliverable() {
        return actionIfUndeliverable;
    }

    public void setActionIfUndeliverable(String actionIfUndeliverable) {
        this.actionIfUndeliverable = actionIfUndeliverable;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public OrderContains getOrderContains() {
        return orderContains;
    }

    public void setOrderContains(OrderContains orderContains) {
        this.orderContains = orderContains;
    }

    public boolean isDropoffRequiresSignature() {
        return dropoffRequiresSignature;
    }

    public void setDropoffRequiresSignature(boolean dropoffRequiresSignature) {
        this.dropoffRequiresSignature = dropoffRequiresSignature;
    }

    public int getDropoffCashOnDelivery() {
        return dropoffCashOnDelivery;
    }

    public void setDropoffCashOnDelivery(int dropoffCashOnDelivery) {
        this.dropoffCashOnDelivery = dropoffCashOnDelivery;
    }
}
