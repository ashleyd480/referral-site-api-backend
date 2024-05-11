package com.example.springreferallmain.controller;

import com.example.springreferallmain.model.Comment;
import com.example.springreferallmain.service.CommentService;
import com.example.springreferallmain.service.CommentServiceTest;
import com.example.springreferallmain.testdata.TestData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(CommentController.class)
@WebMvcTest(CommentControllerTest.class)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;
    //wire dependency so we can simulate making those endpoint calls


    @MockBean
    private CommentService mockCommentService;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class

    private Comment testComment= TestData.makeTestComment1();
    private List<Comment> testComments= TestData.makeTestComments();

    private Comment emptyTestComment= TestData.makeEmptyTestComment();




// |           TESTING CREATE  | \\

//  Add a new comment to a post
    // addCommentToPost Happy Path 😊
    @Test
    public void testAddCommentToAPostPass () throws Exception {
        // Setup of mock method with "parameters"
        Integer testPostId = 1;

        // Convert the Comment object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String commentJson = objectMapper.writeValueAsString(testComment);


        when(mockCommentService.addCommentToAPost(testPostId, testComment)).thenReturn(testComment);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/comments/posts/{testPostId}", testPostId)
                .contentType(MediaType.APPLICATION_JSON) // Set content type
                .content(commentJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(commentJson)); // Response should be the same JSON as request

        //Verify the controller calls the service method
        verify(mockCommentService, times(1)).addCommentToAPost(testPostId, testComment);
    }


    // addCommentToPost Sad Path 😭

    @Test
    public void testAddCommentToAPostFail () throws Exception {
        // Setup of mock method with "parameters"
        Integer testPostId = 1;

        // Convert the Comment object to JSON format

        ObjectMapper objectMapper = new ObjectMapper();
        String commentJson = objectMapper.writeValueAsString(emptyTestComment);

        when(mockCommentService.addCommentToAPost(1, emptyTestComment)).thenReturn(null);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/comments/posts/{testPostId}", testPostId)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(commentJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null or empty comment provided")); // Expecting error message
    }



// |           TESTING RETRIEVE  | \\
// Get comments tied to a post (to display comments under a post for user)
    // getCommentsByPostId Happy Path 😊
    @Test
    public void testGetCommentsByPostIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer postId = 1;
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String commentJson = objectMapper.writeValueAsString(testComments);

        when(mockCommentService.getCommentsByPostId(postId)).thenReturn(testComments);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/comments/posts/{postId}", postId))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(commentJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockCommentService, times(1)).getCommentsByPostId(postId);
    }
    // getCommentsByPostId Sad Path 😭
    @Test
    public void testGetCommentsByPostIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer nonexistentPostId = 100;
        when(mockCommentService.getCommentsByPostId(nonexistentPostId)).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/address/users/{userId}", nonexistentPostId))
                .andExpect(status().isNotFound()); // Expecting status not found
    }


// |           TESTING UPDATE  | \\

// Update comment for post by id (i.e. user wants to edit comment details)
// updateCommentForPost Happy Path 😊
@Test
public void testUpdateCommentForPostPass() throws Exception {
    // Setup of mock method with "parameters"
    Integer postId = 1;
    Integer commentId = 1;
    // Convert the Comment object to JSON format
    ObjectMapper objectMapper = new ObjectMapper();
    String commentJson = objectMapper.writeValueAsString(testComment);

    when(mockCommentService.updateCommentForPost(postId, commentId, testComment)).thenReturn(testComment);

    // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
    mockMvc.perform(put("/comments/{commentId}/posts/{postId}", postId, commentId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(commentJson))
            .andExpect(status().isOk())  // Expecting status code 200
            .andExpect(content().json(commentJson)); // Response should be the same JSON as request

    // Verify the controller calls the service method
    verify(mockCommentService, times(1)).updateCommentForPost(postId, commentId, testComment);
}

    // updateCommentForPost Sad Path 😭
    @Test
    public void testUpdateCommentForPostFail() throws Exception {
        // Setup of mock method with parameters
        Integer postId = 1;
        Integer commentId = 1;
        when(mockCommentService.updateCommentForPost(postId, commentId, emptyTestComment)).thenReturn(null);

        // Convert the Comment object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String commentJson = objectMapper.writeValueAsString(emptyTestComment);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(put("/comments/{commentId}/posts/{postId}", postId, commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(commentJson))
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null or empty comment provided")); // Expecting error message
    }

// |           TESTING DELETE  | \\

// Delete comment by id

    // deleteCommentById Happy Path 😊
    @Test
    public void testDeleteCommentByIdPass () throws Exception {
        // Setup of mock method with "parameters"
        Integer commentId = 1;

        when(mockCommentService.deleteCommentById(commentId)).thenReturn(true);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/comments/{commentId}", commentId))
                .andExpect(status().isOk())
                .andExpect(content().string("Comment deleted successfully."));

        //Verify the controller calls the service method
        verify(mockCommentService, times(1)).deleteCommentById(commentId);
    }

    // deleteCommentById Sad Path 😭
    @Test
    public void testDeleteCommentByIdFail () throws Exception {
        /// Setup of mock method with "parameters" and simulate unable to delete
        Integer nonExistentCommentId = 100;

        when(mockCommentService.deleteCommentById(nonExistentCommentId)).thenReturn(false);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/comments/{commentId}", nonExistentCommentId))
                .andExpect(status().isBadRequest());
    }


}
