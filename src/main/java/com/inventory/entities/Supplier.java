package com.inventory.entities;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Column(name = "contact_person")
    private String contactPerson;
    @Column(name = "email")
    private String email;
    private String phone;
    private String address;
    
    @ManyToOne
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;

}
