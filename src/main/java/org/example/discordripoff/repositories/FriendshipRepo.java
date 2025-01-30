package org.example.discordripoff.repositories;

import org.example.discordripoff.entities.Friendship;
import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FriendshipRepo extends JpaRepository<Friendship, Integer> {
    List<Friendship> findByUser1(User user1);
    List<Friendship> findByUser2(User user);

    List<Friendship> findByUser1AndUser2(User user1, User user2);
}
