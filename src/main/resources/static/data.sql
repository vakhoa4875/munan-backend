-- Thêm dữ liệu mẫu cho bảng users
INSERT INTO users (id, email, full_name, role, phone_verified, created_at)
VALUES
  ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'admin@example.com', 'Admin User', 'OWNER', true, NOW()),
  ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'user1@example.com', 'User One', 'USER', false, NOW()),
  ('a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'user2@example.com', 'User Two', 'USER', true, NOW());

-- Thêm dữ liệu mẫu cho bảng courses
INSERT INTO courses (id, title, description, thumbnail_url, price, created_by, published, created_at)
VALUES
  ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'GraphQL Fundamentals', 'Learn the basics of GraphQL', 'https://example.com/thumbnails/graphql.jpg', 99000, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', true, NOW()),
  ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Spring Boot Advanced', 'Master Spring Boot', 'https://example.com/thumbnails/spring.jpg', 149000, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', true, NOW()),
  ('b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'React for Beginners', 'Start with React', 'https://example.com/thumbnails/react.jpg', 79000, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', false, NOW());

-- Thêm dữ liệu mẫu cho bảng lessons
INSERT INTO lessons (id, course_id, title, content, "order", duration)
VALUES
  ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Introduction to GraphQL', 'What is GraphQL and why use it?', 1, 600),
  ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Queries and Mutations', 'Learn about GraphQL operations', 2, 900),
  ('c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Spring Boot Basics', 'Getting started with Spring Boot', 1, 1200);

-- Thêm dữ liệu mẫu cho bảng blogs
INSERT INTO blogs (id, title, slug, content, excerpt, featured_image, category, tags, status, created_by, created_at, views, votes)
VALUES
  ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'Getting Started with GraphQL', 'getting-started-with-graphql', 'This is a detailed guide about GraphQL...', 'Learn the basics of GraphQL', 'https://example.com/images/graphql.jpg', 'Programming', ARRAY['graphql', 'api', 'web'], 'published', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', NOW(), 120, 15),
  ('d0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'Spring Boot Best Practices', 'spring-boot-best-practices', 'Here are some best practices for Spring Boot...', 'Improve your Spring Boot applications', 'https://example.com/images/spring.jpg', 'Programming', ARRAY['spring', 'java', 'backend'], 'published', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', NOW(), 85, 10);

-- Thêm dữ liệu mẫu cho bảng purchases
INSERT INTO purchases (id, user_id, course_id, payment_method, payment_verified, verified_by, created_at)
VALUES
  (1, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'BANK_TRANSFER', true, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', NOW()),
  (2, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'MOMO', true, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', NOW()),
  (3, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'b0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'BANK_TRANSFER', false, null, NOW());

-- Thêm dữ liệu mẫu cho bảng comments
INSERT INTO comments (id, blog_id, user_id, parent_id, content, created_at)
VALUES
  ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', null, 'Great article! Very helpful.', NOW()),
  ('e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'e0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'I agree, this helped me a lot!', NOW());

-- Thêm dữ liệu mẫu cho bảng votes
INSERT INTO votes (id, blog_id, user_id, vote_type)
VALUES
  (1, 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'UP'),
  (2, 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'UP'),
  (3, 'd0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'UP');

-- Thêm dữ liệu mẫu cho bảng user_progress
INSERT INTO user_progress (id, user_id, lesson_id, status, started_at, completed_at)
VALUES
  (1, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'COMPLETED', NOW() - INTERVAL '2 days', NOW() - INTERVAL '1 day'),
  (2, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a12', 'IN_PROGRESS', NOW() - INTERVAL '1 day', null),
  (3, 'a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a13', 'c0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11', 'COMPLETED', NOW() - INTERVAL '3 days', NOW() - INTERVAL '2 days');