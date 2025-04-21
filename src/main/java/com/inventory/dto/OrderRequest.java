package com.inventory.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private Long businessOwnerId;
    private String orderStatus;
}
