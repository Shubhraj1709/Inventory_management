package com.inventory.entities;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderStatus;
    
    @ManyToOne
    @JoinColumn(name = "business_owner_id")
    private BusinessOwner businessOwner;
    
    private double totalAmount;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime createdAt;

    public Long getBusinessOwnerId() {
        return businessOwner != null ? businessOwner.getId() : null;
    }
    
    public Order(String orderStatus, BusinessOwner businessOwner) {
        this.orderStatus = orderStatus;
        this.businessOwner = businessOwner;
    }

}
