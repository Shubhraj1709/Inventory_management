package com.inventory.dto;

import com.inventory.entities.BusinessOwner;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessOwnerDTO {
    private Long id;
    private String name;
    private String email;
    private boolean isActive;

    public BusinessOwnerDTO(BusinessOwner businessOwner) {
        this.id = businessOwner.getId();
        this.name = businessOwner.getName();
        this.email = businessOwner.getEmail();
        this.isActive = businessOwner.isActive();
    }
}
