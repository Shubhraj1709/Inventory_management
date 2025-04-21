package com.inventory.controllers;

import com.inventory.entities.StockMovement;
import com.inventory.enums.MovementType;
import com.inventory.services.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PostMapping("/record")
    @PreAuthorize("hasRole('BUSINESS_OWNER')") // ✅ Only Business Owner
    public ResponseEntity<String> moveStock(
            @RequestParam Long productId,
            @RequestParam Long businessId,
            @RequestParam MovementType type,
            @RequestParam int quantity,
            @RequestParam(required = false) String reason,
            @RequestParam Long warehouseId // ✅ Add this

    ) {
        stockService.recordStockMovement(productId, businessId, type, quantity, reason, warehouseId);
        return ResponseEntity.ok("Stock movement recorded successfully!");
    }
    
    @GetMapping("/level/{productId}")
    public ResponseEntity<Integer> getStockLevel(@PathVariable Long productId) {
        int stock = stockService.getStockLevel(productId);
        return ResponseEntity.ok(stock);
    }
    
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'EMPLOYEE')")
    @GetMapping("/low-stock/{productId}")
    public ResponseEntity<Boolean> isLowStock(@PathVariable Long productId,
                                              @RequestParam(defaultValue = "5") int threshold) {
        return ResponseEntity.ok(stockService.isLowStock(productId, threshold));
    }

    @GetMapping("/movements")
    public ResponseEntity<List<StockMovement>> getAllMovements() {
        return ResponseEntity.ok(stockService.getAllMovements());
    }
    
    @GetMapping("/stock-level")
    @PreAuthorize("hasAnyRole('BUSINESS_OWNER', 'EMPLOYEE')")
    public ResponseEntity<Integer> getStockLevelByWarehouse(
            @RequestParam Long productId,
            @RequestParam Long warehouseId) {
        return ResponseEntity.ok(stockService.getStockLevel(productId, warehouseId));
    }

}
