package com.inventory.dto;

public class PaymentRequest {
    private double amount;
    private Long planId;
    private Long businessId;
    private Long businessOwnerId; // ✅ NEW field

    // Getters & Setters
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }

    public Long getBusinessId() { return businessId; }
    public void setBusinessId(Long businessId) { this.businessId = businessId; }
    
    public Long getBusinessOwnerId() { return businessOwnerId; }
    public void setBusinessOwnerId(Long businessOwnerId) { this.businessOwnerId = businessOwnerId; }

}
