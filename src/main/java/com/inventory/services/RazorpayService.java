package com.inventory.services;

import com.razorpay.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RazorpayService {

    @Value("${razorpay.key_id}")
    private String razorpayKey;

    @Value("${razorpay.key_secret}")
    private String razorpaySecret;

    public String createOrder(double amount, String currency, String receipt) throws RazorpayException {
        RazorpayClient client = new RazorpayClient(razorpayKey, razorpaySecret);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", (int)(amount * 100)); // Amount in paise
        orderRequest.put("currency", currency);
        orderRequest.put("receipt", receipt);

        Order order = client.orders.create(orderRequest);
        return order.toString(); // Send this order info to frontend for payment
    }
}
