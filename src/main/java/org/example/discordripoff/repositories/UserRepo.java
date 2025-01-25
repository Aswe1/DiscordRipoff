package org.example.discordripoff.repositories;

import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByIsActive(boolean isActive);
}
