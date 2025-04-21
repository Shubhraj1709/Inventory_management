package com.inventory.entities;

import java.time.LocalDate;

import com.inventory.enums.PaymentStatus;
import com.inventory.enums.SubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder

@Table(name = "businesses")
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "business_name", nullable = false)
    private String businessName;
    
    private String status; // Active, Inactive
    private String ownerUsername;
    private String ownerPassword;

    @Enumerated(EnumType.STRING)
    private SubscriptionPlan subscriptionPlan;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDate nextDueDate;


    private int maxUsers;
    
    @ManyToOne
    @JoinColumn(name = "business_owner_id", nullable = false) // Make sure it matches your DB schema
    private BusinessOwner businessOwner;

}