package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.LoginRequest;
import com.inventory.services.BusinessOwnerService;
import com.inventory.services.EmployeeService;
import com.inventory.dto.EmployeeUpdateRequest;


import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessOwnerController {

    private final EmployeeService employeeService;
    
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    @PostMapping("/add-employee")
    public ResponseEntity<String> addEmployee(@RequestBody EmployeeRequest request) {
        employeeService.addEmployee(request);
        return ResponseEntity.ok("Employee added successfully!");
    }

    @PutMapping("/update-employee/{id}")
    public ResponseEntity<String> updateEmployee(@PathVariable Long id, @RequestBody EmployeeUpdateRequest request) {
        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok("Employee updated successfully!");
    }

}

