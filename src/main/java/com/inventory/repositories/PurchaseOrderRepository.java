package com.inventory.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.PurchaseOrder;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findBySupplierId(Long supplierId);

}
