package com.inventory.repositories;

import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BusinessRepository extends JpaRepository<Business, Long> {
}
