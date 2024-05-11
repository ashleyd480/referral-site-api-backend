package com.example.notificationmicro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class NotificationDTO {

    private Integer notificationId;
    private Integer postId;
    private Integer userId;
    private Integer commentId;
    private String commentText;
    private String commentedByUsername;
    private LocalDateTime timeCommentCreated;

}

// Notice how it's just annotated with @Data. A DTO is simply a courier of data.

// The attributes listed are so that we get the rest of comment info as well as associated post for context
// We have the user id too of who wrote the post to identify who needs to be notified is a comment is left on that post