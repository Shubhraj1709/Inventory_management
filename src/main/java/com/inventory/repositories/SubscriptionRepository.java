package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Subscription;

import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    Optional<Subscription> findByBusinessOwnerId(Long businessOwnerId); // ✅ Add this method
}
