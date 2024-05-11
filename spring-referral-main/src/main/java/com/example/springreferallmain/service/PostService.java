package com.example.springreferallmain.service;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IPostRepository;
import com.example.springreferallmain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    IPostRepository iPostRepository;

    @Autowired
    IUserRepository iUserRepository;


    // ------- CREATE --------
    // Create a new post

    public Post addPost(Integer userId, Post postToAdd) {
        User existingUser = iUserRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with id " + userId + " does not exist." ));
        postToAdd.setUser(existingUser);
        return iPostRepository.save(postToAdd);
    }


    // Create a list of posts for testing
    public List<Post> addPosts(List<Post> postsToAdd) {
        return iPostRepository.saveAll(postsToAdd);
    }

    // ------- RETRIEVE --------
    // Get all posts (to display on homepage)
    public List<Post> getAllPosts() {
        return iPostRepository.findAll();
    }


    // Get all posts matching a keyword search, i.e. so users can see matching refer-all posts
    public List<Post> getPostsByDescriptionContaining(String keyword) {
        return iPostRepository.findPostsByPostContentContaining (keyword).orElseThrow(()-> new RuntimeException("Post with keyword " + keyword + " does not exist." ));
    }


    // ------- UPDATE --------
    // Update post by id (if user edits a post)
    public Post updatePostById(Integer postId, Post editedPost) {
        // Let's find the post we want to update first.
        Post existingPost = iPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("User with id " + postId + " does not exist."));

        // Now let's update that post.
        existingPost.setPostTitle(editedPost.getPostTitle());
        existingPost.setPostContent(editedPost.getPostContent());
        existingPost.setNumberLikesOnPost(editedPost.getNumberLikesOnPost());
        existingPost.setPostMediaURL(editedPost.getPostMediaURL());

        // Save the updated post
        return iPostRepository.save(existingPost);

    }


    // ------- DELETE--------
    // Delete post by id (if user deletes a post)
    public boolean deletePostById(Integer postId) {
        iPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post with id " + postId + " does not exist."));
        iPostRepository.deleteById(postId);
        //if the post no longer exists (true), it's no longer present!
        return !iPostRepository.findById(postId).isPresent();
    }


}
