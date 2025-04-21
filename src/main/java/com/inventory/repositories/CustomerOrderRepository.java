package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
}
