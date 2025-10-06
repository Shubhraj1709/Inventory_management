package com.inventory.controllers;

import com.inventory.dto.PaymentRequest;
import com.inventory.dto.SubscriptionDTO;
import com.inventory.entities.Subscription;
import com.inventory.entities.User;
import com.inventory.services.PaypalService;
import com.inventory.services.SubscriptionService;
import com.inventory.services.UserService;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/paypal")
public class PaypalController {

    private final PaypalService paypalService;
    private final UserService userService; // your existing service
    private final SubscriptionService subscriptionService;


    public PaypalController(PaypalService paypalService, UserService userService ,SubscriptionService subscriptionService) {
        this.paypalService = paypalService;
        this.userService = userService;
        this.subscriptionService= subscriptionService;
    }

    @PostMapping("/pay")
    public ResponseEntity<?> pay(@RequestBody PaymentRequest paymentRequest) {
        try {
            double amount = paymentRequest.getAmount();
            Long planId = paymentRequest.getPlanId();
            Long businessId = paymentRequest.getBusinessId();

            String cancelUrl = "http://localhost:3000/payment/cancel";
            String successUrl = "http://localhost:8080/api/paypal/success"
                    + "?planId=" + planId
                    + "&businessId=" + businessId
                    + "&businessOwnerId=" + paymentRequest.getBusinessOwnerId(); // ✅ add this


            Payment payment = paypalService.createPayment(
                    amount,
                    "USD",
                    "paypal",
                    "sale",
                    "Subscription Payment",
                    cancelUrl,
                    successUrl
            );

            for (com.paypal.api.payments.Links link : payment.getLinks()) {
                if (link.getRel().equals("approval_url")) {
                    return ResponseEntity.ok(link.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Error occurred during payment creation.");
    }

//    @GetMapping("/success")
//    public ResponseEntity<?> success(@RequestParam("paymentId") String paymentId,
//                                     @RequestParam("PayerID") String payerId,
//                                     @RequestParam("planId") Long planId,
//                                     @RequestParam("businessId") Long businessId) {
//        try {
//            Payment payment = paypalService.executePayment(paymentId, payerId);
//            if (payment.getState().equals("approved")) {
//                subscriptionService.extendSubscription(businessId, planId);
//
//                // Fetch updated subscription
//                Subscription updatedSubscription = subscriptionService
//                    .getLatestSubscriptionByBusinessId(businessId);
//
//                return ResponseEntity.ok(updatedSubscription);
//            }
//        } catch (PayPalRESTException e) {
//            e.printStackTrace();
//        }
//        return ResponseEntity.badRequest().body("❌ Payment failed.");
//    }

    @GetMapping("/success")
    public ResponseEntity<?> success(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,
            @RequestParam("planId") Long planId,
            @RequestParam("businessId") Long businessId,
            @RequestParam("businessOwnerId") Long businessOwnerId) {  // ✅ NEW param

        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);

            if (payment.getState().equals("approved")) {
                subscriptionService.extendSubscription(businessOwnerId, businessId, planId); // ✅ use new method

                Subscription subscription = subscriptionService.getLatestSubscriptionByBusinessId(businessId);

                SubscriptionDTO subscriptionDTO = new SubscriptionDTO(
                    subscription.getId(),
                    subscription.getPlan().getName(),
                    subscription.getPlan().getPrice(),
                    subscription.getStartDate(),
                    subscription.getEndDate(),
                    subscription.isActive()
                );

                return ResponseEntity.ok(subscriptionDTO);
            }
        } catch (PayPalRESTException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().body("Payment failed or subscription not updated");
    }



    @GetMapping("/cancel")
    public ResponseEntity<?> cancel() {
        return ResponseEntity.ok("Payment cancelled by user.");
    }
}
