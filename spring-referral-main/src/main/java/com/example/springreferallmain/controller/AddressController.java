package com.example.springreferallmain.controller;


import com.example.springreferallmain.model.Address;
import com.example.springreferallmain.service.AddressService;
import com.example.springreferallmain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("address")
// A user has a OneToOne CascadeType.All with address, therefore address is updated via User.
public class AddressController {
    @Autowired
    UserService userService;

    @Autowired
    AddressService addressService;

    // We don't need a Delete mapping for Address, since when a user is deleted, an address is deleted.
    // Similarly, when a user is created, an address is required to be added.
    // Instead, I have decided to write two methods that allow me to target just the address of a user, that is 1.) getting just the address of a user and 2.) updating just the address of a user.

    // ------- GET--------
    // Get address for a specific user (to display address for confirmation for mailing merch)
    @GetMapping("users/{userId}")
    public ResponseEntity<Address> getAddressByUserId(@PathVariable Integer userId) {
        Address addressForSpecificUser = addressService.getAddressByUserId(userId);
        if (addressForSpecificUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(addressForSpecificUser);
    }


    // ------- PATCH --------
    // Update just the address info of a user (i.e. user wants to update shipping/billing address)
    @PatchMapping("users/{userId}")
    public ResponseEntity<Address> modifyAddressForUserId(@PathVariable Integer userId, @RequestBody Address newAddress) {
        Address modifiedAddressForUserId = addressService.modifyAddressUserId(userId, newAddress);
        if (modifiedAddressForUserId == null || modifiedAddressForUserId.getStreet().isEmpty() || modifiedAddressForUserId.getCity().isEmpty() || modifiedAddressForUserId.getState().isEmpty() || modifiedAddressForUserId.getZipCode().isEmpty() || modifiedAddressForUserId.getCountry().isEmpty()) {
            return ResponseEntity.badRequest().build();

        }
        return ResponseEntity.ok(modifiedAddressForUserId);


    }


}
