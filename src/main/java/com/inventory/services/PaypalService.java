package com.inventory.services;

import com.inventory.entities.Subscription;
import com.inventory.entities.SubscriptionPlan;
import com.inventory.entities.BusinessOwner;
import com.inventory.entities.User;
import com.inventory.enums.PlanType;
import com.inventory.enums.SubscriptionLevel;
import com.inventory.repositories.PlanRepository;
import com.inventory.repositories.SubscriptionRepository;
import com.inventory.repositories.BusinessOwnerRepository;
import com.inventory.repositories.UserRepository;

import com.paypal.api.payments.*;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.PayPalRESTException;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaypalService {
    private final APIContext apiContext;
    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final BusinessOwnerRepository businessOwnerRepository;
    private final UserRepository userRepository;

    public PaypalService(
            APIContext apiContext,
            SubscriptionRepository subscriptionRepository,
            PlanRepository planRepository,
            BusinessOwnerRepository businessOwnerRepository,
            UserRepository userRepository) {
        this.apiContext = apiContext;
        this.subscriptionRepository = subscriptionRepository;
        this.planRepository = planRepository;
        this.businessOwnerRepository = businessOwnerRepository;
        this.userRepository = userRepository;
    }

    public Payment createPayment(
            Double total,
            String currency,
            String method,
            String intent,
            String description,
            String cancelUrl,
            String successUrl) throws PayPalRESTException {

        Amount amount = new Amount();
        amount.setCurrency(currency);
        amount.setTotal(String.format("%.2f", total));

        Transaction transaction = new Transaction();
        transaction.setDescription(description);
        transaction.setAmount(amount);

        List<Transaction> transactions = List.of(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod(method);

        Payment payment = new Payment();
        payment.setIntent(intent);
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelUrl);
        redirectUrls.setReturnUrl(successUrl);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    public Payment executePayment(String paymentId, String payerId) throws PayPalRESTException {
        Payment payment = new Payment();
        payment.setId(paymentId);

        PaymentExecution paymentExecution = new PaymentExecution();
        paymentExecution.setPayerId(payerId);

        return payment.execute(apiContext, paymentExecution);
    }

    public void extendSubscription(Long businessOwnerId, Long businessId, Long planId) {
        Subscription subscription = subscriptionRepository
                .findActiveSubscriptionByBusinessOwnerIdAndBusinessId(businessOwnerId, businessId)
                .orElseThrow(() -> new RuntimeException(
                        "No active subscription found for businessOwnerId: " + businessOwnerId + " and businessId: " + businessId));

        LocalDate currentEndDate = subscription.getEndDate() != null
                ? subscription.getEndDate()
                : LocalDate.now();

        LocalDate newEndDate = currentEndDate.isAfter(LocalDate.now())
                ? currentEndDate.plusDays(30)
                : LocalDate.now().plusDays(30);

        SubscriptionPlan newPlan = planRepository.findById(planId)
                .orElseThrow(() -> new RuntimeException("Subscription plan not found"));

        subscription.setPlan(newPlan);
        subscription.setEndDate(newEndDate);
        subscription.setActive(true);

        BusinessOwner owner = subscription.getBusinessOwner();
        SubscriptionLevel newLevel = switch (newPlan.getPlanType()) {
            case BASIC -> SubscriptionLevel.FREE;
            case PREMIUM -> SubscriptionLevel.PAID;
            case ENTERPRISE -> SubscriptionLevel.PREMIUM;
        };

        owner.setSubscriptionPlan(newLevel);
        businessOwnerRepository.save(owner);

        List<User> users = userRepository.findByBusinessId(owner.getBusiness().getId());
        for (User user : users) {
            user.setSubscriptionPlan(newLevel.toDbValue());
            user.setPlanStart(LocalDate.now());
            user.setPlanEnd(newEndDate);
            userRepository.save(user);
        }

        subscriptionRepository.save(subscription);
    }

}
