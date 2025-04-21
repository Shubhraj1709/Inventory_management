package com.inventory.controllers;

import com.inventory.dto.ProductDTO;
import com.inventory.dto.ProductRequest;
import com.inventory.entities.Product;
import com.inventory.services.ProductService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/business/{businessId}/owner/{ownerId}/products")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") //today i update this after successfully test apis
    public ResponseEntity<String> addProduct(@PathVariable Long businessId, @RequestBody ProductDTO request, @PathVariable Long ownerId) {
        productService.addProduct(request, businessId, ownerId);
        return ResponseEntity.ok("Product added successfully!");
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<String> updateProduct(@PathVariable Long id, @RequestBody ProductDTO request) {
        productService.updateProduct(id, request);
        return ResponseEntity.ok("Product updated successfully!");
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully!");
    }

    @PreAuthorize("hasAnyRole('EMPLOYEE', 'BUSINESS_OWNER')")
    @GetMapping("/all/{businessId}")
    public ResponseEntity<List<ProductDTO>> getAllProducts(@PathVariable Long businessId) {
        return ResponseEntity.ok(productService.getAllProducts(businessId));
    }
}

