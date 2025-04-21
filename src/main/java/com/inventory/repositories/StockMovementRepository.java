package com.inventory.repositories;

import com.inventory.entities.StockMovement;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.type = 'INCREASE'")
    Integer getTotalIncreasedStock(@Param("productId") Long productId);

    @Query("SELECT SUM(sm.quantity) FROM StockMovement sm WHERE sm.product.id = :productId AND sm.type = 'DECREASE'")
    Integer getTotalDecreasedStock(@Param("productId") Long productId);
    
 // ✅ New for warehouse-based filtering
    List<StockMovement> findByProductIdAndWarehouseId(Long productId, Long warehouseId);
}

