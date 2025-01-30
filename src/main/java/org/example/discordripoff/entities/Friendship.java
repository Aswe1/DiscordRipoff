package org.example.discordripoff.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "friendships")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user1_id", nullable = false)
    private User user1;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user2_id", nullable = false)
    private User user2;

    public Friendship() {}

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

}

