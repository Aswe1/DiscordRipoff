package org.example.discordripoff.controllers;

import org.example.discordripoff.entities.Channel;
import org.example.discordripoff.entities.Message;
import org.example.discordripoff.http.AppResponse;
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
        return channelService.getChannelFromOwnerId(Id);
    }
    @GetMapping("/messages/{Id}")
    public ResponseEntity<?> getMessagesFromChannelID(@PathVariable int Id) {
        return channelService.getMessagesFromChannelID(Id);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> softDeleteChannel(@PathVariable Integer id) {
//        channelService.softDeleteChannel(id);
//        return ResponseEntity.ok("Channel soft deleted successfully");
//    }

    @GetMapping
    public List<Channel> getActiveChannels() {
        return channelService.getAllActiveChannels();
    }

}
