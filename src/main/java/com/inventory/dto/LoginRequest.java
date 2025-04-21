package com.inventory.dto;

import lombok.*;

@Data
@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
}
