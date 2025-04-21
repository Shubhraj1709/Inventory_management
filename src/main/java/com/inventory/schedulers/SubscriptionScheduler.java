package com.inventory.schedulers;

import com.inventory.entities.Business;
import com.inventory.enums.PaymentStatus;
import com.inventory.repositories.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionScheduler {

    private final BusinessRepository businessRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void checkSubscriptionStatus() {
        List<Business> businesses = businessRepository.findAll();
        LocalDate today = LocalDate.now();
        for (Business business : businesses) {
            if (business.getNextDueDate() != null && business.getNextDueDate().isBefore(today)) {
                business.setPaymentStatus(PaymentStatus.UNPAID);
            }
        }
        businessRepository.saveAll(businesses);
    }
}
