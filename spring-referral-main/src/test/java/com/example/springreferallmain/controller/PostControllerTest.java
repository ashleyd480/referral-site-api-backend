package com.example.springreferallmain.controller;


import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.service.CommentService;
import com.example.springreferallmain.service.PostService;
import com.example.springreferallmain.testdata.TestData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(PostController.class)
@WebMvcTest(PostControllerTest.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;
    //wire dependency so we can simulate making those endpoint calls

    @MockBean
    private PostService mockPostService;

    @MockBean
    private CommentService mockCommentService;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class

    private Post testPost = TestData.makeTestPost1();
    private List<Post> testPosts = TestData.makeTestPosts();

    private Post emptyTestPost = TestData.makeEmptyTestPost();

    private List<Post> emptyTestPostList = new ArrayList<>();


    // |           TESTING CREATE  | \\
// Add a post (user creates a post)
    // addPost Happy Path 😊
    @Test
    public void testAddPostPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId = 1;

        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(testPost);

        when(mockPostService.addPost(userId, testPost)).thenReturn(testPost);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/posts/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(postJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(postJson)); // Response should be the same JSON as request


        // Verify the controller calls the service method
        verify(mockPostService, times(1)).addPost(userId, testPost);
    }


    // addPost Sad Path 😭
    @Test
    public void testAddPostFail() throws Exception {
        /// Setup of mock method with "parameters"
        Integer userId = 1;
        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(emptyTestPost);

        when(mockPostService.addPost(userId, emptyTestPost)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/posts/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(postJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Please make sure you provide a post title and content before submitting.")); // Expecting error message

    }

    // Create a list of posts for testing
    // addPosts Happy Path 😊
    @Test
    public void testAddPostsPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(testPosts);

        when(mockPostService.addPosts(testPosts)).thenReturn(testPosts);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(postJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(postJson)); // Response should be the same JSON as request


        // Verify the controller calls the service method
        verify(mockPostService, times(1)).addPosts(testPosts);
    }


    // addPosts Sad Path 😭
    @Test
    public void testAddPostsFail() throws Exception {
        /// Setup of mock method with "parameters"
        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(emptyTestPostList);

        when(mockPostService.addPosts(emptyTestPostList)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(postJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null posts provided")); // Expecting error message

    }


// |           TESTING RETRIEVE  | \\

// Get all posts (to display on homepage)
// getAllPosts Happy Path 😊
@Test
public void testGetAllPostsPass() throws Exception {
    // Setup of mock method with "parameters"

    // Convert the Post object to JSON format
    ObjectMapper objectMapper = new ObjectMapper();
    String postJson = objectMapper.writeValueAsString(testPosts);

    when(mockPostService.getAllPosts()).thenReturn(testPosts);

    // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
    mockMvc.perform(get("/posts"))
            .andExpect(status().isOk())  // Expecting status code 200
            .andExpect(content().json(postJson)); // Response should be the same JSON as request


    // Verify the controller calls the service method
    verify(mockPostService, times(1)).getAllPosts();
}

    // getAllPosts Sad Path 😭
    @Test
    public void testGetAllPostsFail() throws Exception {
        // Setup of mock method; simulating an empty list

        when(mockPostService.getAllPosts()).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/posts"))
                .andExpect(status().isNotFound()); // Expecting status not found

    }

// Get all posts matching a keyword search, i.e. so users can see matching refer-all posts
    // getPostsByDescriptionContaining Happy Path 😊

    @Test
    public void testGetPostsByDescriptionContainingPass() throws Exception {
        // Setup of mock method with "parameters"
        String keyword = "jobs";
        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(testPosts);

        when(mockPostService.getPostsByDescriptionContaining(keyword)).thenReturn(testPosts);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/posts")
                        .param("keyword", keyword))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(postJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockPostService, times(1)).getPostsByDescriptionContaining(keyword);
    }

    // getPostsByDescriptionContaining Sad Path 😭
    @Test
    public void testGetPostsByDescriptionContainingFail() throws Exception {
        // Setup of mock method; simulating an empty list because no results
        String keyword = "nonexistent";
        when(mockPostService.getPostsByDescriptionContaining(keyword)).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/posts")
                        .param("keyword", keyword))
                .andExpect(status().isNotFound()); // Expecting status not found
    }





// |           TESTING UPDATE  | \\

// Update post by id (i.e. user wants to edit post details)
// updatePostById Happy Path 😊
@Test
public void testUpdatePostByIdPass() throws Exception {
    // Setup of mock method with "parameters"
    Integer postId = 1;
    // Convert the Post object to JSON format
    ObjectMapper objectMapper = new ObjectMapper();
    String postJson = objectMapper.writeValueAsString(testPost);

    when(mockPostService.updatePostById(postId, testPost)).thenReturn(testPost);

    // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
    mockMvc.perform(put("/posts/{postId}", postId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(postJson))
            .andExpect(status().isOk())  // Expecting status code 200
            .andExpect(content().json(postJson));
}

    // updatePostById Sad Path 😭
    @Test
    public void testUpdatePostByIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer postId = 1;
        when(mockPostService.updatePostById(postId, emptyTestPost)).thenReturn(null);

        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(emptyTestPost);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(put("/posts/{postId}", postId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson))
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Please make sure you provide a post title and content before submitting.")); // Expecting error message
    }


// |           TESTING DELETE  | \\
// Delete post by id
    // deletePostById Happy Path 😊
    @Test
    public void testDeletePostByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer postId = 1;

        when(mockPostService.deletePostById(postId)).thenReturn(true);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/posts/{postId}", postId))
                .andExpect(status().isOk())
                .andExpect(content().string("Post deleted successfully."));

        //Verify the controller calls the service method
        verify(mockPostService, times(1)).deletePostById(postId);
    }

    // deletePostById Sad Path 😭
    @Test
    public void testDeletePostByIdFail() throws Exception {
        /// Setup of mock method with "parameters" and simulate unable to delete
        Integer nonExistentPostId = 100;

        when(mockPostService.deletePostById(nonExistentPostId)).thenReturn(false);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/posts/{postId}", nonExistentPostId))
                .andExpect(status().isBadRequest());
    }


}
