package com.inventory.dto;

import lombok.*;
import com.inventory.enums.PaymentStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    private Long id; // 
    
    private String invoiceNumber;
    private double totalAmount;
    private double taxAmount;
    private double discountAmount;
    private double finalAmount;
    private PaymentStatus paymentStatus;
    private Long orderId;
    private List<InvoiceItemDTO> items;
    private String pdfUrl;

}
