package com.example.springreferallmain.controller;
import com.example.springreferallmain.model.Comment;
import com.example.springreferallmain.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping ("/comments")
public class CommentController {
    @Autowired
    CommentService commentService;


    // ------- Creating --------
    // Add a new comment to a post
    // Note: in the service method, we included the logic of creating a notification (since a notification is created when a post is made)
    @PostMapping("posts/{postId}")
    public ResponseEntity<?> addCommentToAPost(@PathVariable Integer postId, @RequestBody Comment commentToAdd) {

        if (commentToAdd == null || commentToAdd.getCommentText().isEmpty()) {
            return ResponseEntity.badRequest().body("Tsk tsk. Null or empty comment provided");
        }
        else {
            Comment newComment = commentService.addCommentToAPost(postId, commentToAdd);

            return ResponseEntity.status(HttpStatus.CREATED).body(newComment);
        }
    }


    // ------- RETRIEVE --------
    // Get comments tied to a post (to display comments under a post for user)
    @GetMapping("posts/{postId}")
    public ResponseEntity<List<Comment>> getCommentsByPostId(@PathVariable Integer postId) {
        List<Comment> commentsTiedToPost = commentService.getCommentsByPostId(postId);
        if (commentsTiedToPost == null || commentsTiedToPost.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(commentsTiedToPost);
    }


    // ------- UPDATE --------
    // Update an existing comment for post
    @PutMapping("{commentId}/posts/{postId}")
    public ResponseEntity<?> updateCommentForPost(@PathVariable Integer postId, @PathVariable Integer commentId, @RequestBody Comment editedComment) {
        Comment updatedCommentOnPost = commentService.updateCommentForPost(postId, commentId, editedComment);
        if (editedComment.getCommentText().isEmpty()) {
            return ResponseEntity.badRequest().body("Tsk tsk. Null or empty comment provided");
        }
        return ResponseEntity.ok(updatedCommentOnPost);
    }


    // ------- DELETE--------
    // Delete comment by id
    @DeleteMapping("{commentId}")
    public ResponseEntity<?> deleteCommentById(@PathVariable Integer commentId) {
        boolean deleted = commentService.deleteCommentById(commentId);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body("Comment deleted successfully.");
    }
}




