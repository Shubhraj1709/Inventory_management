package com.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BusinessDTO {
    private Long id;
    private String name;
    private String email;
    private boolean active;
    
    
}
