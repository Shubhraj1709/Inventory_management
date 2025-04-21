package com.inventory.controllers;

import com.inventory.entities.PurchaseTransaction;
import com.inventory.enums.PaymentMethod;
import com.inventory.services.PurchaseTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchase-transactions")
@RequiredArgsConstructor
public class PurchaseTransactionController {

    private final PurchaseTransactionService transactionService;

    @PostMapping("/create")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<PurchaseTransaction> createTransaction(
            @RequestParam Long supplierId,
            @RequestParam Long orderId,
            @RequestParam double amount,
            @RequestParam PaymentMethod  paymentMethod // ✅ Add param
            ) {
        return ResponseEntity.ok(transactionService.createTransaction(supplierId, orderId, amount,paymentMethod));
    }
    
    

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseTransaction>> getSupplierTransactions(@PathVariable Long supplierId) {
        return ResponseEntity.ok(transactionService.getTransactionsBySupplier(supplierId));
    }
}
