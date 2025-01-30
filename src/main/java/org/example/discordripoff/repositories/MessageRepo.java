package org.example.discordripoff.repositories;

import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.Message;
import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Message, Integer> {
    List<Message> findByChannel(Channel channel);
    List<Message> findBySender(User sender);
    List<Message> findByReceiver(User receiver);

    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1)")
    List<Message> findByTwoUsers(@Param("user1") User user1, @Param("user2") User user2);
}
