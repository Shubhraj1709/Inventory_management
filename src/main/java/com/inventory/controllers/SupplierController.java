package com.inventory.controllers;


import com.inventory.dto.SupplierDTO;
import com.inventory.entities.Supplier;
import com.inventory.services.SupplierService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @PostMapping
    public ResponseEntity<SupplierDTO> addSupplier(@RequestBody SupplierDTO dto) {
        return ResponseEntity.ok(supplierService.addSupplier(dto));
    }

    @GetMapping("/{businessId}")
    public ResponseEntity<List<SupplierDTO>> getSuppliersByBusinessId(@PathVariable Long businessId) {
        List<SupplierDTO> suppliers = supplierService.getSuppliersByBusinessId(businessId);
        suppliers.forEach(s -> System.out.println("Contact Person: " + s.getContactPerson()));
        return ResponseEntity.ok(suppliers);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Supplier> updateSupplier(@PathVariable Long id, @RequestBody Supplier updatedSupplier) {
        try {
            Supplier updated = supplierService.updateSupplier(id, updatedSupplier);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.noContent().build();
    }


    
    
}
