package com.example.springreferallmain.service;


import com.example.springreferallmain.model.Address;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IUserRepository;
import com.example.springreferallmain.testdata.TestData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AddressServiceTest {
    @Autowired
    AddressService beanAddressService;

    @MockBean
    IUserRepository mockUserRepository;

    // We didn't need a Delete mapping for Address, since when a user is deleted, an address is deleted.
    // Similarly, when a user is created, an address is required to be added.
    // Instead, I only have two methods that allow me to target just the address of a user, that is 1.) getting just the address of a user and 2.) updating just the address of a user.
    // ^ Those are the two methods I'm testing.

    // ----------- TEST DATA  -----------
    // #DRY - keeping in DRY, variables to represent return from Test Data class

    private User testUser1 = TestData.makeTestUser1();
    private Address testAddress1 = TestData.makeTestAddress1();


    // |           TESTING RETRIEVE  | \\
    // Get address for a specific user (to display address for confirmation for mailing merch)

    // getAddressByUserById Happy Path 😊
    @Test
    public void testGetAddressByUserIdPass() {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser1));
        Address testUser1Address = testUser1.getAddress();

        // Test the service method
        Address result = beanAddressService.getAddressByUserId(userId);

        // Verify results
        assertNotNull(result);
        assertEquals(testUser1Address, result);

        verify(mockUserRepository, times(1)).findById(userId);
    }

    // getAddressByUserById Sad Path 😭
    @Test
    public void testGetAddressByUserIdFail() {
        // Setup of mock method with "parameters"
        Integer nonexistentUserId = 1;
        when(mockUserRepository.findById(nonexistentUserId)).thenReturn(Optional.empty());


        // Verify results (service method is called here as well)
        assertThrows(RuntimeException.class, () -> beanAddressService.getAddressByUserId(nonexistentUserId));
    }


    // |           TESTING PATCH  | \\
// Update just the address info of a user (i.e. user wants to update shipping/billing address)

    // modifyAddressUserId Happy Path 😊
    @Test
    public void testModifyAddressUserIdPass() {
        // Setup of mock method with "parameters"
        Integer userId = 1;
        Integer addressId = 1;
        when(mockUserRepository.findById(userId)).thenReturn(Optional.ofNullable(testUser1));

        Address updatedAddress = new Address();
        updatedAddress.setAddressId(addressId); // allowing us to "override" the address info of testUser1
        updatedAddress.setStreet("456 Updated St");
        updatedAddress.setCity("Updated City");
        updatedAddress.setState("Updated State");
        updatedAddress.setZipCode("54321");
        updatedAddress.setCountry("Updated Country");

        when(mockUserRepository.save(testUser1)).thenReturn(testUser1);

        // Test the service method
        Address result = beanAddressService.modifyAddressUserId(userId, updatedAddress);

        // Verify results to prove it actually updated
        assertNotNull(result);
        assertEquals(updatedAddress, result);

        // Verify the calls to repository were made
        verify(mockUserRepository, times(1)).findById(userId);
        verify(mockUserRepository, times(1)).save(testUser1);
    }

    // modifyAddressUserId Sad Path 😭
    @Test
    public void testUpdateUserByIdFail() {
        // Setup of mock method with "parameters"
        Integer nonExistentUserId = 100;
        when(mockUserRepository.findById(nonExistentUserId)).thenReturn(Optional.empty());

        // Verify results (and service method is called here)
        assertThrows(RuntimeException.class, () -> beanAddressService.modifyAddressUserId(nonExistentUserId, testAddress1));
    }
}
