CREATE TABLE IF NOT EXISTS channels_users (
    channel_id INT,
    user_id INT,
    user_role ENUM('OWNER', 'ADMIN', 'GUEST') DEFAULT 'GUEST',
    PRIMARY KEY (channel_id, user_id),
    FOREIGN KEY (channel_id) REFERENCES channels(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
    );