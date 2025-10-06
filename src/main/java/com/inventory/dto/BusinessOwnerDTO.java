package com.inventory.dto;

import com.inventory.entities.BusinessOwner;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class BusinessOwnerDTO {
    private Long id;
    private String name;
    private String email;
    private boolean isActive;
    private Long businessId;  // Add this field

    public BusinessOwnerDTO(BusinessOwner businessOwner) {
        this.id = businessOwner.getId();
        this.name = businessOwner.getName();
        this.email = businessOwner.getEmail();
        this.isActive = businessOwner.isActive();
        this.businessId = businessOwner.getBusiness().getId();
    }

    public BusinessOwnerDTO(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.isActive = true;
    }

    public BusinessOwnerDTO(Long id, String name, String email, Long businessId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.businessId = businessId;
        this.isActive = true;
    }
}







//package com.inventory.dto;
//
//import com.inventory.entities.BusinessOwner;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@Getter
//@Setter
//@NoArgsConstructor
//public class BusinessOwnerDTO {
//    private Long id;
//    private String name;
//    private String email;
//    private boolean isActive;
//
//    public BusinessOwnerDTO(BusinessOwner businessOwner) {
//        this.id = businessOwner.getId();
//        this.name = businessOwner.getName();
//        this.email = businessOwner.getEmail();
//        this.isActive = businessOwner.isActive();
//    }
//    
//    public BusinessOwnerDTO(Long id, String name, String email) {
//        this.id = id;
//        this.name = name;
//        this.email = email;
//        this.isActive = true; // or false, depending on your use case
//    }
//}
