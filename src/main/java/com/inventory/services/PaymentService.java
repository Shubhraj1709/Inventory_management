package com.inventory.services;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PaymentService {
    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    public String createPaymentOrder(BigDecimal amount) {
        try {
            RazorpayClient razorpay = new RazorpayClient(keyId, keySecret);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100))); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.orders.create(orderRequest);
            return order.get("id");
        } catch (RazorpayException e) {
            throw new RuntimeException("Error creating payment order: " + e.getMessage());
        }
    }
}

