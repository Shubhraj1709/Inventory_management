package com.inventory.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPermission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean canRead = false;
    private boolean canWrite = false;
    private boolean canDelete = false;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
