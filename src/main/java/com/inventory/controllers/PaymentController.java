package com.inventory.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inventory.services.RazorpayService;
import com.razorpay.RazorpayException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final RazorpayService razorpayService;

    @PostMapping("/create-order")
    public ResponseEntity<String> createPayment(@RequestParam double amount) {
        try {
            String order = razorpayService.createOrder(amount, "INR", "txn_" + System.currentTimeMillis());
            return ResponseEntity.ok(order);
        } catch (RazorpayException e) {
        	e.printStackTrace();
            return ResponseEntity.status(500).body("Payment creation failed: " + e.getMessage());
        }
    }
}
