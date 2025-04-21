package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "invoice_items")
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String productName;
    private int quantity;
    private double unitPrice;
    private double totalPrice;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;
}
