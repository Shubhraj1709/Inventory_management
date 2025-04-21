package com.inventory.services;

import com.inventory.entities.Notification;
import com.inventory.enums.NotificationType;
import com.inventory.repositories.NotificationRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createNotification(String message, NotificationType type, Long referenceId) {
        Notification notification = new Notification(null, message, false, LocalDateTime.now(), type, referenceId);
        notificationRepository.save(notification);
    }

    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByReadFalse();
    }

    public void markAsRead(Long id) {
        notificationRepository.findById(id).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Scheduled(fixedRate = 86400000) // Runs every 24 hours
    public void checkForPendingInvoices() {
        // Logic to fetch pending invoices and create notifications
        createNotification("Reminder: You have pending invoices!", NotificationType.PENDING_INVOICE, null);
    }
}
