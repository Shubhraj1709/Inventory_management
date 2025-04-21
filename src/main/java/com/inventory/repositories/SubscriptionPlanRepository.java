package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
}
