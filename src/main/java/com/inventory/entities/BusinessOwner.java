package com.inventory.entities;

import com.inventory.enums.SubscriptionLevel; // Ensure the enum is used

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

    @Column(name = "name", nullable = false)
    private String name;
    
    private String email;
    private String passwordHash;
    private boolean isActive;

    @Column(name = "subscription_plan", length = 20)
    @Enumerated(EnumType.STRING)
    private SubscriptionLevel subscriptionPlan;
    
    @OneToOne
    @JoinColumn(name = "business_id")
    private Business business;


    
//    @OneToOne(mappedBy = "businessOwner", cascade = CascadeType.ALL)
//    @JoinColumn(name = "business_id", nullable = false)
//    private Business business;

}
