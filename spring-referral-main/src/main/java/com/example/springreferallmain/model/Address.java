package com.example.springreferallmain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer addressId;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;


    @JsonIgnore
    @OneToOne
    User user;

}

// -------  One to One with User --------

// One address is associated with one user.
// We are able to link a user to an address b/c User has a foreign key of address_Id.
// @JsonIgnore so that user doesn't print out again under address, preventing circular reference

// Side note: zipCode as string type to account for if zip code has dash