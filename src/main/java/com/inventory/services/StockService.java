package com.inventory.services;

import com.inventory.entities.*;
import com.inventory.enums.MovementType;
import com.inventory.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StockService {

    private final StockMovementRepository stockMovementRepository;
    private final ProductRepository productRepository;
    private final BusinessRepository businessRepository;
    private final WarehouseRepository warehouseRepository; // ✅ Add this


    public void recordStockMovement(Long productId, Long businessId, MovementType type, int quantity, String reason, Long warehouseId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Business business = businessRepository.findById(businessId)
                .orElseThrow(() -> new RuntimeException("Business not found"));
        
        Warehouse warehouse = warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new RuntimeException("Warehouse not found")); // ✅ Fetch warehouse

        if (type == MovementType.DECREASE && product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock!");
        }

        StockMovement movement = new StockMovement();
        movement.setProduct(product);
        movement.setBusiness(business);
        movement.setType(type);
        movement.setQuantity(quantity);
        movement.setReason(reason);
        movement.setWarehouse(warehouse); // ✅ IMPORTANT

        stockMovementRepository.save(movement);

        int updatedStock = type == MovementType.INCREASE
                ? product.getStockQuantity() + quantity
                : product.getStockQuantity() - quantity;

        product.setStockQuantity(updatedStock);	
        productRepository.save(product);
        
        System.out.println("Saving stock movement with warehouseId: " + warehouse.getId());

        System.out.println("StockMovement warehouse: " + movement.getWarehouse());

    }

    public List<StockMovement> getAllMovements() {
        return stockMovementRepository.findAll();
    }
    
    public int getStockLevel(Long productId) {
        Integer increased = stockMovementRepository.getTotalIncreasedStock(productId);
        Integer decreased = stockMovementRepository.getTotalDecreasedStock(productId);

        // Handle null values if no movement exists yet
        int netStock = (increased != null ? increased : 0) - (decreased != null ? decreased : 0);

        return netStock;
    }
    
    public int getStockLevel(Long productId, Long warehouseId) {
        List<StockMovement> movements = stockMovementRepository.findByProductIdAndWarehouseId(productId, warehouseId);

        int stock = 0;
        for (StockMovement movement : movements) {
            if (movement.getType() == MovementType.INCREASE) {
                stock += movement.getQuantity();
            } else if (movement.getType() == MovementType.DECREASE) {
                stock -= movement.getQuantity();
            }
        }
        return stock;
    }
    
    public boolean isLowStock(Long productId, int threshold) {
        int currentStock = getStockLevel(productId);
        return currentStock < threshold;
    }


}


    

