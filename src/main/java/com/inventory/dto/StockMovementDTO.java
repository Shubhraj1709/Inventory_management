package com.inventory.dto;

import lombok.Data;

@Data
public class StockMovementDTO {
    private Long businessId;
    private Long productId;
    private String type; // INCREASE or DECREASE
    private int quantity;
    private String reason;
}
