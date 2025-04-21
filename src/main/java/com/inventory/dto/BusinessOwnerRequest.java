package com.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessOwnerRequest {
    private String businessName;
    private String email;
    private String password;
}
