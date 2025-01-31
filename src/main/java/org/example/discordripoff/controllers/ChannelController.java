package org.example.discordripoff.controllers;

import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.Channel_User;
import org.example.discordripoff.entities.Message;
import org.example.discordripoff.services.ChannelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;

    public ChannelController(ChannelService channelService) {
        this.channelService = channelService;
    }

    @PostMapping("/new")
    public ResponseEntity<?> createNewChannel(@RequestBody Channel channel) {
        return channelService.createNewChannel(channel);
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> getChannelFromOwnerId(@PathVariable int Id) {
        return channelService.getChannelFromUserId(Id);
    }

    @GetMapping("/{ChannelID}/auth/{authID}/add/{UserID}")
    public ResponseEntity<?> addUserToChannel(@PathVariable int ChannelID,@PathVariable int authID,@PathVariable int UserID) {
        return channelService.addUserToChannel(ChannelID,authID, UserID);
    }

    @GetMapping("/{Id}/users")
    public ResponseEntity<?> getChannelUsersFromChannelID(@PathVariable int Id) {
        return channelService.getChannelUsersFromChannelID(Id);
    }

    @GetMapping("/{Id}/messages")
    public ResponseEntity<?> getMessagesFromChannelID(@PathVariable int Id) {
        return channelService.getMessagesFromChannelID(Id);
    }

    @PostMapping("/messages")
    public ResponseEntity<?> sendMessageToChannelID(@RequestBody Message msg) {
        return channelService.sendMessageToChannelID(msg);
    }

    @DeleteMapping("/{channelID}/auth/{authID}")
    public ResponseEntity<?> softDeleteChannel(@PathVariable int channelID, @PathVariable int authID) {
        return channelService.softDeleteChannel(channelID ,authID);
    }

    @DeleteMapping("/{channelID}/auth/{authID}/user/{userID}")
    public ResponseEntity<?> kickUserFromChannel(@PathVariable int channelID, @PathVariable int authID, @PathVariable int userID) {
        return channelService.removeUserFromChannel(channelID ,authID, userID);
    }
    @PutMapping("/{authID}")
    public ResponseEntity<?> updateChannel(@PathVariable int authID, @RequestBody Channel channel) {
        return channelService.updateChannel(authID ,channel);
    }

    @PutMapping("/{authID}/users")
    public ResponseEntity<?> updateChannelUser(@PathVariable int authID, @RequestBody Channel_User channelUser) {
        return channelService.updateChannelUser(authID ,channelUser);
    }

    @GetMapping
    public List<Channel> getActiveChannels() {
        return channelService.getAllActiveChannels();
    }

}
