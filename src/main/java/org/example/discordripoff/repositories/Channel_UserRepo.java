package org.example.discordripoff.repositories;

import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.Channel_User;
import org.example.discordripoff.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface Channel_UserRepo extends JpaRepository<Channel_User, Integer> {
    List<Channel_User> findByChannel(Channel channel);
    List<Channel_User> findByUser(User user);
    Optional<Channel_User> findByChannelAndUser(Channel channel, User user);

    List<Channel_User> findByChannelAndUserRole(Channel channel, Channel_User.UserRole userRole);
    List<Channel_User> findByUserAndUserRole(User user, Channel_User.UserRole userRole);

    // Delete a user from a channel
    void deleteByChannelAndUser(Channel channel, User user);
}
