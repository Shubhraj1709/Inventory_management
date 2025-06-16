package com.inventory.dto;

import java.time.LocalDate;

public class SubscriptionAssignmentDto {
    private String plan;      // e.g., "BASIC", "PREMIUM", "ENTERPRISE"
    private LocalDate planStart;
    private LocalDate planEnd;

    // Getters and setters
    public String getPlan() {
        return plan;
    }
    public void setPlan(String plan) {
        this.plan = plan;
    }
    public LocalDate getPlanStart() {
        return planStart;
    }
    public void setPlanStart(LocalDate planStart) {
        this.planStart = planStart;
    }
    public LocalDate getPlanEnd() {
        return planEnd;
    }
    public void setPlanEnd(LocalDate planEnd) {
        this.planEnd = planEnd;
    }
}
