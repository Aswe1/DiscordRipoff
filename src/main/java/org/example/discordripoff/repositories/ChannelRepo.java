package org.example.discordripoff.repositories;

import jakarta.transaction.Transactional;
import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChannelRepo extends JpaRepository<Channel, Integer> {
    List<Channel> findByOwnerAndIsActiveTrue(User owner);
    List<Channel> findAllByIsActiveTrue();

    Channel findByName(String name);
    boolean existsByName(String name);
    boolean existsByIdAndIsActiveFalse(int id);
    //List<Channel> findByUser(User user); // Find All channels that a user has joined

    @Transactional
    @Modifying
    @Query("UPDATE Channel c SET c.isActive = false WHERE c.id = :channelId")
    void softDeleteById(@Param("channelId") int channelId);
}
