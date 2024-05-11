package com.example.springreferallmain.controller;

import com.example.springreferallmain.model.Address;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.service.AddressService;
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

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(AddressController.class)
@WebMvcTest(AddressControllerTest.class)
public class AddressControllerTest {
    @Autowired
    private MockMvc mockMvc;
    //wire dependency so we can simulate making those endpoint calls

    @MockBean
    private UserService mockUserService;

    @MockBean
    private AddressService mockAddressService;

    // ----------- TEST DATA  -----------

    private Address testAddress = TestData.makeTestAddress1();
    private Address emptyTestAddress = TestData.makeTestEmptyAddress();

    // ----------- NOTE  -----------
// We didn't need a Delete mapping for Address, since when a user is deleted, an address is deleted.
// Similarly, when a user is created, an address is required to be added.
// ^ Those are the two methods I'm testing.


// |           TESTING RETRIEVE  | \\
// Get address for a specific user (to display address for confirmation for mailing merch)

    // getAddressByUserId Happy Path 😊
    @Test
    public void testGetAddressByUserIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId= 1;
        // Convert the User object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(testAddress);

        when(mockAddressService.getAddressByUserId(userId)).thenReturn(testAddress);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(get("/address/users/{userId}", userId))
                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(addressJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockAddressService, times(1)).getAddressByUserId(userId);
    }

    // getAddressByUserId Sad Path 😭
    @Test
    public void testGetAddressByUserIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer nonexistentUserId = 100;
        when(mockAddressService.getAddressByUserId(nonexistentUserId)).thenReturn(null);


        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(get("/address/users/{userId}", nonexistentUserId))
                .andExpect(status().isNotFound()); // Expecting status not found
    }

// |           TESTING PATCH | \\

    // Update just the address info of a user (i.e. user wants to update shipping/billing address)
    // modifyAddressForUser Happy Path 😊
    @Test
    public void testModifyAddressForUserIdPass() throws Exception {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        // Convert the Address object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(testAddress);

        when(mockAddressService.modifyAddressUserId(userId, testAddress)).thenReturn(testAddress);

        // Test the controller method and see if expected output and status is returned; provide "parameters" by URL
        mockMvc.perform(patch("/address/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))

                .andExpect(status().isOk())  // Expecting status code 200
                .andExpect(content().json(addressJson)); // Response should be the same JSON as request

        // Verify the controller calls the service method
        verify(mockAddressService, times(1)).modifyAddressUserId(userId, testAddress);
    }

    // modifyAddressForUser Sad Path 😭
    @Test
    public void testModifyAddressForUserIdFail() throws Exception {
        // Setup of mock method with parameters
        Integer userId = 1;
        when(mockAddressService.modifyAddressUserId(userId, emptyTestAddress)).thenReturn(null);

        // Convert the Address object to JSON format
        ObjectMapper objectMapper = new ObjectMapper();
        String addressJson = objectMapper.writeValueAsString(emptyTestAddress);

        // Test the controller method and see if expected output and status is returned
        mockMvc.perform(patch("/address/users/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(addressJson))
                .andExpect(status().isBadRequest());// Expecting status code 400

    }

}
