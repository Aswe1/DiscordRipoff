package org.example.discordripoff.controllers;


import org.example.discordripoff.entities.Message;
import org.example.discordripoff.entities.User;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.services.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/friends")
public class FriendshipController {

    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }


    @GetMapping("/add")
    public ResponseEntity<?> addFriend(@RequestParam int userId, @RequestParam int friendId) {
        return friendshipService.addFriend(userId, friendId);
    }

    @GetMapping
    public ResponseEntity<?> getAllFrienships() {
        return AppResponse.success().withData(friendshipService.findFriendships()).build();
    }

    @GetMapping("/{Id}")
    public ResponseEntity<?> getFriendFromId(@PathVariable int Id) {
        return friendshipService.getFriendFromId(Id);
    }

    @GetMapping("/messages/{user}/{friend}")
    public ResponseEntity<?> getMessageFromId(@PathVariable int user, @PathVariable int friend) {
        return friendshipService.getMessageFromId(user,friend);
    }
    @PostMapping("/messages")
    public ResponseEntity<?> sendMessageToID(@RequestBody Message msg) {
        return friendshipService.sendMessage(msg);
    }

}