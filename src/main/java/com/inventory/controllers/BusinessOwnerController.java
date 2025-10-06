package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.inventory.dto.BusinessOwnerDTO;
import com.inventory.dto.BusinessOwnerRequest;
import com.inventory.dto.BusinessOwnerWithSubscriptionDTO;
import com.inventory.dto.EmployeeRequest;
import com.inventory.dto.LoginRequest;
import com.inventory.services.BusinessOwnerService;
import com.inventory.services.BusinessService;
import com.inventory.services.EmployeeService;
import com.inventory.services.SubscriptionService;
import com.inventory.services.UserService;
import com.inventory.dto.EmployeeUpdateRequest;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Subscription;
import com.inventory.entities.User;
import com.inventory.enums.SubscriptionLevel;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.PlanRepository;
import com.inventory.repositories.SubscriptionPlanRepository;
import com.inventory.repositories.SubscriptionRepository;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/business")
@RequiredArgsConstructor
public class BusinessOwnerController {

    private final EmployeeService employeeService;
    private final UserService userService; // ✅ Add this if not already
    private final BusinessService businessService;
    private final BusinessOwnerService businessOwnerService;

    private final BusinessOwnerRepository businessOwnerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionService subscriptionService;
    
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

    @PostMapping("/add-business-owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessOwnerDTO> addBusinessOwner(@RequestBody BusinessOwnerRequest request) {
        BusinessOwnerDTO createdOwner = businessOwnerService.createBusinessOwner(request);
        return ResponseEntity.ok(createdOwner);
    }
    
 // List all business owners (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-business-owners")
    public ResponseEntity<List<BusinessOwnerDTO>> getAllBusinessOwners() {
        List<BusinessOwnerDTO> owners = businessOwnerService.getAllBusinessOwners();
        return ResponseEntity.ok(owners);
    }

    // Update business owner by ID (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update-business-owner/{id}")
    public ResponseEntity<BusinessOwnerDTO> updateBusinessOwner(
            @PathVariable Long id,
            @RequestBody BusinessOwnerRequest request) {
        BusinessOwnerDTO updated = businessOwnerService.updateBusinessOwner(id, request);
        return ResponseEntity.ok(updated);
    }

    // Delete business owner by ID (ADMIN only)
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete-business-owner/{id}")
    public ResponseEntity<String> deleteBusinessOwner(@PathVariable Long id) {
        businessOwnerService.deleteBusinessOwner(id);
        return ResponseEntity.ok("Business owner deleted successfully");
    }


    @GetMapping("/business-owner/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BusinessOwnerDTO> getBusinessOwnerById(@PathVariable Long id) {
        BusinessOwnerDTO owner = businessOwnerService.getBusinessOwnerById(id);
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(owner);
    }
    
    @GetMapping("/{ownerId}/details")
    @PreAuthorize("hasAnyRole('ADMIN','BUSINESS_OWNER','EMPLOYEE')")
    public ResponseEntity<?> getBusinessOwnerDetails(@PathVariable Long ownerId) {
        BusinessOwner owner = businessOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("BusinessOwner not found"));

        Subscription subscription = subscriptionService.getLatestOrActiveSubscription(ownerId);

        Map<String, Object> response = new HashMap<>();
        response.put("id", owner.getId());
        response.put("name", owner.getName());
        response.put("email", owner.getEmail());
        response.put("businessId", owner.getBusiness() != null ? owner.getBusiness().getId() : null); // ✅ Add this
        response.put("businessName", owner.getBusinessName());
        response.put("subscriptionPlan", subscription != null ? subscription.getPlan().getName() : "None");
        response.put("startDate", subscription != null ? subscription.getStartDate() : null);
        response.put("endDate", subscription != null ? subscription.getEndDate() : null);
        response.put("active", subscription != null && subscription.isActive());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/business-owner/{id}/subscription")
    public BusinessOwnerWithSubscriptionDTO getBusinessOwnerWithSubscription(@PathVariable Long id) {
        BusinessOwner owner = businessOwnerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Subscription subscription = subscriptionService.getLatestOrActiveSubscription(id);

        return new BusinessOwnerWithSubscriptionDTO(owner, subscription);
    }



}

