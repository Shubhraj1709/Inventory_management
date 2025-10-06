package com.inventory.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.inventory.entities.SubscriptionPlan;

@Repository
public interface PlanRepository extends JpaRepository<SubscriptionPlan, Long> {
}
