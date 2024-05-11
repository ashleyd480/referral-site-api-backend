package com.example.notificationmicro.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data

public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer notificationId;
    private Integer postId;
    private Integer userId;
    private Integer commentId;
    private String commentText;
    private String commentedByUsername;
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime timeCommentCreated;

}


// The Notification entity mirrors attributes of the NotificationDTO. This is so that when we need to save the NotificationDTO instances to the database, we can convert them into an instance of the Notification entity first.