package com.example.springreferallmain.controller;

import com.example.springreferallmain.model.Post;
import com.example.springreferallmain.model.Tag;
import com.example.springreferallmain.service.TagService;
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

@Import(TagController.class)
@WebMvcTest(TagControllerTest.class)
public class TagControllerTest {
    @Autowired
    private MockMvc mockMvc;
    //wire dependency so we can simulate making those endpoint calls

    @MockBean
    private TagService mockTagService;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class
    private Tag testTag = TestData.makeTestTag1();

    private Tag emptyTestTag = TestData.makeEmptyTestTag();
    private List<Tag> testTags = TestData.makeTestTags();

    private List<Tag> emptyTestTagList = new ArrayList<>();

    private List<Post> testPosts = TestData.makeTestPosts();


// |           TESTING CREATE  | \\

    // Add a tag (user creates a tag)
    // addTag Happy Path 😊
    @Test
    public void testAddTagPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTag);

        when(mockTagService.addTag(testTag)).thenReturn(testTag);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags", testTag)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(tagJson)); // Response should be the same JSON as request

        //Verify the controller calls the service method
        verify(mockTagService, times(1)).addTag(testTag);
    }


    // addTag Sad Path 😭
    @Test
    public void testAddTagFail() throws Exception {
        /// Setup of mock method with "parameters"
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(emptyTestTag);

        when(mockTagService.addTag(emptyTestTag)).thenReturn(null);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags", emptyTestTag)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null tag provided. You must provide tag name and description")); // Expecting error message

    }

    // Create a list of tags for testing
    // addTags Happy Path 😊
    @Test
    public void testAddTagsPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTags);

        when(mockTagService.addTags(testTags)).thenReturn(testTags);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags/bulk")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(tagJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockTagService, times(1)).addTags(testTags);
    }

    // addTags Sad Path 😭
    @Test
    public void testAddTagsFail() throws Exception {
        /// Setup of mock method with "parameters"
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(emptyTestTagList);

        when(mockTagService.addTags(emptyTestTagList)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags/bulk")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null tags provided")); // Expecting error message
    }

// Add tag to a post

    // addTagToPost Happy Path 😊
    @Test
    public void addTagToPostPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer postId= 1;
        Integer tagId = 1;

        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTag);

        when(mockTagService.addTagToPost(postId, tagId)).thenReturn(testTag);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags/{tagId}/posts/{postId}",postId, tagId)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(tagJson)); // Response should be the same JSON as request

        //Verify the controller calls the service method
        verify(mockTagService, times(1)).addTagToPost(postId, tagId);
    }


    // ddTagToPost Sad Path 😭
    @Test
    public void addTagToPostFail() throws Exception {
        // Setup of mock method with "parameters"
        Integer postId= 1;
        Integer nonExistentTagId = 100;
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTag);

        when(mockTagService.addTag(testTag)).thenReturn(null);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/tags/{tagId}/posts/{postId}",postId, nonExistentTagId)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(tagJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 200
                .andExpect(content().string("Tsk tsk. Null tag provided"));
    }

// |           TESTING RETRIEVE  | \\

    // Get all tags (display tag options to user)
    // getAllTags Happy Path 😊
    @Test
    public void testGetAllTagsPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTags);

        when(mockTagService.getAllTags()).thenReturn(testTags);

        // Test the controller method and see if expected output and status is returned;
        mockMvc.perform(get("/tags"))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(tagJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockTagService, times(1)).getAllTags();
    }

    // getAllTags Sad Path 😭
    @Test
    public void testGetAllTagsFail() throws Exception {
        // Setup of mock method; ; simulating an empty list
        when(mockTagService.getAllTags()).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/tags"))
                .andExpect(status().isNotFound()); // Expecting status not found
    }


    // Get posts tied to a tag (to display posts with a specific tag)

// Note: I realize after the fact, I probably could have put this in tag controller, however since this still technically has to do with tags and since I'm still querying from the iTagRepository with tag.getPosts()... leaving it alone for now as project is almost due. Sowwy :(

    // getPostsByTagId Happy Path 😊
    @Test
    public void testGetPostsByTagIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer tagId = 1;
        // Convert the Post object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String postJson = objectMapper.writeValueAsString(testPosts);

        when(mockTagService.getPostsByTagById(tagId)).thenReturn(testPosts);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/tags/{tagId}/posts", tagId))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(postJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockTagService, times(1)).getPostsByTagById(tagId);
    }

    // getPostsByTagId Sad Path 😭
    @Test
    public void testGetPostsByTagIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer nonexistentTagId = 100;
        when(mockTagService.getPostsByTagById(nonexistentTagId)).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/tags/{tagId}/post", nonexistentTagId))
                .andExpect(status().isNotFound()); // Expecting status not found
    }


    // |           TESTING UPDATE  | \\

// Update tag by id (i.e. user wants to edit tag he/she/they created)
    // updateTagById Happy Path 😊

    @Test
    public void testUpdateTagByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer tagId = 1;
        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(testTag);

        when(mockTagService.updateTagById(tagId, testTag)).thenReturn(testTag);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(put("/tags/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(tagJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockTagService, times(1)).updateTagById(tagId, testTag);
    }


    // updateTagById Sad Path 😭
    @Test
    public void testUpdateTagByIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer tagId = 1;
        when(mockTagService.updateTagById(tagId, emptyTestTag)).thenReturn(null);

        // Convert the Tag object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String tagJson = objectMapper.writeValueAsString(emptyTestTag);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(put("/tags/{tagId}", tagId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tagJson))
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null tag update. You must provide tag name and description"));


    }


// |           TESTING DELETE  | \\

    // deleteTagById Happy Path 😊
    @Test
    public void testDeleteTagByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer tagId = 1;


        when(mockTagService.deleteTagById(tagId)).thenReturn(true);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/tags/{tagId}", tagId))
                .andExpect(status().isOk())
                .andExpect(content().string("Tag deleted successfully."));

        //Verify the controller calls the service method
        verify(mockTagService, times(1)).deleteTagById(tagId);
    }


    // deleteTagById Sad Path 😭
    @Test
    public void testDeleteTagByIdFail() throws Exception {
        /// Setup of mock method with "parameters" and simulate unable to delete
        Integer nonExistentTagId = 100;

        when(mockTagService.deleteTagById(nonExistentTagId)).thenReturn(false);


        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/tags/{tagId}", nonExistentTagId))
                .andExpect(status().isBadRequest());

    }

}
