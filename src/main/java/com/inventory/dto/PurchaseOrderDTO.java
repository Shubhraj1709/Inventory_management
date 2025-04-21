package com.inventory.dto;


import lombok.*;
import com.inventory.dto.PurchaseOrderItemDTO;

import com.inventory.enums.OrderStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDTO {
    private Long id;
    private Long supplierId;
    private double totalAmount;
    private OrderStatus status;
    private List<PurchaseOrderItemDTO> items;
}
