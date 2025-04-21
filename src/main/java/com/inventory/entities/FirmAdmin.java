package com.inventory.entities;

import com.inventory.enums.Role;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "firm_admins")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FirmAdmin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;

    private String password;
    
    @Enumerated(EnumType.STRING)
    private Role role = Role.ADMIN;
}
