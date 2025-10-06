package com.inventory.services;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inventory.entities.BusinessOwner;
import com.inventory.entities.Subscription;
import com.inventory.entities.SubscriptionPlan;
import com.inventory.entities.User;
import com.inventory.enums.PlanType;
import com.inventory.enums.SubscriptionLevel;
import com.inventory.enums.NotificationType;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.PlanRepository;
import com.inventory.repositories.SubscriptionPlanRepository;
import com.inventory.repositories.SubscriptionRepository;
import com.inventory.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionPlanRepository subscriptionPlanRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    
    private final PlanRepository planRepository;


    public Subscription createSubscription(Long businessOwnerId, Long planId) {
        BusinessOwner businessOwner = businessOwnerRepository.findById(businessOwnerId)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        SubscriptionPlan plan = subscriptionPlanRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        SubscriptionLevel level = switch (plan.getPlanType()) {
            case BASIC -> SubscriptionLevel.FREE;
            case PREMIUM -> SubscriptionLevel.PAID;
            case ENTERPRISE -> SubscriptionLevel.PREMIUM;
        };

        businessOwner.setSubscriptionPlan(level);
        businessOwner.setActive(true); // ✅ ensure active when subscription is created

        businessOwnerRepository.save(businessOwner);

        LocalDate planStart = LocalDate.now();
        LocalDate planEnd = planStart.plusMonths(1);

        List<User> users = userRepository.findByBusinessId(businessOwner.getBusiness().getId());
        for (User user : users) {
            user.setSubscriptionPlan(level.toDbValue());
            user.setPlanStart(planStart);
            user.setPlanEnd(planEnd);
        }
        userRepository.saveAll(users);

        Subscription subscription = new Subscription();
        subscription.setBusinessOwner(businessOwner);
        subscription.setPlan(plan);
        subscription.setStartDate(planStart);
        subscription.setEndDate(planEnd);
        subscription.setActive(true);

        return subscriptionRepository.save(subscription);
    }

    public Subscription getSubscriptionByBusiness(Long businessOwnerId) {
        List<Subscription> subscriptions = subscriptionRepository.findByBusinessOwnerId(businessOwnerId);
        return subscriptions.stream()
                .filter(Subscription::isActive)
                .max((a, b) -> b.getStartDate().compareTo(a.getStartDate()))
                .orElse(null);
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public List<String> checkSubscriptionExpiry() {
        LocalDate today = LocalDate.now();
        List<String> downgradedUsers = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for (Subscription subscription : subscriptions) {
            BusinessOwner owner = subscription.getBusinessOwner();
            boolean wasActive = subscription.isActive();

            if (subscription.getEndDate().isBefore(today)) {
                if (wasActive) {
                    subscription.setActive(false);
                    owner.setSubscriptionPlan(SubscriptionLevel.FREE);
                    owner.setActive(false); // ✅ deactivate business owner too

                    List<User> users = userRepository.findByBusinessId(owner.getBusiness().getId());
                    for (User user : users) {
                        user.setSubscriptionPlan(SubscriptionLevel.FREE.toDbValue());
                        user.setPlanStart(null);
                        user.setPlanEnd(null);
                        userRepository.save(user);
                        downgradedUsers.add(user.getEmail());
                    }

                    notificationService.createNotification(
                        "Subscription expired for business owner: " + owner.getEmail(),
                        NotificationType.SUBSCRIPTION_RENEWAL,
                        owner.getId()
                    );
                }
            } else {
                if (!wasActive) {
                    subscription.setActive(true);
                    SubscriptionLevel level = switch (subscription.getPlan().getPlanType()) {
                        case BASIC -> SubscriptionLevel.FREE;
                        case PREMIUM -> SubscriptionLevel.PAID;
                        case ENTERPRISE -> SubscriptionLevel.PREMIUM;
                    };
                    owner.setSubscriptionPlan(level);

                    notificationService.createNotification(
                        "Subscription reactivated for business owner: " + owner.getEmail(),
                        NotificationType.SUBSCRIPTION_RENEWAL,
                        owner.getId()
                    );
                }
            }

            subscriptionRepository.save(subscription);
            businessOwnerRepository.save(owner);
        }

        return downgradedUsers;
    }

    public boolean expireSubscriptionById(Long id) {
        Optional<Subscription> subscriptionOpt = subscriptionRepository.findById(id);
        if (subscriptionOpt.isEmpty()) return false;

        Subscription subscription = subscriptionOpt.get();
        if (!subscription.isActive()) return false;

        subscription.setActive(false);
        subscription.setEndDate(LocalDate.now().minusDays(1));
        subscriptionRepository.save(subscription);

        return true;
    }

    public void handleSuccessfulPayment(Long businessOwnerId) {
        BusinessOwner owner = businessOwnerRepository.findById(businessOwnerId)
                .orElseThrow(() -> new RuntimeException("Business Owner not found"));

        Subscription subscription = subscriptionRepository.findTopByBusinessOwnerIdAndIsActiveTrueOrderByStartDateDesc(businessOwnerId)
                .orElseThrow(() -> new RuntimeException("Active subscription not found"));

        PlanType currentPlanType = subscription.getPlan().getPlanType();

        if (currentPlanType == PlanType.BASIC) {
            SubscriptionPlan premiumPlan = subscriptionPlanRepository.findByPlanType(PlanType.PREMIUM)
                    .orElseThrow(() -> new RuntimeException("Premium plan not found"));

            subscription.setPlan(premiumPlan);
            subscription.setStartDate(LocalDate.now());
            subscription.setEndDate(LocalDate.now().plusMonths(1));
            subscription.setActive(true);
            subscriptionRepository.save(subscription);

            SubscriptionLevel newLevel = SubscriptionLevel.PAID;
            owner.setSubscriptionPlan(newLevel);
            owner.setActive(true); // ✅ mark as active after payment

            businessOwnerRepository.save(owner);

            List<User> users = userRepository.findByBusinessId(owner.getBusiness().getId());
            for (User user : users) {
                user.setSubscriptionPlan(newLevel.toDbValue());
                user.setPlanStart(LocalDate.now());
                user.setPlanEnd(LocalDate.now().plusMonths(1));
                userRepository.save(user);
            }

            notificationService.createNotification(
                    "Payment received: upgraded to PREMIUM for business owner: " + owner.getEmail(),
                    NotificationType.SUBSCRIPTION_RENEWAL,
                    owner.getId()
            );
        }
    }

    
    @Transactional
    public Subscription extendSubscription(Long businessOwnerId, Long businessId, Long planId) {
        SubscriptionPlan plan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Plan not found"));

        Subscription subscription = subscriptionRepository
                .findTopByBusinessOwnerIdAndIsActiveTrueOrderByStartDateDesc(businessOwnerId)
                .orElse(null);

        LocalDate startDate;
        if (subscription != null && subscription.isActive() && subscription.getEndDate().isAfter(LocalDate.now())) {
            // Extend existing subscription
            startDate = subscription.getEndDate().plusDays(1);
        } else {
            // Create a new subscription
            subscription = new Subscription();
            BusinessOwner owner = businessOwnerRepository.findById(businessOwnerId)
                    .orElseThrow(() -> new RuntimeException("BusinessOwner not found"));
            subscription.setBusinessOwner(owner);

            startDate = LocalDate.now();
        }

        subscription.setPlan(plan);
        subscription.setStartDate(startDate);
        subscription.setEndDate(startDate.plusMonths(1)); // Or plan.getDuration() if you support different lengths
        subscription.setActive(true);

        BusinessOwner owner = subscription.getBusinessOwner();
        owner.setActive(true); // ✅ ensure owner is marked active
        businessOwnerRepository.save(owner);
        
        return subscriptionRepository.save(subscription);
    }

    
    public Subscription getLatestSubscriptionByBusinessId(Long businessId) {
        List<Subscription> subscriptions = subscriptionRepository.findActiveSubscriptionsByBusinessId(businessId);
        if (subscriptions.isEmpty()) {
            throw new RuntimeException("No active subscription found for business ID: " + businessId);
        }
        return subscriptions.get(0); // Return the most recent active subscription
    }

    public Subscription getLatestOrActiveSubscription(Long businessOwnerId) {
        return subscriptionRepository.findTopByBusinessOwnerIdAndIsActiveTrueOrderByStartDateDesc(businessOwnerId)
                .or(() -> subscriptionRepository.findTopByBusinessOwnerIdOrderByStartDateDesc(businessOwnerId))
                .orElse(null);
    }


}


