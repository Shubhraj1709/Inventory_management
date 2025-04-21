package com.inventory.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

import com.inventory.enums.Role;

@Getter
@Setter
public class EmployeeRequest {
    private String name;
    private String email;
    private String password;
    private List<String> permissions;
    private Role role;        // ✅ Added
    private Long businessId;  // ✅ Added    

}
