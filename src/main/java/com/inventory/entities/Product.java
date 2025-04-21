package com.inventory.entities;



import com.inventory.enums.ProductCategory;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @ManyToOne
    @JoinColumn(name = "business_id")
    private Business business;


}
