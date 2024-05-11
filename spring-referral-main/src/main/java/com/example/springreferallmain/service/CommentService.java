package com.example.springreferallmain.service;

import com.example.springreferallmain.dto.NotificationDTO;
import com.example.springreferallmain.model.Comment;
import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.repository.ICommentRepository;
import com.example.springreferallmain.repository.IPostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import java.util.List;
@CrossOrigin(origins = "*")
@Service
public class CommentService {
    @Autowired
    ICommentRepository iCommentRepository;

    @Autowired
    IPostRepository iPostRepository;

    // Autowire my Rest Template to make call to Notification microservice

    @Autowired
    RestTemplate restTemplate;



    // ------- Creating --------
    // Add a new comment to a post

    public Comment addCommentToAPost(Integer postId, Comment commentToAdd) {
        // Let's find the post first
        Post existingPost = iPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post with id " + postId + " does not exist."));

        // Associate the comment with the post
        commentToAdd.setPost(existingPost);

        // Add the comment to the post's list of comments
        // We need to set comment to post and post to the comments, b/c of unidirectional relationship.
        existingPost.getComments().add(commentToAdd);

        // After comment is added to post and saved to DB, we send a notification by making a post request to Notification
        // We need to ensure the comment is saved, so we can call the getters on it to write its info to Notification DTO

        Comment commentSaved = iCommentRepository.save(commentToAdd);

        // We create new notificationDTO instance to represent the notification for this specific comment
        // We write the info using getters and set it to the notification DTO

        NotificationDTO notificationDTO = getNotificationDTO(postId, existingPost, commentSaved);

        restTemplate.postForEntity("http://localhost:8081/notifications",
                notificationDTO, // parameter passed to Notification
                NotificationDTO.class); // return type (b/c required for syntax, although we aren't returning anything since we are simply doing a Post call)


        // Return the comment the user added
        return commentSaved;


    }

    private static NotificationDTO getNotificationDTO(Integer postId, Post existingPost, Comment commentSaved) {
        NotificationDTO notificationDTO = new NotificationDTO();
        notificationDTO.setPostId(postId);
        notificationDTO.setUserId(existingPost.getUser().getUserId());
        notificationDTO.setCommentId(commentSaved.getCommentId());
        notificationDTO.setCommentText(commentSaved.getCommentText());
        notificationDTO.setCommentedByUsername(commentSaved.getCommentedByUsername());
        notificationDTO.setTimeCommentCreated(commentSaved.getTimeCommentCreated());
        return notificationDTO;
    }



    // ------- RETRIEVE --------
    // Get comments tied to a post (to display comments under a post for user)

    public List<Comment> getCommentsByPostId(Integer postId) {
        return iCommentRepository.findCommentByPostPostId(postId).orElseThrow(() -> new RuntimeException("Post with id " + postId + " does not exist."));
    }

    // ------- UPDATE --------
    // Update an existing comment for post
    public Comment updateCommentForPost(Integer postId, Integer commentId, Comment editedComment) {
        // Let's find the comment and post first to make sure they exist (i.e. to prevent a user from updating a comment on a post that no longer exists).
        Comment existingComment = iCommentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment with id " + commentId + " does not exist."));

        Post existingPost = iPostRepository.findById(postId).orElseThrow(() -> new RuntimeException("Post with id " + postId + " does not exist."));


        // Update the fields of the existing comment with the edited values
        existingComment.setCommentText(editedComment.getCommentText());
        existingComment.setNumberLikesOnComment(editedComment.getNumberLikesOnComment());
        existingComment.setCommentedByUsername(editedComment.getCommentedByUsername());

        // Save updated comment
        return iCommentRepository.save(existingComment);

        // Since we're updating an existing comment that is already associated with a post, we don't need to explicitly re-associate the comment with the post.

    }


    // ------- DELETE--------
    // Delete comment by id for post

    public boolean deleteCommentById(Integer commentId) {

        iCommentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("Comment with id " + commentId + " does not exist."));

        iCommentRepository.deleteById(commentId);
        return !iCommentRepository.findById(commentId).isPresent();

    }
}


