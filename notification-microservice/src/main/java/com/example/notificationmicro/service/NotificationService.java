package com.example.notificationmicro.service;

import com.example.notificationmicro.dto.NotificationDTO;
import com.example.notificationmicro.model.Notification;
import com.example.notificationmicro.repository.INotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotificationService {
    @Autowired
    RestTemplate restTemplate;


    @Autowired
    INotificationRepository iNotificationRepository;


    public Notification createNotification(NotificationDTO notificationDTO) {
        // We need to map the Notification DTO to a notification object
        // We start that mapping by creating an instance to represent our newly created notification
        Notification notificationCreated = new Notification();
        // We write the notification DTO info back to this instance of notification,so we can save it in the database
        notificationCreated.setPostId(notificationDTO.getPostId());
        notificationCreated.setUserId(notificationDTO.getUserId());
        notificationCreated.setCommentId(notificationDTO.getCommentId());
        notificationCreated.setCommentText(notificationDTO.getCommentText());
        notificationCreated.setCommentedByUsername(notificationDTO.getCommentedByUsername());
        notificationCreated.setTimeCommentCreated(notificationDTO.getTimeCommentCreated());

        return iNotificationRepository.save(notificationCreated);

    }

    public List<Notification> getNotificationFromUser(Integer userId) {
        return iNotificationRepository.findNotificationsByUserId(userId).orElseThrow(() -> new RuntimeException("Notifications from userId " + userId + " does not exist."));
    }
}
