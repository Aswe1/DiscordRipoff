package org.example.discordripoff.services;

import org.example.discordripoff.entities.User;
import org.example.discordripoff.repositories.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public boolean IsValidUser(User user)
    {
        if (user.getEmail() == null || user.getEmail().isEmpty())
            return false;

        Optional<User> testUser = userRepo.findByEmail(user.getEmail());
        if(testUser.isEmpty())
            return false;

        return !testUser.equals(user);
    }

    public User createUser(User user) {
        if (userRepo.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_ACCEPTABLE,
                    "Email already exists"
            );
        }
        return userRepo.save(user);
    }

    //Will currently only return the first user with the specified username. For uniqueness use
    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepo.findByUsername(username).getFirst());
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

    public boolean addFriend(int userId, int friendId) {
        User user = userRepo.findById(userId).orElse(null);
        User friend = userRepo.findById(friendId).orElse(null);

        if (user == null || friend == null) return false;

        if (!user.getFriends().contains(friend)) {
            user.getFriends().add(friend);
            userRepo.save(user);
            return true;
        }
        return false;
    }
}
