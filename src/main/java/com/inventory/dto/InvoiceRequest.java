package com.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InvoiceRequest {
    private Long orderId;
    private double taxRate;
    private double discount;
}
