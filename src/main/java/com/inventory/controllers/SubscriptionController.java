package com.inventory.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventory.dto.SubscriptionDTO;
import com.inventory.entities.Subscription;
import com.inventory.security.SubscriptionGuard;
import com.inventory.services.PaymentService;
import com.inventory.services.SubscriptionService;

import java.math.BigDecimal;
import java.util.Optional;

@RestController
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subscriptionService;
    private final PaymentService paymentService;

    public SubscriptionController(SubscriptionService subscriptionService, PaymentService paymentService) {
        this.subscriptionService = subscriptionService;
        this.paymentService = paymentService;
    }

    @PostMapping("/create/{businessId}/{planId}")//business owner id passed 
    public ResponseEntity<Subscription> createSubscription(@PathVariable Long businessId, @PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionService.createSubscription(businessId, planId));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<SubscriptionDTO> getSubscription(@PathVariable Long businessId) {
        Subscription subscription = subscriptionService.getSubscriptionByBusiness(businessId);

        if (subscription == null) {
            return ResponseEntity.notFound().build();
        }

        SubscriptionDTO dto = new SubscriptionDTO(
            subscription.getId(),
            subscription.getPlan() != null ? subscription.getPlan().getName() : null,
            subscription.getPlan() != null ? subscription.getPlan().getPrice() : null,
            subscription.getStartDate(),
            subscription.getEndDate(),
            subscription.isActive()
        );

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/pay/{amount}")
    public ResponseEntity<String> createPayment(@PathVariable BigDecimal amount) {
        return ResponseEntity.ok(paymentService.createPaymentOrder(amount));
    }
    
    @GetMapping("/some-premium-resource/{businessOwnerId}")
    public ResponseEntity<?> getPremiumFeature(@PathVariable("businessOwnerId") Long businessOwnerId) {
        Subscription subscription = subscriptionService.getSubscriptionByBusiness(businessOwnerId)
            ;

        if (!SubscriptionGuard.hasAccess("PREMIUM", subscription)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Upgrade to access this feature.");
        }

        // ✅ Premium logic here
        return ResponseEntity.ok("You have access to premium features!");
    }
    
    @PatchMapping("/expire-now/{id}")
    public ResponseEntity<?> manuallyExpireSubscription(@PathVariable Long id) {
        boolean expired = subscriptionService.expireSubscriptionById(id);

        if (!expired) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Subscription not found or already expired.");
        }

        return ResponseEntity.ok("Subscription expired successfully.");
    }
    
    @PatchMapping("/simulate-payment-success/{businessOwnerId}")
    public ResponseEntity<?> simulatePaymentSuccess(@PathVariable Long businessOwnerId) {
        try {
            subscriptionService.handleSuccessfulPayment(businessOwnerId);
            return ResponseEntity.ok("✅ Payment successful. Subscription upgraded.from free to paid ");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("❌ " + e.getMessage());
        }
    }

    
    

}
