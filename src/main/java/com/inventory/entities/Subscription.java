package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscriptions")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "business_owner_id" , nullable = false) // Ensure this matches your BusinessOwner
    private BusinessOwner businessOwner; // Change from Business to BusinessOwner


    @ManyToOne
    @JoinColumn(name = "plan_id")
    private SubscriptionPlan plan;

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isActive;
}

