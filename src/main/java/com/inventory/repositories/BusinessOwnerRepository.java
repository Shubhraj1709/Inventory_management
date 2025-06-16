package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.BusinessOwner;

import java.util.List;
import java.util.Optional;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {
    Optional<BusinessOwner> findByEmail(String email);
    
 // Updated to support multiple owners per business
    List<BusinessOwner> findByBusiness_Id(Long businessId);
}
