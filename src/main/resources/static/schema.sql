CREATE DATABASE munan_db;

create schema public;

CREATE TABLE users
(
    id             UUID PRIMARY KEY                       DEFAULT gen_random_uuid(),
    email          TEXT NOT NULL UNIQUE,
    full_name      TEXT,
    role           TEXT CHECK (role IN ('USER', 'OWNER')) DEFAULT 'USER',
    phone_verified BOOLEAN                                DEFAULT FALSE,
    created_at     TIMESTAMP                              DEFAULT NOW()
);

CREATE TABLE courses
(
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title         TEXT NOT NULL,
    description   TEXT,
    thumbnail_url TEXT,
    price         INT  NOT NULL CHECK (price >= 0),
    created_by    UUID NOT NULL,
    published     BOOLEAN          DEFAULT FALSE,
    created_at    TIMESTAMP        DEFAULT NOW(),

    FOREIGN KEY (created_by) REFERENCES users (id)
);

CREATE TABLE lessons
(
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    course_id UUID NOT NULL,
    title     TEXT NOT NULL,
    content   TEXT,                      -- text hoặc iframe URL
    "order"   INT,                       -- số thứ tự trong khóa
    duration  INT CHECK (duration >= 0), -- giây
    created_at TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE
);

CREATE TABLE user_progress
(
    id           SERIAL PRIMARY KEY,
    user_id      UUID NOT NULL,
    lesson_id    UUID NOT NULL,
    status       TEXT CHECK (status IN ('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED')) DEFAULT 'NOT_STARTED',
    started_at   TIMESTAMP,
    completed_at TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (lesson_id) REFERENCES lessons (id) ON DELETE CASCADE,
    UNIQUE (user_id, lesson_id) -- Mỗi người chỉ có 1 record progress/bài học
);

CREATE TABLE purchases
(
    id               SERIAL PRIMARY KEY,
    user_id          UUID NOT NULL,
    course_id        UUID NOT NULL,
    payment_method   TEXT,
    payment_verified BOOLEAN   DEFAULT FALSE,
    verified_by      UUID,
    created_at       TIMESTAMP DEFAULT NOW(),

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (course_id) REFERENCES courses (id) ON DELETE CASCADE,
    FOREIGN KEY (verified_by) REFERENCES users (id),
    UNIQUE (user_id, course_id) -- 1 user mua 1 khoá 1 lần
);

CREATE TABLE blogs
(
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title          TEXT NOT NULL,
    slug           TEXT UNIQUE,
    content        TEXT,
    excerpt        TEXT,
    featured_image TEXT,
    category       TEXT,
    tags           TEXT[],
    status         TEXT             DEFAULT 'draft',
    created_by     UUID NOT NULL,
    created_at     TIMESTAMP        DEFAULT NOW(),
    updated_at     TIMESTAMP,
    views          INT              DEFAULT 0,
    votes          INT              DEFAULT 0,

    FOREIGN KEY (created_by) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE comments
(
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    blog_id    UUID NOT NULL,
    user_id    UUID NOT NULL,
    parent_id  UUID, -- hỗ trợ nested comment
    content    TEXT NOT NULL,
    created_at TIMESTAMP        DEFAULT NOW(),

    FOREIGN KEY (blog_id) REFERENCES blogs (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (parent_id) REFERENCES comments (id) ON DELETE CASCADE
);

CREATE TABLE votes
(
    id        SERIAL PRIMARY KEY,
    blog_id   UUID NOT NULL,
    user_id   UUID NOT NULL,
    vote_type TEXT CHECK (vote_type IN ('UP', 'DOWN')),

    FOREIGN KEY (blog_id) REFERENCES blogs (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    UNIQUE (blog_id, user_id) -- 1 user chỉ vote 1 lần/blog
);

CREATE TABLE phone_verifications
(
    id           SERIAL PRIMARY KEY,
    user_id      UUID NOT NULL,
    phone_number TEXT NOT NULL,
    zalo_status  TEXT CHECK (zalo_status IN ('PENDING', 'VERIFIED', 'FAILED')) DEFAULT 'PENDING',
    requested_at TIMESTAMP                                                     DEFAULT NOW(),
    verified_at  TIMESTAMP,

    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

