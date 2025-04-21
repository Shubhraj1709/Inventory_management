package com.inventory.dto;

import lombok.Data;

@Data
public class ProductRequest {
    private String name;
    private String category;
    private double price;
    private int stock;
}
