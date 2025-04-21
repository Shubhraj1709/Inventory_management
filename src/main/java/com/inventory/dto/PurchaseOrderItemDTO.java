package com.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderItemDTO {
    private Long productId;
    private String productName;
    private int quantity;
    private double price;
}
