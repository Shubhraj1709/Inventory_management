package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

import com.inventory.enums.NotificationType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String message;
    
    @Column(name = "is_read")
    private boolean read;
    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

    private Long referenceId; // Reference to Product, Invoice, or Order
}
