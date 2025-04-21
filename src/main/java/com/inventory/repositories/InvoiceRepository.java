package com.inventory.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inventory.entities.Invoice;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
	
	@Query("SELECT i FROM Invoice i WHERE i.paymentStatus = 'UNPAID'")
    List<Invoice> findAllUnpaidInvoices();
	
	List<Invoice> findByBusinessId(Long businessId);

}
