package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

import com.inventory.enums.PlanType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "subscription_plans")
public class SubscriptionPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private BigDecimal price;
    private int maxUsers; // Number of users allowed in this plan

    @Enumerated(EnumType.STRING)
    private PlanType planType;
}
