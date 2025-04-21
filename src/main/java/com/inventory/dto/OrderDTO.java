package com.inventory.dto;

import java.time.LocalDateTime;

import com.inventory.entities.Order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class OrderDTO {
    private Long id;
    private String orderStatus;
    private Long businessOwnerId;
    
    private Long userId;          
    private Double totalAmount;   
    private LocalDateTime createdAt;

    public OrderDTO() {
        // Default constructor needed for deserialization
    }

    
    public OrderDTO(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus();
        this.businessOwnerId = order.getBusinessOwnerId();
        
        this.userId = order.getUser() != null ? order.getUser().getId() : null;
        this.totalAmount = order.getTotalAmount();
        this.createdAt = order.getCreatedAt();
    }
}
