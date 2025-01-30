package org.example.discordripoff.services;

import org.example.discordripoff.entities.User;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.repositories.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<?> IsValidUser(User user)
    {
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return AppResponse.error().withMessage("Invalid Email").build();

        Optional<User> testUser = userRepo.findByEmail(user.getEmail());
        if(testUser.isEmpty())
            return AppResponse.error().withMessage("Invalid User").build();

        return AppResponse.success().withData( PrepData(testUser.get())).build();
    }

    public ResponseEntity<?> createUser(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent())   return AppResponse.error().withMessage("Email already in Use").build();
        if (userRepo.findByUsername(user.getUsername()).isPresent())    return AppResponse.error().withMessage("Username Taken").build();

        return AppResponse.success().withData(userRepo.save(user)).build();
    }


    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    public List<User> findActiveUsers() {

        return PrepData(userRepo.findByisActive(true));
    }

    public boolean removeUser(int id) {
        User user = userRepo.findById(id).orElse(null);
        if (user != null && user.isActive()) {
            user.setActive(false);
            userRepo.save(user);
            return true;

        }
        return false;
    }

    // This will clear Friendship data as it is currently its recursive loop,
    public User PrepData(User user)
    {
//        user.setFriendships(new ArrayList<>());
        return user;
    }
    // Same as PrepData just for ArrayLists
    public List<User> PrepData(List<User> users) {
        for (User user : users) {
//            user.setFriendships(new ArrayList<>());
        }
        return users;
    }
}
