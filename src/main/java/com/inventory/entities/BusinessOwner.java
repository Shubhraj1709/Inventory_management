package com.inventory.entities;

import com.inventory.enums.SubscriptionPlan; // Ensure the enum is used

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Table(name = "business_owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessOwner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false)
    private String businessName; // 👈 required for DB

    private String name;
    private String email;
    private String passwordHash;
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
}
