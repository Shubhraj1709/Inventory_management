package com.inventory.repositories;

import com.inventory.entities.PurchaseTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PurchaseTransactionRepository extends JpaRepository<PurchaseTransaction, Long> {

    List<PurchaseTransaction> findBySupplierId(Long supplierId);

    List<PurchaseTransaction> findBySupplierIdAndTransactionDateBetween(
        Long supplierId, LocalDateTime startDate, LocalDateTime endDate
    );
}
