package com.example.notificationmicro.controller;


import com.example.notificationmicro.dto.NotificationDTO;
import com.example.notificationmicro.model.Notification;
import com.example.notificationmicro.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    // This saves the notificationDTO to the notification DTO with the newly added comment's info, the associated post, and who that post belongs to.
    // By having it saved to the Notification DB, we can then query the DB by user (who the post belongs to) to notify a user of comments on their post.
    @PostMapping
    public ResponseEntity<?> addNotification (@RequestBody NotificationDTO notificationDTO) {
        Notification newNotificationToSend = notificationService.createNotification(notificationDTO);
        if (newNotificationToSend == null) {
            return ResponseEntity.badRequest().body("Unable to save notification to be sent");
        }
        return new ResponseEntity<>(newNotificationToSend, HttpStatus.CREATED);
    }

   // To query notifications that belong to a user to display on the front end as their list of notifications
    @GetMapping ("{userId}")
    public  ResponseEntity<?> getNotificationByUserId (@PathVariable Integer userId) {
        List<Notification> notificationsFromUser = notificationService.getNotificationFromUser(userId);
        if (notificationsFromUser == null) {
            return ResponseEntity.badRequest().body("Unable to get notification");
        }
        return new ResponseEntity<>(notificationsFromUser, HttpStatus.OK);
    }
}
