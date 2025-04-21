package com.inventory.services;



import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.inventory.entities.Business;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Subscription;
import com.inventory.entities.SubscriptionPlan;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.BusinessRepository;
import com.inventory.repositories.SubscriptionPlanRepository;
import com.inventory.repositories.SubscriptionRepository;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final BusinessOwnerRepository businessOwnerRepository; // ✅ Update to BusinessOwnerRepository

    public SubscriptionService(SubscriptionRepository subscriptionRepository,
            SubscriptionPlanRepository subscriptionPlanRepository,
            BusinessOwnerRepository businessOwnerRepository) { // ✅ Inject repository
this.subscriptionRepository = subscriptionRepository;
this.subscriptionPlanRepository = subscriptionPlanRepository;
this.businessOwnerRepository = businessOwnerRepository;
}

    public Subscription createSubscription(Long businessOwnerId, Long planId) {
        BusinessOwner businessOwner = businessOwnerRepository.findById(businessOwnerId)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Subscription subscription = new Subscription(); // ✅ Declare subscription before using it
        subscription.setBusinessOwner(businessOwner); // ✅ Use businessOwner instead of business
        subscription.setPlan(plan);
        subscription.setStartDate(LocalDate.now());
        subscription.setEndDate(LocalDate.now().plusMonths(1)); // 1-month subscription
        subscription.setActive(true);

        return subscriptionRepository.save(subscription);
    }

    public Optional<Subscription> getSubscriptionByBusiness(Long businessOwnerId) {
        return subscriptionRepository.findByBusinessOwnerId(businessOwnerId);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Runs daily at midnight
    public void checkSubscriptionExpiry() {
        LocalDate today = LocalDate.now();
        subscriptionRepository.findAll().forEach(subscription -> {
            if (subscription.getEndDate().isBefore(today)) {
                subscription.setActive(false);
                subscriptionRepository.save(subscription);
                System.out.println("Subscription expired for Business: " + subscription.getBusinessOwner().getName());
            }
        });
    }
}
