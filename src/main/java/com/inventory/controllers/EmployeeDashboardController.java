package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/employee")
public class EmployeeDashboardController {

	@PreAuthorize("hasPermission(null, 'VIEW_ORDERS')")	
	@GetMapping("/orders")
	public ResponseEntity<String> getOrders() {
	    return ResponseEntity.ok("Only employees with 'VIEW_ORDERS' can access this.");
	}


    @PreAuthorize("hasPermission(null, 'MANAGE_PRODUCTS')")
    @GetMapping("/products")
    public ResponseEntity<String> getEmployeeProducts() {
        return ResponseEntity.ok("Employee: Managing Products");
    }
}
