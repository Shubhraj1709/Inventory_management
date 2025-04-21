package com.inventory.dto;

import com.inventory.entities.User;
import com.inventory.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private Role role;

    // Constructor to convert User entity to UserDTO
    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
    }
}
