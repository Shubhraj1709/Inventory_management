package com.inventory.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.inventory.services.PermissionService;
import com.inventory.services.SubscriptionService;
import com.inventory.services.UserService;
import com.inventory.entities.User;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    private final PermissionService permissionService;
    private final SubscriptionService subscriptionService;
    private final UserService userService;

    @Autowired
    public UserController(PermissionService permissionService,
                          SubscriptionService subscriptionService,
                          UserService userService) {
        this.permissionService = permissionService;
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    @GetMapping("/me/permissions")
    public ResponseEntity<?> getPermissions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not authenticated");
        }

        String email = authentication.getName();
        Optional<User> userOpt = userService.getUserByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }

        User user = userOpt.get();
        String plan = user.getSubscriptionPlan() != null
                ? user.getSubscriptionPlan().toString()
                : "FREE"; // Fallback if null

        return ResponseEntity.ok("Subscription plan: " + plan);
    }

    @GetMapping("/subscriptions/cron-now")
    public ResponseEntity<?> testCron() {
        List<String> downgraded = subscriptionService.checkSubscriptionExpiry();

        if (downgraded.isEmpty()) {
            return ResponseEntity.ok("No expired subscriptions found.");
        }

        return ResponseEntity.ok("Downgraded users: " + String.join(", ", downgraded));
    }

}
