package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.LoginRequest;
import com.inventory.services.BusinessOwnerService;
import com.inventory.services.BusinessService;
import com.inventory.services.EmployeeService;
import com.inventory.services.UserService;
import com.inventory.dto.EmployeeUpdateRequest;

import com.inventory.entities.User;
import com.inventory.enums.SubscriptionLevel;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessOwnerController {

    private final EmployeeService employeeService;
    private final UserService userService; // ✅ Add this if not already
    private final BusinessService businessService;
    
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'ADMIN', 'EMPLOYEE')")
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
    
 // ✅ Add this method for testing subscription-based access
    @GetMapping("/secure-feature")
    public ResponseEntity<?> getSecureFeature(org.springframework.security.core.Authentication authentication) {
        String email = authentication.getName();
        Optional<com.inventory.entities.User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found");
        }

        com.inventory.entities.User user = userOpt.get();

        SubscriptionLevel userLevel = SubscriptionLevel.fromDbValue(user.getSubscriptionPlan());

        if (userLevel == SubscriptionLevel.FREE) {
            return ResponseEntity.status(403).body("❌ Access denied: Subscription expired");
        }

        return ResponseEntity.ok("✅ You have access to this secure business feature!");
    }
    
    @PostMapping("/mark-payment/{businessId}")
    public ResponseEntity<String> markPayment(@PathVariable Long businessId) {
        businessService.markPaymentAsCompleted(businessId);
        return ResponseEntity.ok("✅ Payment marked as completed");
    }


}

