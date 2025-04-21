package com.inventory.dto;

import com.inventory.enums.ProductCategory;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private ProductCategory category;
}


