CREATE TABLE IF NOT EXISTS channels_users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    channel_id INT NOT NULL,
    user_id INT NOT NULL,
    user_role ENUM('OWNER', 'ADMIN', 'GUEST') DEFAULT 'GUEST',
    FOREIGN KEY (channel_id) REFERENCES channels(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
    );