package com.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;
}
