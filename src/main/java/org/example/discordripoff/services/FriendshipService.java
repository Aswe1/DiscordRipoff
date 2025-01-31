package org.example.discordripoff.services;

import org.example.discordripoff.entities.Friendship;
import org.example.discordripoff.entities.Message;
import org.example.discordripoff.entities.User;
import org.example.discordripoff.http.AppResponse;
import org.example.discordripoff.repositories.FriendshipRepo;
import org.example.discordripoff.repositories.MessageRepo;
import org.example.discordripoff.repositories.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    private final FriendshipRepo friendshipRepo;
    private final UserRepo userRepo;
    private final MessageRepo messageRepo;

    public FriendshipService(UserRepo userRepository,FriendshipRepo friendshipRepository, MessageRepo messageRepository) {
        this.userRepo = userRepository;
        this.friendshipRepo = friendshipRepository;
        this.messageRepo = messageRepository;
    }

    public List<Friendship> findFriendships()
    {
        return PrepData(friendshipRepo.findAll());
    }

    public ResponseEntity<?> addFriend(int userId, int friendId)
    {
        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<User> optionalFriend = userRepo.findById(friendId);

        if(optionalUser.isPresent() && optionalFriend.isPresent()) {
            if (optionalUser.get().getId() == optionalFriend.get().getId()) {
                return AppResponse.error().withMessage("Cant Add Yourself as a friend").build();
            }

            User user = optionalUser.get();
            User friend = optionalFriend.get();

            List<Friendship> existingFriendship = friendshipRepo.findByUser1AndUser2(user, friend);
            if (!existingFriendship.isEmpty())
                return AppResponse.error().withMessage("Friendship already exists").build();

            Friendship friendship1 = new Friendship();
            friendship1.setUser1(user);
            friendship1.setUser2(friend);

            Friendship friendship2 = new Friendship();
            friendship2.setUser1(friend);
            friendship2.setUser2(user);

            friendshipRepo.save(friendship1);
            friendshipRepo.save(friendship2);
            return AppResponse.success().withMessage("Friendship added successfully").build();
        }
        return AppResponse.error().withMessage("Either User1 Or User2 Not Found").build();
    }

    public ResponseEntity<?> getFriendFromId(int id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if(optionalUser.isPresent())
        {
            User user = optionalUser.get();
            List<Friendship> existingFriendship = friendshipRepo.findByUser1(user);
            if(!existingFriendship.isEmpty())
                existingFriendship = PrepData(existingFriendship);

            return AppResponse.success().withData(existingFriendship).build();
        }
        return AppResponse.error().withMessage("User with given ID not found").build();
    }

    // This will clear Friendship data as it is currently its recursive loop,
    public Friendship PrepData(Friendship Friendship)
    {
//        Friendship.getUser1().setFriendships(new ArrayList<>());
//        Friendship.getUser2().setFriendships(new ArrayList<>());
        return Friendship;
    }
    // Same as PrepData just for ArrayLists
    public List<Friendship> PrepData(List<Friendship> Friendships) {
        for (Friendship friendship : Friendships) {
//            friendship.getUser1().setFriendships(new ArrayList<>());
//            friendship.getUser2().setFriendships(new ArrayList<>());
        }
        return Friendships;
    }

    public ResponseEntity<?> getMessageFromId(int userId,int friendId)
    {
        Optional<User> optionalUser = userRepo.findById(userId);
        Optional<User> optionalFriend = userRepo.findById(friendId);
        if(optionalUser.isEmpty() || optionalFriend.isEmpty()) return AppResponse.error().withMessage("User not found").build();

        return AppResponse.success().withData(messageRepo.findByTwoUsers(optionalUser.get(), optionalFriend.get())).build();
    }

    public ResponseEntity<?> sendMessage(Message message)
    {
        Optional<User> optionalSender = userRepo.findById(message.getSender().getId());
        Optional<User> optionalReceiver = userRepo.findById(message.getReceiver().getId());
        if(optionalSender.isEmpty() || optionalReceiver.isEmpty()) return AppResponse.error().withMessage("Bad Message").build();
        if (message.getContent().isEmpty()) return AppResponse.error().withMessage("Empty Message").build();

        messageRepo.save(message);
        return AppResponse.success().withMessage("Message added successfully").build();
    }
}