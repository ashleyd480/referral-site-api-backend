package com.example.springreferallmain.repository;

import com.example.springreferallmain.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IPostRepository extends JpaRepository<Post, Integer> {

    // Find posts that contain keyword
    Optional <List<Post>> findPostsByPostContentContaining(String keyword);


}
