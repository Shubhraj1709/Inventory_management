package com.inventory.controllers;

import com.inventory.dto.EmployeeDTO;
import com.inventory.dto.EmployeeLoginDTO;
import com.inventory.dto.EmployeeUpdateRequest;
import com.inventory.services.EmployeeService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'BUSINESS_OWNER','EMPLOYEE')") // restrict if needed
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEmployeePermissions(@PathVariable Long id, @RequestBody EmployeeUpdateRequest request) {
        employeeService.updateEmployee(id, request);
        return ResponseEntity.ok().body(Map.of("message", "Permissions updated successfully"));
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployeeById(id);
        return ResponseEntity.ok().build();
    }

    
    

}
