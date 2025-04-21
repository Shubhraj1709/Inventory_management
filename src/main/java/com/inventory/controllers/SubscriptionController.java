package com.inventory.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.inventory.entities.Subscription;
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

    @PostMapping("/create/{businessId}/{planId}")
    public ResponseEntity<Subscription> createSubscription(@PathVariable Long businessId, @PathVariable Long planId) {
        return ResponseEntity.ok(subscriptionService.createSubscription(businessId, planId));
    }

    @GetMapping("/business/{businessId}")
    public ResponseEntity<Optional<Subscription>> getSubscription(@PathVariable Long businessId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByBusiness(businessId));
    }

    @PostMapping("/pay/{amount}")
    public ResponseEntity<String> createPayment(@PathVariable BigDecimal amount) {
        return ResponseEntity.ok(paymentService.createPaymentOrder(amount));
    }
}
