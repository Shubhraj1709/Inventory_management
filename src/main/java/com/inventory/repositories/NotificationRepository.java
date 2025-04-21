package com.inventory.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.inventory.entities.Notification;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByReadFalse();
}
