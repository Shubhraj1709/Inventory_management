package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.inventory.dto.BusinessOwnerDTO;
import com.inventory.dto.BusinessOwnerRequest;
import com.inventory.entities.BusinessOwner;

import java.util.List;
import java.util.Optional;

public interface BusinessOwnerRepository extends JpaRepository<BusinessOwner, Long> {
    Optional<BusinessOwner> findByEmail(String email);
    
 // Updated to support multiple owners per business
    List<BusinessOwner> findByBusiness_Id(Long businessId);
    @Query("SELECT new com.inventory.dto.BusinessOwnerDTO(b.id, b.name, b.email) FROM BusinessOwner b")
    List<BusinessOwnerDTO> getAllBusinessOwners(); 
    
    //List<BusinessOwner> findAll();

    
    // List<BusinessOwnerDTO> getAllBusinessOwners();
    // BusinessOwnerDTO updateBusinessOwner(Long id, BusinessOwnerRequest request);
    //void deleteBusinessOwner(Long id);

}
