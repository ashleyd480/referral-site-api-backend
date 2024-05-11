package com.example.springreferallmain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer commentId;
    private String commentText;
    private Integer numberLikesOnComment;
    private String commentedByUsername;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime timeCommentCreated;

    @JsonIgnore
    @ManyToOne
    @JoinColumn (name = "post_id", nullable = false)
    private Post post;

}


// ------- Many to One with Posts --------
// Many comments can belong to one post.
// @JoinColumn: post_id is the foreign key that references the postId from Post. Each comment needs to be tied to a post (nullable= false).
// ^Hibernate then associates the post with that post_id to its respective comment(s).

// @JsonIgnore: We can view comments without needing to see the post, vs it would be helpful to see the post and affiliated comments.