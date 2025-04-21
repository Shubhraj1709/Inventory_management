package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

import com.inventory.enums.PaymentMethod;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "purchase_transactions")
public class PurchaseTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double amount;

    private LocalDateTime transactionDate;

    @Enumerated(EnumType.STRING)
    private PaymentMethod  paymentMethod;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @ManyToOne
    @JoinColumn(name = "purchase_order_id")
    private PurchaseOrder purchaseOrder;
}
