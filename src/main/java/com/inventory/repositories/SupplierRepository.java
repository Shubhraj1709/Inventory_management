package com.inventory.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Business;
import com.inventory.entities.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
	
    List<Supplier> findByBusiness(Business business);

}
