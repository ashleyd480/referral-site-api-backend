package com.example.springreferallmain.controller;
import com.example.springreferallmain.dto.NotificationDTO;
import com.example.springreferallmain.model.User;
import com.example.springreferallmain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    UserService userService;

    // ------- CREATE --------
    // Create a new user (when user creates an account)
    // ? return type b/c either could return a string or the new user created
    // User must provide an email, username, and password when creating an account

    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody User userToAdd) {
        if (userToAdd == null || userToAdd.getEmail().isEmpty() || userToAdd.getUsername().isEmpty()||userToAdd.getPassword().isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null user provided. Please make sure to fill in the required fields when creating account.");
        }
        else {
            User newUserCreated = userService.addUser(userToAdd);
            return new ResponseEntity<>(newUserCreated, HttpStatus.CREATED);


        }
    }


    // Create a list of Users for testing purposes
    @PostMapping("bulk")
    public ResponseEntity<?> addUsers(@RequestBody List<User> usersToAdd) {
        List<User> newUsersCreated = userService.addUsers(usersToAdd);
        if (usersToAdd == null || usersToAdd.isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null users provided");
        }
        return new ResponseEntity<>(newUsersCreated, HttpStatus.CREATED);
    }

    // ------- RETRIEVE --------
    // Get all users (to display users in profile directory)
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers () {
        List<User> registeredUsersList = userService.getAllUsers();
        if (registeredUsersList == null || registeredUsersList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registeredUsersList);
    }


    // Get user by id (to display user info on My Account page)
    @GetMapping("{userId}")
    public ResponseEntity<User> getUserById (@PathVariable Integer userId) {
        User registeredUser = userService.getUserById(userId);
        if (registeredUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(registeredUser);

    }


    // ------- UPDATE --------
    // Update user info by id (if user updates his account)
    // When editing account information, a user must still fill in the required username, email, and password.
    @PutMapping ("{userId}")
    public ResponseEntity<?> updateUserById (@PathVariable Integer userId, @RequestBody User editedUser) {
        User updatedUser= userService.updateUserById(userId, editedUser);
        if (updatedUser == null || updatedUser.getEmail().isEmpty() || updatedUser.getUsername().isEmpty()|| updatedUser.getPassword().isEmpty())  {
            return ResponseEntity.badRequest().body("Tsk tsk. Null user provided. Please make sure to fill in the required fields when updating account.");
        }
        return ResponseEntity.ok(updatedUser);

    }


    // ------- DELETE--------
    // Delete user by id (if user closes his account)
    // boolean deleted means if our deleteUserById returns true, then we give it a variable of "deleted"
    @DeleteMapping("{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable Integer userId) {
        boolean deleted = userService.deleteUserById(userId);
        if (!deleted) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().body("Your account deleted successfully. Hasta la vista");
    }


    // ------- MICROSERVICE GET NOTIFICATION FOR USER --------
    @GetMapping("{userId}/notifications")
    public ResponseEntity<?> getNotificationsByUserId (@PathVariable Integer userId) {
        if (userId == null) {
            return new ResponseEntity<>("We need user id", HttpStatus.BAD_REQUEST);
        }

       List<NotificationDTO> notificationsForThatUser = userService.getNotificationsByUserId(userId);

        // error handling below for microservice get call
        // if no notifications for user, they will see message that there are no notifications

        if (notificationsForThatUser != null && !notificationsForThatUser.isEmpty())
        {return ResponseEntity.ok(notificationsForThatUser);}
        else {
            return new ResponseEntity<>("Notifications for user not found", HttpStatus.NOT_FOUND);

        }


    }

}
