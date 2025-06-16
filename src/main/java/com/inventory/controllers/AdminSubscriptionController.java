package com.inventory.controllers;

import com.inventory.dto.SubscriptionAssignmentDto;
import com.inventory.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminSubscriptionController {

//    private final UserService userService;
//
//    // Only admins can assign a subscription plan
//    @PreAuthorize("hasRole('ADMIN')")
//    @PutMapping("/assign-subscription/{userId}")
//    public ResponseEntity<?> assignSubscription(@PathVariable Long userId,
//                                                @RequestBody SubscriptionAssignmentDto dto) {
//        userService.assignSubscription(userId, dto);
//        return ResponseEntity.ok("Subscription assigned successfully!");
//    }
}
