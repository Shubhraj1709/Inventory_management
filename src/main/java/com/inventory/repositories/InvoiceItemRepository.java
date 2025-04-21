package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.InvoiceItem;

public interface InvoiceItemRepository extends JpaRepository<InvoiceItem, Long> {
}
