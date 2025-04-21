package com.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginDTO {
    private String email;
    private String password;
}
