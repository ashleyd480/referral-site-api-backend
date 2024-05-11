package com.example.springreferallmain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "\"User\"")
public class User {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String profilePictureURL;
    private String userBio;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime timeUserCreated;

    @JsonIgnore
    @OneToMany (mappedBy= "user")
    private List<Post> posts;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", nullable= false, unique= true)
    private Address address;
}

// ------- One to Many with Post --------
// One user can have multiple posts.
// mappedBy: Post entity owns the relationship
// list of posts mapped to each user by user field in Post.
// @JsonIgnore means I would see the user under the post

// -------  One to One with Address --------
// @OneToOne annotated above Address: Each user has one address.
//CascadeType.ALL: for any actions taken on User should be taken on Address; i.e. if User deleted, then we delete that Address
//@JoinColumn: address_id is the foreign key (referencing addressId from Address) and each user needs to have an address (nullable= false) that is unique