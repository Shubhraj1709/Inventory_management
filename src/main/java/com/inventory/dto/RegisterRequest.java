package com.inventory.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;     // ✅ Add this
	private String email;
    private String password;
    private String role; // e.g., "EMPLOYEE", "FIRM_ADMIN"
    private Long businessId; // ✅ make sure this is present
    private Long businessOwnerId;


}
