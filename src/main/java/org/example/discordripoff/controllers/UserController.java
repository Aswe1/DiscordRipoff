package org.example.discordripoff.controllers;

import org.example.discordripoff.entities.User;
import org.example.discordripoff.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUser(@PathVariable String username) {
        return userService.getUserByUsername(username)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.findActiveUsers();
        return ResponseEntity.ok(users);
    }

    // Add method for adding friends
    @PostMapping("/{userId}/friends/{friendId}")
    public ResponseEntity<Void> addFriend(@PathVariable int userId, @PathVariable int friendId) {
        boolean added = userService.addFriend(userId, friendId);
        return added ? ResponseEntity.ok().build() : ResponseEntity.badRequest().build();
    }


}