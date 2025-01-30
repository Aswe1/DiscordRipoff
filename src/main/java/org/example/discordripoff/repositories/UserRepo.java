package org.example.discordripoff.repositories;

import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByisActive(boolean isActive);

    @Query("SELECT u FROM User u WHERE u.id IN (SELECT f.user2.id FROM Friendship f WHERE f.user1.id = :userId)")
    List<User> getFriendsByUserId(@Param("userId") int userId);
}
