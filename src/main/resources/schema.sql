-- Users table
CREATE TABLE IF NOT EXISTS users (
    id       BIGINT       NOT NULL AUTO_INCREMENT,
    username VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL,
    role     VARCHAR(50)  NOT NULL DEFAULT 'USER',

    PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email    UNIQUE (email)
);

-- Seed data (optional – remove if you prefer an empty table)
INSERT INTO users (username, email, role) VALUES
    ('alice', 'alice@example.com', 'USER'),
    ('bob',   'bob@example.com',   'USER'),
    ('admin', 'admin@example.com', 'ADMIN');
