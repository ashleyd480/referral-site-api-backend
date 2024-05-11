package com.example.notificationmicro.repository;

import com.example.notificationmicro.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface INotificationRepository extends JpaRepository<Notification, Integer> {
   // custom query method to find notifications by user id

   Optional<List<Notification>> findNotificationsByUserId (Integer userId);
}
