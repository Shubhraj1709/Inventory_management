package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.inventory.enums.MovementType;

@Entity
@Table(name = "stock_movements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StockMovement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Business business;
    
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse; // ✅ Multi-warehouse support

    @Enumerated(EnumType.STRING)
    private MovementType type; // INCREASE or DECREASE

    private int quantity;

    private String reason;

    private LocalDateTime movementTime = LocalDateTime.now();
}
