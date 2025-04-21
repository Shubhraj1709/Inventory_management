package com.inventory.dto;

import lombok.*;

@Data
@Getter
@Setter
//@AllArgsConstructor
public class JwtResponse {
    private String token;

	public JwtResponse(String token) {
		this.token = token;
	}
    
    
}
