package com.inventory.controllers;


import com.inventory.dto.BusinessDTO;
import com.inventory.dto.BusinessRequest;
import com.inventory.services.BusinessService;
import com.inventory.services.FirmAdminService;


import lombok.*;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class FirmAdminController {

    private final BusinessService businessService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register-business")
    public ResponseEntity<String> registerBusiness(@Valid @RequestBody BusinessRequest request) {
        businessService.registerBusiness(request);
        return ResponseEntity.ok("Business registered successfully!");
    }

    @PutMapping("/enable-business/{id}")
    public ResponseEntity<String> enableBusiness(@PathVariable Long id) {
        businessService.enableBusiness(id);
        return ResponseEntity.ok("Business enabled successfully!");
    }

    @PutMapping("/disable-business/{id}")
    public ResponseEntity<String> disableBusiness(@PathVariable Long id) {
        businessService.disableBusiness(id);
        return ResponseEntity.ok("Business disabled successfully!");
    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all-businesses")
    public ResponseEntity<List<BusinessDTO>> getAllBusinesses() {
        return ResponseEntity.ok(businessService.getAllBusinesses());
    }
}
