package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Warehouse;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
}
