package org.example.discordripoff.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String username;
    private String password;
    private String email;
    @Column(name = "is_active")
    private boolean isActive = true;

    @ManyToMany
    @JoinTable(
            name = "friendships", // Таблицата за връзката
            joinColumns = @JoinColumn(name = "user1_id"), // user1_id като текущия потребител
            inverseJoinColumns = @JoinColumn(name = "user2_id") // user2_id като приятел
    )
    private List<User> friends = new ArrayList<>();

}
