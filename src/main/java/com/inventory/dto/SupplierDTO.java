package com.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SupplierDTO {
    private Long id;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private Long businessId; // ✅ Add this

}
