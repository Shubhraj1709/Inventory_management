package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Notification;
import com.inventory.enums.NotificationType;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReadFalse();
    
    List<Notification> findAllByOrderByTimestampDesc();
    
    List<Notification> findByTypeAndReadFalse(NotificationType type);

    List<Notification> findByTypeAndReferenceIdAndReadFalse(NotificationType type, Long referenceId);


}
