package com.example.springreferallmain.service;
import com.example.springreferallmain.config.RestTemplateConfig;
import com.example.springreferallmain.dto.NotificationDTO;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {
    @Autowired
    IUserRepository iUserRepository;

    @Autowired
    RestTemplate restTemplate;


    // ------- CREATE --------
    // Create a new user (when user creates an account)

    public User addUser(User userToAdd) {
        return iUserRepository.save(userToAdd);
    }

    // Create a list of users for testing in Postman
    public List<User> addUsers(List<User> usersToAdd) {
        return iUserRepository.saveAll(usersToAdd);
    }



    // ------- RETRIEVE --------
    // Get all users (to display users in profile directory)
    public List<User> getAllUsers() {
        return iUserRepository.findAll();
    }

    // Get user by id (if user forgot credentials)

    public User getUserById(Integer userId) {
        return iUserRepository.findById(userId).orElse(null);
    }

    // ------- UPDATE --------
    // Update user info by id (if user updates his account)

    public User updateUserById(Integer userId, User editedUser) {
        // Let's find the user we want to update first
        User existingUser = iUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " does not exist."));

        // Then, we update the user info
        existingUser.setUsername(editedUser.getUsername());
        existingUser.setEmail(editedUser.getEmail());
        existingUser.setPassword(editedUser.getPassword());
        existingUser.setProfilePictureURL(editedUser.getProfilePictureURL());
        existingUser.setUserBio(editedUser.getUserBio());

        // Address is updated through User as address is non-nullable
        existingUser.getAddress().setStreet(editedUser.getAddress().getStreet());
        existingUser.getAddress().setCity(editedUser.getAddress().getCity());
        existingUser.getAddress().setState(editedUser.getAddress().getState());
        existingUser.getAddress().setZipCode(editedUser.getAddress().getZipCode());
        existingUser.getAddress().setCountry(editedUser.getAddress().getCountry());


        // Save the updated user
        return iUserRepository.save(existingUser);
    }


    // ------- DELETE--------
    // With delete, we first check if the user to delete is present, else throw an exception
    // We then delete that User
    // We then return what constitutes a "true" - in this case if the user was deleted, it should no longer be present

    public boolean deleteUserById(Integer userId) {
        iUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " does not exist."));
        iUserRepository.deleteById(userId);
        return !iUserRepository.findById(userId).isPresent();

    }



    // ------- MICROSERVICE GET NOTIFICATION FOR USER --------

    public List<NotificationDTO> getNotificationsByUserId(Integer userId) {
        // for error handling in case user doesn't exist
        User existingUser = iUserRepository.findById(userId).orElseThrow(() -> new RuntimeException("User with id " + userId + " does not exist."));

        // responseEntity is a variable to represent the metadata we get back which include the body and status

        ResponseEntity<NotificationDTO[]> responseEntity = restTemplate.getForEntity("http://localhost:8081/notifications/{userId}",
                NotificationDTO[].class,
                userId);

        // we check to see if the response was successful and if so we assign the body a variable name of listOfNotifications of data type array
        // And then we check that even if we do get a response, we double-check that the array is not null. If it's not null, we convert it into a List

        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            NotificationDTO[] listOfNotifications = responseEntity.getBody();

            if(listOfNotifications != null)
                return Arrays.asList(listOfNotifications);

        }

        return null;

    }





    }



