package com.inventory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessOwnerRequest {
	private String businessName;
    private String name;
    private String email;
    private String password;
    private Long businessId;
    private String subscriptionPlan;
    
//    private String businessName;
//    private String email;
//    private String password;
}
