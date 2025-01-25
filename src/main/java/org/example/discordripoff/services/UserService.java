package org.example.discordripoff.services;

import org.example.discordripoff.entities.User;
import org.example.discordripoff.repositories.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public User createUser(User user) {
        return userRepo.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    public List<User> findActiveUsers() {
        return userRepo.findByIsActive(true);
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
}
