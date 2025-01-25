CREATE TABLE IF NOT EXISTS messages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    content TEXT NOT NULL,
    sender_id INT,
    channel_id INT,
    FOREIGN KEY (sender_id) REFERENCES users(id),
    FOREIGN KEY (channel_id) REFERENCES channels(id)
);