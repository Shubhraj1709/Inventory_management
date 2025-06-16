package com.inventory.entities;

import com.inventory.enums.Role;
import com.inventory.enums.SubscriptionLevel;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "business_owner_id", nullable = false)
    private BusinessOwner businessOwner;
 
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserPermission> permissions;

    public String getPassword() {
        return passwordHash;  // Return passwordHash as password
    }
    
//    @Column(name = "subscription_plan")
//    @Enumerated(EnumType.STRING)
//    private SubscriptionLevel subscriptionPlan;
    
    @Column(name = "subscription_plan")
    private String subscriptionPlan;  // ✅ stores "BASIC", "PREMIUM", "ENTERPRISE"


    @Column(name = "plan_start")
    private java.time.LocalDate planStart;

    @Column(name = "plan_end")
    private java.time.LocalDate planEnd;

    public SubscriptionLevel getSubscriptionLevelEnum() {
        return SubscriptionLevel.fromDbValue(this.subscriptionPlan);
    }

    
}
