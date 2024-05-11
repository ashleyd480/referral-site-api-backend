package com.example.springreferallmain.service;

import com.example.springreferallmain.model.Address;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    IUserRepository iUserRepository;

    // We don't need a Delete mapping for Address, since when a user is deleted, an address is deleted.
    // Similarly, when a user is created, an address is required to be added.
    // Instead, I have decided to write two methods that allow me to target just the address of a user, that is 1.) getting just the address of a user and 2.) updating just the address of a user.


    // ------- GET--------
    // Get address for a specific user (to display address for confirmation for mailing merch)
    public Address getAddressByUserId(Integer userId) {
        User existingUser = iUserRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with id " + userId + " does not exist." ));;
        return existingUser.getAddress();
    }
    
    //^This is known as lazy loading. Because User has a CascadeType.ALL with Address, Hibernate "will transparently fetch the associated Address entity from the database and provide you with access to its fields as if they were part of the User entity."


    // ------- PATCH --------
    // Update just the address info of a user (i.e. user wants to update shipping/billing address)
    public Address modifyAddressUserId(Integer userId, Address newAddress) {
        //Find user by id
        User existingUser = iUserRepository.findById(userId).orElseThrow(()-> new RuntimeException("User with id " + userId + " does not exist." ));

        //Get that user's address
        Address existingUserAddress = existingUser.getAddress();

        //Now let's update the address for that user
        existingUserAddress.setStreet(newAddress.getStreet());
        existingUserAddress.setCity(newAddress.getCity());
        existingUserAddress.setState(newAddress.getState());
        existingUserAddress.setZipCode(newAddress.getZipCode());
        existingUserAddress.setCountry(newAddress.getCountry());


        // save updated user to cascade the changes to the address :)
        iUserRepository.save(existingUser);

        //return updated address
        return existingUserAddress;
    }
}
