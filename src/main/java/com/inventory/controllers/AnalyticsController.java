package com.inventory.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.entities.Subscription;
import com.inventory.security.SubscriptionGuard;
import com.inventory.services.SubscriptionService;
import com.inventory.utils.TokenUtils;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final SubscriptionService subscriptionService;
    private final TokenUtils tokenUtils; // ✅ Add this


    @GetMapping("/check")
    @PreAuthorize("hasRole('BUSINESS_OWNER')")
    public ResponseEntity<?> checkAnalyticsAccess(@RequestHeader("Authorization") String authHeader) {
        Long businessOwnerId = tokenUtils.extractBusinessOwnerIdFromToken(authHeader);

        Subscription subscription = subscriptionService.getSubscriptionByBusiness(businessOwnerId);

        if (!SubscriptionGuard.hasAccess("PREMIUM", subscription)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Upgrade to Premium Plan to view analytics");
        }

        return ResponseEntity.ok("Allowed to view analytics!");
    }
}
