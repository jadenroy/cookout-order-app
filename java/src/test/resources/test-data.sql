INSERT INTO users(user_id, username, password_hash, role) VALUES
(1, 'hostUser', 'password', 'USER'),
(2, 'chefUser', 'password', 'USER');

-- Add a menu
INSERT INTO menus(menu_id) VALUES (1);

-- Add a cookout
INSERT INTO cookouts(cookout_id, host_id, chef_id, menu_id, start_time, name, location, description) VALUES
(1, 1, 2, 1, '2025-01-15 12:30:00', 'Backyard BBQ', 'backyard', 'event_one');
