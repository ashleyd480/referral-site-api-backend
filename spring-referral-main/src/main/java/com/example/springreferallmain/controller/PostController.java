package com.example.springreferallmain.controller;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.service.CommentService;
import com.example.springreferallmain.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping ("posts")
public class PostController {
    @Autowired
    PostService postService;

    @Autowired
    CommentService commentService;


    // ------- CREATE --------
    // Create a new post
    // Error handling built in to ensure user adds postTitle and postContent.
    @PostMapping("{userId}")
    public ResponseEntity<?> addPost(@PathVariable Integer userId, @RequestBody Post postToAdd) {
        if (postToAdd.getPostTitle().isEmpty() || postToAdd.getPostContent().isEmpty())  {
            return ResponseEntity.badRequest().body("Please make sure you provide a post title and content before submitting.");
        }
        else {
            Post newPostCreated = postService.addPost(userId, postToAdd);

            return new ResponseEntity<>(newPostCreated, HttpStatus.CREATED);
        }
    }

    // Create a list of posts for testing
    @PostMapping
    public ResponseEntity<?> addPosts(@RequestBody List<Post> postsToAdd) {
        List<Post> newPostsCreated = postService.addPosts(postsToAdd);
        if (postsToAdd == null || postsToAdd.isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null posts provided");
        }
        return new ResponseEntity<>(newPostsCreated, HttpStatus.CREATED);
    }

    // ------- RETRIEVE --------
    // Get all posts (to display on homepage)
    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        List<Post> allPosts = postService.getAllPosts();
        if (allPosts.isEmpty() ) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(allPosts);
    }

    // Get all posts matching a keyword search, i.e. so users can see matching refer-all posts
    @GetMapping(params= {"keyword"})
    public ResponseEntity<List<Post>> getPostsByDescriptionContaining(@RequestParam String keyword) {
        List<Post> postsMatchingKeyword = postService.getPostsByDescriptionContaining(keyword);
        if (postsMatchingKeyword == null ||postsMatchingKeyword.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postsMatchingKeyword);
    }

    // Note: Since this a social media website, where users view a timeline of posts of homepage, there's no business reason to get posts by userId

    // ------- UPDATE --------
    // Update post by id (if user edits a post)
    @PutMapping("{postId}")
    public ResponseEntity<?> updatePostById(@PathVariable Integer postId, @RequestBody Post editedPost) {
        Post updatedPost = postService.updatePostById(postId, editedPost);
        if (editedPost.getPostTitle().isEmpty() || editedPost.getPostContent().isEmpty())  {
            return ResponseEntity.badRequest().body("Please make sure you provide a post title and content before submitting.");
        }
        return ResponseEntity.ok(updatedPost);
    }

    // ------- DELETE--------
    // Delete post by id (if user deletes a post)
    @DeleteMapping("{postId}")
    public ResponseEntity<?> deletePostById(@PathVariable Integer postId) {
        boolean deleted = postService.deletePostById(postId);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body("Post deleted successfully.");
    }


}
