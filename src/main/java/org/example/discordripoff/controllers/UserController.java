package org.example.discordripoff.controllers;

import org.example.discordripoff.entities.User;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> LoginUser(@RequestBody User user) {
        boolean result = userService.IsValidUser(user);
        return result ? AppResponse.success().withMessage("User login Successful").build() : AppResponse.error().withMessage("User login Failed").build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }

    // Add method for adding friends
    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<?> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        boolean added = userService.addFriend(userId, friendId);
        return added ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


}