BEGIN TRANSACTION;

-- Create at least one default menu so FK_menu always has something valid to reference
INSERT INTO menus DEFAULT VALUES;   -- This will create menu_id = 1

-- the password for both users is "password"
INSERT INTO users (username, password_hash, role)
VALUES ('user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_USER');

INSERT INTO users (username, password_hash, role)
VALUES ('admin', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', 'ROLE_ADMIN');

COMMIT TRANSACTION;
