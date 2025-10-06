package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.inventory.entities.Subscription;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findByBusinessOwnerId(Long businessOwnerId); // ✅ Add this method

    Optional<Subscription> findTopByBusinessOwnerIdAndIsActiveTrueOrderByStartDateDesc(Long businessOwnerId);

    Optional<Subscription> findTopByBusinessOwnerIdOrderByStartDateDesc(Long businessOwnerId);

    @Query("SELECT s FROM Subscription s WHERE s.businessOwner.business.id = :businessId AND s.isActive = true ORDER BY s.startDate DESC")
    List<Subscription> findActiveSubscriptionsByBusinessId(@Param("businessId") Long businessId);

    @Query("SELECT s FROM Subscription s " +
    	       "WHERE s.businessOwner.id = :businessOwnerId " +
    	       "AND s.businessOwner.business.id = :businessId " +
    	       "AND s.isActive = true " +
    	       "ORDER BY s.startDate DESC")
    	Optional<Subscription> findActiveSubscriptionByBusinessOwnerIdAndBusinessId(
    	        @Param("businessOwnerId") Long businessOwnerId,
    	        @Param("businessId") Long businessId);


}
