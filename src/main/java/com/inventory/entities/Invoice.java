package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

import com.inventory.enums.PaymentStatus;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoices")
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNumber;
    private double totalAmount;
    private double taxAmount;
    private double discountAmount;
    private double finalAmount;
    
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL)
    private List<InvoiceItem> invoiceItems;
    
 // Invoice.java

 // Invoice.java
    @Column(name = "business_id", nullable = false)
    private Long businessId;

    // Add setter
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }


}
