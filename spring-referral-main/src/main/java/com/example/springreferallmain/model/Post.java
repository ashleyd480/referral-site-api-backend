package com.example.springreferallmain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer postId;
    private String postTitle;
    private String postContent;
    private Integer numberLikesOnPost;
    private String postMediaURL;

    @Column(updatable = false)
    @CreationTimestamp // to auto generate time
    private LocalDateTime timePostCreated;

    // Many to One with User

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;


    // One to Many with Comments
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();


    // Many to Many with Tags
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "Tag_Posts",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> tags = new ArrayList<>();

}

// ------- Many to One with User --------
// Many posts can belong to one User.
// @JoinColumn: user_id is the foreign key that references the userId from User. Each post needs to have a userId (nullable= false)
//^Hibernate then associates the user with that user_id to its respective post(s).


// ------- One to Many with Comment --------
// A post can have many comments.
// mappedBy: Comment entity owns the relationship.
// list of Comments mapped to each post by post field in Comment.


// ------- Many to Many with Tags--------
// A post can have many tags.
// @JoinTable since we query more from posts, and when we do query, the related Tags will display.
// inverseJoinColumn: tag_id is the foreign key.

