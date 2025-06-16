package com.inventory.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.SubscriptionPlan;

public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {

	Optional<SubscriptionPlan> findByPlanType(com.inventory.enums.PlanType planType);

}
