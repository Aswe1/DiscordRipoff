package org.example.discordripoff.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Check reference equality
        if (o == null || getClass() != o.getClass()) return false; // Check type
        User user = (User) o;
        return Objects.equals(email, user.email); // Compare unique identifiers
    }

    @Override
    public int hashCode() {
        return Objects.hash(email); // Ensure consistency with equals
    }

}
