package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inventory.entities.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
