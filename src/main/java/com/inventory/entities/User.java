package com.inventory.entities;

import com.inventory.enums.Role;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    
    @Column(unique = true, nullable = false)
    private String email;
    
    @Column(name = "password_hash")
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @JoinColumn(name = "business_owner_id")
    private BusinessOwner businessOwner;
 
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserPermission> permissions;

    public String getPassword() {
        return passwordHash;  // Return passwordHash as password
    }

}
