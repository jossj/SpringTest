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
    ('alice', 'alice@example.com', 'USER'),   -- id 1
    ('bob',   'bob@example.com',   'USER'),   -- id 2
    ('admin', 'admin@example.com', 'ADMIN');  -- id 3

-- Rewards seed data (4 per user, one of each type)
INSERT INTO rewards (user_id, type, description, points, awarded_at) VALUES
    -- Alice (id 1)
    (1, 'ACADEMIC',  'Top marks in maths test',          50, DATEADD('DAY', -10, CURRENT_TIMESTAMP)),
    (1, 'HOMEWORK',  'Completed all homework on time',   20, DATEADD('DAY', -7,  CURRENT_TIMESTAMP)),
    (1, 'BEHAVIOR',  'Helped a classmate',               15, DATEADD('DAY', -5,  CURRENT_TIMESTAMP)),
    (1, 'SPORTS',    'First place in 100m sprint',       40, DATEADD('DAY', -2,  CURRENT_TIMESTAMP)),

    -- Bob (id 2)
    (2, 'BEHAVIOR',  'Outstanding classroom conduct',    25, DATEADD('DAY', -12, CURRENT_TIMESTAMP)),
    (2, 'SPORTS',    'Man of the match — football',      35, DATEADD('DAY', -8,  CURRENT_TIMESTAMP)),
    (2, 'HOMEWORK',  'Perfect homework streak (2 weeks)',30, DATEADD('DAY', -3,  CURRENT_TIMESTAMP)),
    (2, 'ACADEMIC',  'Science project excellence',       45, DATEADD('DAY', -1,  CURRENT_TIMESTAMP)),

    -- Admin (id 3)
    (3, 'ACADEMIC',  'Completed advanced training',      60, DATEADD('DAY', -15, CURRENT_TIMESTAMP)),
    (3, 'BEHAVIOR',  'Mentored new team members',        50, DATEADD('DAY', -9,  CURRENT_TIMESTAMP)),
    (3, 'SPORTS',    'Won staff charity fun run',        20, DATEADD('DAY', -4,  CURRENT_TIMESTAMP)),
    (3, 'HOMEWORK',  'Submitted all reports early',      25, DATEADD('DAY', -1,  CURRENT_TIMESTAMP));
