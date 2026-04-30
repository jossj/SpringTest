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

-- Rewards table
CREATE TABLE IF NOT EXISTS rewards (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    user_id     BIGINT       NOT NULL,
    type        VARCHAR(50)  NOT NULL,  -- BEHAVIOR | ACADEMIC | HOMEWORK | SPORTS
    description VARCHAR(500) NOT NULL,
    points      INT          NOT NULL DEFAULT 0,
    awarded_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (id),
    CONSTRAINT fk_rewards_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Seed data (optional – remove if you prefer an empty table)
INSERT INTO users (username, email, role) VALUES
    ('alice', 'alice@example.com', 'USER'),
    ('bob',   'bob@example.com',   'USER'),
    ('admin', 'admin@example.com', 'ADMIN');
