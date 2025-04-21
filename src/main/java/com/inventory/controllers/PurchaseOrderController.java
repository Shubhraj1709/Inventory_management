package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.entities.PurchaseOrder;
import com.inventory.services.PurchaseOrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<PurchaseOrder> createOrder(
            @RequestParam Long supplierId,
            @RequestParam double totalAmount
    ) {
        return ResponseEntity.ok(purchaseOrderService.createOrder(supplierId, totalAmount));
    }
}
