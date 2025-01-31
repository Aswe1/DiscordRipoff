package org.example.discordripoff.services;

import jakarta.transaction.Transactional;
import org.example.discordripoff.entities.*;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.repositories.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    private final ChannelRepo channelRepo;
    private final UserRepo userRepo;
    private final MessageRepo messageRepo;
    private final Channel_UserRepo channel_UserRepo;

    public ChannelService(ChannelRepo channelRepo, UserRepo userRepo, MessageRepo messageRepo, Channel_UserRepo channel_UserRepo) {
        this.channelRepo = channelRepo;
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
        this.channel_UserRepo = channel_UserRepo;
    }
    public ResponseEntity<?> createNewChannel(Channel channel)
    {
        Optional<User> optionalUser = userRepo.findById(channel.getOwner().getId());
        boolean exists = channelRepo.existsByName(channel.getName());

        if(optionalUser.isEmpty())  return AppResponse.error().withMessage("Owner user doesnt exist").build();
        if(exists) return AppResponse.error().withMessage("Name Taken").build();

        Channel_User channel_User = new Channel_User();
        channel_User.setChannel(channel);
        channel_User.setUser(optionalUser.get());
        channel_User.setUserRole(Channel_User.UserRole.OWNER);


        channelRepo.save(channel);
        channel_UserRepo.save(channel_User);
        return AppResponse.success().withMessage("Channel Created Successfully").build();
    }

    public ResponseEntity<?> getChannelFromUserId(int userID)
    {
        Optional<User> optionalUser = userRepo.findById(userID);
        if (optionalUser.isEmpty()) return AppResponse.error().withMessage("User not found").build();
        List<Channel_User> channel_Users = channel_UserRepo.findByUser(optionalUser.get());

        List<Channel> channels = new ArrayList<>();
        for (Channel_User channelUser : channel_Users)
        {
            Channel channel = channelUser.getChannel();
            if (channel.isActive() && !channels.contains(channel)) {
                channels.add(channel);
            }
        }
        return AppResponse.success().withData(channels).build();
    }

    public List<Channel> getAllActiveChannels() {
        return channelRepo.findAllByIsActiveTrue();
    }

    public ResponseEntity<?> getMessagesFromChannelID(int id) {
        Optional<Channel> optionalChannel = channelRepo.findById(id);
        if (optionalChannel.isEmpty()) return AppResponse.error().withMessage("Channel not found").build();

        return AppResponse.success().withData(messageRepo.findByChannel(optionalChannel.get())).build();
    }

    public ResponseEntity<?> sendMessageToChannelID(Message message) {
        if (message.getContent().isEmpty())  return AppResponse.error().withMessage("Empty Message").build();

        Optional<User> optionalSender = userRepo.findById(message.getSender().getId());
        Optional<Channel> optionalChannel = channelRepo.findById(message.getChannel().getId());

        if(optionalSender.isEmpty() || optionalChannel.isEmpty()) return AppResponse.error().withMessage("Owner or channel is missing").build();

        Optional<Channel_User> optionalChannelUser = channel_UserRepo.findByChannelAndUser(optionalChannel.get(), optionalSender.get());
        if (optionalChannelUser.isEmpty()) return AppResponse.error().withMessage("User Not In Channel").build();

        messageRepo.save(message);
        return AppResponse.success().withMessage("Message added successfully").build();
    }

    public ResponseEntity<?> getChannelUsersFromChannelID(int id) {
        Optional<Channel> optionalChannel = channelRepo.findById(id);
        if (optionalChannel.isEmpty()) return AppResponse.error().withMessage("Channel not found").build();

        return AppResponse.success().withData(channel_UserRepo.findByChannel(optionalChannel.get())).build();
    }

    public ResponseEntity<?> addUserToChannel(int channelID, int authID ,int userID) {
       Optional<Channel> optionalChannel = channelRepo.findById(channelID);
       Optional<User> optionalAuth = userRepo.findById(authID);
       Optional<User> optionalUser = userRepo.findById(userID);
        if (optionalChannel.isEmpty() || optionalUser.isEmpty() || optionalAuth.isEmpty()) return AppResponse.error().withMessage("User Or Channel not found").build();

        Optional<Channel_User> optionalChannelAuth = channel_UserRepo.findByChannelAndUser(optionalChannel.get(), optionalAuth.get());
        if (optionalChannelAuth.isEmpty() || optionalChannelAuth.get().getUserRole() == Channel_User.UserRole.GUEST) return AppResponse.error().withMessage("No Authority").build();

        Optional<Channel_User> optionalChannelUser = channel_UserRepo.findByChannelAndUser(optionalChannel.get(), optionalUser.get());
        if (optionalChannelUser.isPresent()) return AppResponse.error().withMessage("User Already Added to channel").build();

        Channel_User channelUser = new Channel_User();
        channelUser.setChannel(optionalChannel.get());
        channelUser.setUser(optionalUser.get());

        channel_UserRepo.save(channelUser);
        return AppResponse.success().withMessage("User Added successfully").build();
    }
    @Transactional
    public ResponseEntity<?> softDeleteChannel(Integer channelID, Integer ownerID) {
        Optional<Channel> optionalChannel = channelRepo.findById(channelID);
        Optional<User> optionalUser = userRepo.findById(ownerID);
        if(optionalChannel.isEmpty() || optionalUser.isEmpty()) return AppResponse.error().withMessage("Invalid Channel Or Owner").build();
        if(channelRepo.existsByIdAndIsActiveFalse(channelID)) return AppResponse.error().withMessage("Channel already deleted").build();
        if(optionalChannel.get().getOwner() != optionalUser.get()) return AppResponse.error().withMessage("Invalid Owner").build();

        channelRepo.softDeleteById(channelID);
        return AppResponse.success().withMessage("Channel Soft Deleted Successfully").build();
    }

    @Transactional
    public ResponseEntity<?> updateChannel(int userId, Channel channel) {
        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<Channel> optionalChannel = channelRepo.findById(channel.getId());
        if(optionalUser.isEmpty() || optionalChannel.isEmpty()) return AppResponse.error().withMessage("User or Channel not found").build();
        Optional<Channel_User> optionalChannelUser = channel_UserRepo.findByChannelAndUser(optionalChannel.get(),optionalUser.get());
        if(optionalChannelUser.isEmpty() || optionalChannelUser.get().getUserRole() == Channel_User.UserRole.GUEST) return AppResponse.error().withMessage("Invalid User").build();

        optionalChannel.get().setName(channel.getName());
        channelRepo.save(optionalChannel.get());
        return AppResponse.success().withMessage("Channel Updated Successfully").build();
    }

    @Transactional
    public ResponseEntity<?> removeUserFromChannel(int channelID, int authID, int userID) {
        Optional<Channel> optionalChannel = channelRepo.findById(channelID);
        Optional<User> optionalUser = userRepo.findById(authID);
        Optional<Channel_User> optionalChannelUser = channel_UserRepo.findById(userID);
        if(optionalChannel.isEmpty() || optionalChannelUser.isEmpty() || optionalUser.isEmpty()) return AppResponse.error().withMessage("Invalid User Or Channel ID").build();

        Optional<Channel_User> optionalAuthUser = channel_UserRepo.findByChannelAndUser(optionalChannel.get(),optionalUser.get());
        if(optionalAuthUser.isEmpty() || optionalAuthUser.get().getUserRole() == Channel_User.UserRole.GUEST) return AppResponse.error().withMessage("No Authority").build();
        if(optionalChannelUser.get().getUserRole() == Channel_User.UserRole.OWNER) return AppResponse.error().withMessage("Cant Kick Owner").build();
        if (optionalChannelUser.get().getUserRole() != Channel_User.UserRole.GUEST && optionalAuthUser.get().getUserRole() != Channel_User.UserRole.OWNER) return AppResponse.error().withMessage("Not Enough Authority").build();

        channel_UserRepo.delete(optionalChannelUser.get());
        return AppResponse.success().withMessage("User Removed Successfully").build();
    }

    @Transactional
    public ResponseEntity<?> updateChannelUser(int authID, Channel_User channelUser) {

        Optional<Channel> optionalChannel = channelRepo.findById(channelUser.getChannel().getId());
        Optional<User> optionalUser = userRepo.findById(authID);
        Optional<Channel_User> optionalChannelUser = channel_UserRepo.findById(channelUser.getId());
        if(optionalChannel.isEmpty() || optionalChannelUser.isEmpty() || optionalUser.isEmpty()) return AppResponse.error().withMessage("Invalid User Or Channel ID").build();

        Optional<Channel_User> optionalAuthUser = channel_UserRepo.findByChannelAndUser(optionalChannel.get(),optionalUser.get());
        if(optionalAuthUser.isEmpty() || optionalAuthUser.get().getUserRole() == Channel_User.UserRole.GUEST) return AppResponse.error().withMessage("No Authority").build();

        if (optionalChannelUser.get().getUserRole() == Channel_User.UserRole.OWNER) return AppResponse.error().withMessage("Cannot Modify Owner").build();

        channel_UserRepo.save(channelUser);
        return AppResponse.success().withMessage("ChannelUser Updated Successfully").build();
    }
}
