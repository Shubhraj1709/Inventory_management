package com.inventory.dto;

import lombok.*;
import jakarta.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessRequest {

    @NotBlank
  //  @JsonProperty("businessName") // maps JSON field "businessName" to this Java field
    private String businessName;

    @NotBlank
    private String ownerName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String subscriptionPlan;
    
    private String role; // e.g., "BUSINESS_OWNER"
    
}

