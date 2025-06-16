package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.schedulers.SubscriptionScheduler;
import com.inventory.services.SubscriptionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class SubscriptionTestController {

    private final SubscriptionScheduler scheduler;
    private final SubscriptionService subscriptionService;


    @GetMapping("/run-subscription-check")
    public ResponseEntity<String> testScheduler() {
        scheduler.checkSubscriptionStatus(); // manually call the method
        return ResponseEntity.ok("Subscription check executed");
    }
    
    @GetMapping("/check-expiry")
    public ResponseEntity<String> manuallyRunExpiryCheck() {
        subscriptionService.checkSubscriptionExpiry();
        return ResponseEntity.ok("Subscription expiry check completed!");
    }
}
