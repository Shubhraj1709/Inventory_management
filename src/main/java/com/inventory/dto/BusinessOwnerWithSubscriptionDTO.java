package com.inventory.dto;

import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Subscription;
import com.inventory.enums.PlanType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BusinessOwnerWithSubscriptionDTO {
    private Long ownerId;
    private String ownerName;
    private String email;
    private boolean isActive;
    private Long businessId;
    private LocalDate subscriptionStartDate;
    private LocalDate subscriptionEndDate;
    private String planType;

    public BusinessOwnerWithSubscriptionDTO(BusinessOwner owner, Subscription subscription) {
        this.ownerId = owner.getId();
        this.ownerName = owner.getName();
        this.email = owner.getEmail();
        this.isActive = owner.isActive();
        this.businessId = owner.getBusiness().getId();

        if (subscription != null) {
            this.subscriptionStartDate = subscription.getStartDate();
            this.subscriptionEndDate = subscription.getEndDate();

            PlanType type = subscription.getPlan().getPlanType();
            this.planType = type != null ? type.name() : "UNKNOWN";
        }
    }
}
