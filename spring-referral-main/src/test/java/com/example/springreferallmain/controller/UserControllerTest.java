package com.example.springreferallmain.controller;

import com.example.springreferallmain.model.User;
import com.example.springreferallmain.service.UserService;
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

@Import(UserController.class)
@WebMvcTest(UserControllerTest.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    //wire dependency so we can simulate making those endpoint calls

    @MockBean
    private UserService mockUserService;

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class
    private User testUser = TestData.makeTestUser1();

    private User emptyTestUser = TestData.makeEmptyTestUser();

    private List<User> testUsers = TestData.makeTestUsers();

    private List<User> emptyTestUserList = new ArrayList<>();


// |           TESTING CREATE  | \\

    // Create a new user (when user creates an account)
    // addUser Happy Path 😊
    @Test
    public void testAddUserPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(testUser);

        when(mockUserService.addUser(testUser)).thenReturn(testUser);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/users", testUser)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(userJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(userJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockUserService, times(1)).addUser(testUser);
    }


    // addUser Sad Path 😭
    @Test
    public void testAddUserFail() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(emptyTestUser);

        when(mockUserService.addUser(emptyTestUser)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/users", emptyTestUser)
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(userJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null user provided. Please make sure to fill in the required fields when creating account.")); // Expecting error message
    }

    // Create a list of users for testing

    // addUsers Happy Path 😊
    @Test
    public void testAddUsersPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(testUsers);

        when(mockUserService.addUsers(testUsers)).thenReturn(testUsers);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/users/bulk")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(userJson)) // Pass JSON content
                .andExpect(status().isCreated())  // Expecting status code 200
                .andExpect(content().json(userJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockUserService, times(1)).addUsers(testUsers);
    }


    // addUsers Sad Path 😭
    @Test
    public void testAddUsersFail() throws Exception {
        /// Setup of mock method with "parameters"
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(emptyTestUserList);

        when(mockUserService.addUsers(emptyTestUserList)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(post("/users/bulk")
                        .contentType(MediaType.APPLICATION_JSON) // Set content type
                        .content(userJson)) // Pass JSON content
                .andExpect(status().isBadRequest())  // Expecting status code 400
                .andExpect(content().string("Tsk tsk. Null users provided")); // Expecting error message

    }

// |           TESTING RETRIEVE  | \\
// Get all users (to display users in profile directory)

    // getAllUsers Happy Path 😊
    @Test
    public void testGetAllUsersPass() throws Exception {
        // Setup of mock method with "parameters"
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(testUsers);

        when(mockUserService.getAllUsers()).thenReturn(testUsers);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(userJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockUserService, times(1)).getAllUsers();
    }

    // getAllUsers Sad Path 😭
    @Test
    public void testGetAllUsersFail() throws Exception {
        // Setup of mock method; simulating an empty list
        when(mockUserService.getAllUsers()).thenReturn(Collections.emptyList());

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/users"))
                .andExpect(status().isNotFound()); // Expecting status not found
    }

// Get user by id (to display user info on My Account page)

    // getUserById Happy Path 😊
    @Test
    public void testGetUserByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(testUser);

        when(mockUserService.getUserById(userId)).thenReturn(testUser);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/users/{userId}", userId))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(userJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockUserService, times(1)).getUserById(userId);
    }

    // getAllUsers Sad Path 😭
    @Test
    public void testGetUserByIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer nonexistentUserId = 100;
        when(mockUserService.getUserById(nonexistentUserId)).thenReturn(null);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/users/{userId}", nonexistentUserId))
                .andExpect(status().isNotFound()); // Expecting status not found
    }


// |           TESTING UPDATE  | \\

    // Update user by id (i.e. user wants to edit user details)
    // updateUserById Happy Path 😊
    @Test
    public void testUpdateUserByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(testUser);

        when(mockUserService.updateUserById(userId, testUser)).thenReturn(testUser);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(userJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockUserService, times(1)).updateUserById(userId, testUser);
    }

    // updateUserById Sad Path 😭
    @Test
    public void testUpdateUserByIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer userId = 1;
        when(mockUserService.updateUserById(userId, emptyTestUser)).thenReturn(null);

        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(emptyTestUser);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(put("/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Tsk tsk. Null user provided. Please make sure to fill in the required fields when updating account."));// Expecting status code 400
    }

    // |           TESTING DELETE  | \\
// Delete user by id
// deleteUserById Happy Path 😊
    @Test
    public void testDeleteUserByIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId = 1;

        when(mockUserService.deleteUserById(userId)).thenReturn(true);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/users/{userId}", userId))
                .andExpect(status().isOk())
                .andExpect(content().string("Your account deleted successfully. Hasta la vista"));

        //Verify the controller calls the service method
        verify(mockUserService, times(1)).deleteUserById(userId);
    }

    // deleteUserById Sad Path 😭
    @Test
    public void testDeleteUserByIdFail() throws Exception {
        /// Setup of mock method with "parameters" and simulate unable to delete
        Integer nonExistentUserId = 100;

        when(mockUserService.deleteUserById(nonExistentUserId)).thenReturn(false);

        //Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(delete("/users/{userId}", nonExistentUserId))
                .andExpect(status().isBadRequest());
    }

}
