package org.example.discordripoff.services;

import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.Channel_User;
import org.example.discordripoff.entities.Friendship;
import org.example.discordripoff.entities.User;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.repositories.ChannelRepo;
import org.example.discordripoff.repositories.FriendshipRepo;
import org.example.discordripoff.repositories.MessageRepo;
import org.example.discordripoff.repositories.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelService {
    private final ChannelRepo channelRepo;
    private final UserRepo userRepo;
    private final MessageRepo messageRepo;

    public ChannelService(ChannelRepo channelRepo, UserRepo userRepo, MessageRepo messageRepo) {
        this.channelRepo = channelRepo;
        this.userRepo = userRepo;
        this.messageRepo = messageRepo;
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
        return AppResponse.success().withMessage("Channel Created Successfully").build();
    }

    public ResponseEntity<?> getChannelFromOwnerId(int OwnerId)
    {

        Optional<User> optionalUser = userRepo.findById(OwnerId);
        if (optionalUser.isEmpty()) return AppResponse.error().withMessage("User not found").build();

        return AppResponse.success().withData(channelRepo.findByOwnerAndIsActiveTrue(optionalUser.get())).build();
    }

//    public void softDeleteChannel(Integer channelId) {
//        Optional<Channel> channel = channelRepo.findByIdAndIsActiveTrue(channelId);
//        if (channel.isPresent()) {
//            channelRepo.softDeleteById(channelId);
//        }
//    }

    public List<Channel> getAllActiveChannels() {
        return channelRepo.findAllByIsActiveTrue();
    }

    public ResponseEntity<?> getMessagesFromChannelID(int id) {
        Optional<Channel> optionalChannel = channelRepo.findById(id);
        if (optionalChannel.isEmpty()) return AppResponse.error().withMessage("Channel not found").build();

        return AppResponse.success().withData(messageRepo.findByChannel(optionalChannel.get())).build();
    }
}
