package com.inventory.dto;

import lombok.*;
import com.inventory.enums.EmployeeRole;
import com.inventory.enums.Role;

@Getter
@Setter
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private Role role;
}
