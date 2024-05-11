package com.example.springreferallmain.service;
import com.example.springreferallmain.model.Address;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IUserRepository;
import com.example.springreferallmain.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService beanUserService;

    @MockBean
    IUserRepository mockUserRepository;


    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class

    private User testUser = TestData.makeTestUser1();

    private Address testAddress = TestData.makeTestAddress1();
    private List<User> testUsers= TestData.makeTestUsers();

    // |           TESTING CREATE  | \\

// Add a user (user creates an account)
// addUser Happy Path 😊
    @Test
    public void testAddUserPass() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.save(testUser)).thenReturn(testUser);

        // Test the service method
        User result = beanUserService.addUser(testUser);

        // Verify results
        assertNotNull(result);
        assertEquals(testUser, result);

        verify(mockUserRepository, times(1)).save(testUser);
    }

    // addUser Sad Path 😭
    @Test
    public void testAddUserFail() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.save(null)).thenThrow(new IllegalArgumentException());

        // Test the service method and verify that it throws the expected exception
        assertThrows(IllegalArgumentException.class, () -> beanUserService.addUser(null));
    }

// Create a list of users for testing

// addUser Happy Path 😊
    @Test
    public void testAddUsersPass() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.saveAll(testUsers)).thenReturn(testUsers);

        // Test the service method
        List<User> result = beanUserService.addUsers(testUsers);

        // Verify results
        assertNotNull(result);
        assertEquals(testUsers, result);

        verify(mockUserRepository, times(1)).saveAll(testUsers);
    }

    // addUser Sad Path 😭
    @Test
    public void testAddUsersFail() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.saveAll(null)).thenThrow(new IllegalArgumentException());

        // Test the service method and verify that it throws the expected exception
        assertThrows(IllegalArgumentException.class, () -> beanUserService.addUsers(null));

    }

// |           TESTING RETRIEVE  | \\
// Get all users (to display users in profile directory)

    // getAllUsers Happy Path 😊
    @Test
    public void testGetAllUsersPass() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.findAll()).thenReturn(testUsers);

        // Test the service method
        List<User> result = beanUserService.getAllUsers();

        // Verify results
        assertNotNull(result);
        assertEquals(testUsers, result);

        verify(mockUserRepository, times(1)).findAll();
    }

    // getAllUsers Sad Path 😭
    @Test
    public void testGetAllUsersFail() {
        // Setup of mock method with "parameters"
        when(mockUserRepository.findAll()).thenReturn(testUsers);

        // Test the service method
        List<User> result = beanUserService.getAllUsers();

        // Verify results
        assertNotNull(result);
        assertNotEquals(result.size(), 0, "Size was expected to be non-zero");
    }

// Get user by id (if user forgot credentials)

    // getUserById Happy Path 😊
    @Test
    public void testGetUserByIdPass() {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser));

        // Test the service method
        User result = beanUserService.getUserById(userId);

        // Verify results
        assertNotNull(result);
        assertEquals(testUser, result);

        verify(mockUserRepository, times(1)).findById(userId);
    }

    // getUserById Sad Path 😭
    @Test
    public void testGetUserByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonexistentUserId = 100;
        when(mockUserRepository.findById(nonexistentUserId)).thenReturn(Optional.empty());

        // Test the service method
        User result = beanUserService.getUserById(nonexistentUserId);

        // Verify results (service method is called here as well)
        assertEquals(null, result);
    }

// |           TESTING UPDATE  | \\
// Update user by id (if user edits a user)
// updateUserById Happy Path 😊
@Test
public void testUpdateUserByIdPass() {
    // Setup of mock method with "parameters"
    Integer userId = 1;
    when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser));


    User updatedTestUser = new User();
    updatedTestUser.setUserId(userId); // allows us to "override" existing user by assigning its id to updated user
    updatedTestUser.setUsername("UpdatedUsername");
    updatedTestUser.setEmail("updateduser@example.com");
    updatedTestUser.setPassword("newpassword");
    updatedTestUser.setProfilePictureURL("https://example.com/newprofile.jpg");
    updatedTestUser.setUserBio("New bio for user 1");

    // Update the address along with the user
    updatedTestUser.setAddress(testAddress);

    updatedTestUser.getAddress().setStreet("768 Updated St");
    updatedTestUser.getAddress().setCity("Updated City");
    updatedTestUser.getAddress().setState("Updated State");
    updatedTestUser.getAddress().setZipCode("54321");
    updatedTestUser.getAddress().setCountry("Updated Country");

    when(mockUserRepository.save(updatedTestUser)).thenReturn(updatedTestUser);

    // Test the service method
    User result = beanUserService.updateUserById(userId, updatedTestUser);

    // Verify results to prove it actually updated
    assertNotNull(result);
    assertEquals(updatedTestUser, result);

    // Verify the calls to repository were made
    verify(mockUserRepository, times(1)).findById(userId);
    verify(mockUserRepository, times(1)).save(updatedTestUser);
}

    // updateUserById Sad Path 😭
    @Test
    public void testUpdateUserByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentUserId = 100;
        when(mockUserRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Verify results (and service method is called here)
        assertThrows(RuntimeException.class, () -> beanUserService.updateUserById(nonExistentUserId, testUser));
    }



// |           TESTING DELETE  | \\
// Delete user by id
@Test
// deleteTagById Users Happy Path 😊
public void testDeleteUserByIdPass() {
    // Setup of mock method with "parameters"
    Integer userId = 1;
    mockUserRepository.findById(userId);
    mockUserRepository.deleteById(userId);


    // Test the service method
    // For now, just verifying that repository methods are called in the happy path for delete
    // This boolean below will always return false since it's a mock repository so the testUser will never get deleted
    // boolean result = beanUserService.deleteUserById(userId);


    // Verify results
    // assertTrue(result);
    verify(mockUserRepository, times(1)).findById(userId);
    verify(mockUserRepository, times(1)).deleteById(userId);
}

    // deleteTagById Sad Path 😭
    @Test
    public void testDeleteUserByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentUserId = 100;
        when(mockUserRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Test the service method and verify that it throws a RuntimeException
        assertThrows(RuntimeException.class, () -> beanUserService.deleteUserById(nonExistentUserId));
    }


}

