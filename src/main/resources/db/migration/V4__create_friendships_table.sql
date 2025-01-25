CREATE TABLE IF NOT EXISTS friendships (
    user1_id INT,
    user2_id INT,
    PRIMARY KEY(user1_id, user2_id),
    FOREIGN KEY (user1_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (user2_id) REFERENCES users(id) ON DELETE CASCADE
);