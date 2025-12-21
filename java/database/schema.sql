
BEGIN TRANSACTION;

DROP TABLE IF EXISTS invitations;
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS cookout_attendees;
DROP TABLE IF EXISTS cookouts;
DROP TABLE IF EXISTS menus;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
	user_id SERIAL,
	username varchar(50) NOT NULL UNIQUE,
	password_hash varchar(200) NOT NULL,
	role varchar(50) NOT NULL,
	CONSTRAINT PK_user PRIMARY KEY (user_id)
);

CREATE TABLE menus (
	menu_id SERIAL,
	CONSTRAINT PK_menu PRIMARY KEY (menu_id)
);

CREATE TABLE cookouts (
	cookout_id SERIAL,
	host_id INT,
	chef_id INT,
	menu_id INT,
	start_time TIMESTAMP,
    name VARCHAR(100),
    location VARCHAR(200),
    description VARCHAR(500),
	CONSTRAINT PK_cookout PRIMARY KEY (cookout_id),
	CONSTRAINT FK_host FOREIGN KEY (host_id) REFERENCES users(user_id),
	CONSTRAINT FK_chef FOREIGN KEY (chef_id) REFERENCES users(user_id),
	CONSTRAINT FK_menu FOREIGN KEY (menu_id) REFERENCES menus(menu_id)
);

CREATE TABLE cookout_attendees (
	cookout_id INT NOT NULL,
	attendee_id INT NOT NULL,
	CONSTRAINT PK_cookout_attendee PRIMARY KEY (cookout_id, attendee_id),
	CONSTRAINT FK_cookout FOREIGN KEY (cookout_id) REFERENCES cookouts(cookout_id),
	CONSTRAINT FK_attendee FOREIGN KEY (attendee_id) REFERENCES users(user_id)
);

CREATE TABLE menu_items (
	id SERIAL,
	cookout_id INT,
	item_name VARCHAR(50),
	CONSTRAINT PK_menu_item PRIMARY KEY (id),
	CONSTRAINT FK_cookout FOREIGN KEY (cookout_id) REFERENCES cookouts(cookout_id)
);

CREATE TABLE orders (
	order_id SERIAL,
	cookout_id INT,
	attendee_id INT,
	finished BOOLEAN DEFAULT FALSE,
	CONSTRAINT PK_order PRIMARY KEY (order_id),
	CONSTRAINT FK_cookout FOREIGN KEY (cookout_id) REFERENCES cookouts(cookout_id),
	CONSTRAINT FK_attendee FOREIGN KEY (attendee_id) REFERENCES users(user_id)
);

CREATE TABLE order_items (
	id SERIAL,
	order_id INT,
	item_id INT,
	quantity INT,
	CONSTRAINT PK_order_item PRIMARY KEY (id),
	CONSTRAINT FK_order FOREIGN KEY (order_id) REFERENCES orders(order_id),
	CONSTRAINT FK_item FOREIGN KEY (item_id) REFERENCES menu_items(id)
);

CREATE TABLE invitations (
	invitation_id SERIAL,
	from_id INT,
	to_id INT,
	cookout_id INT,
	active BOOLEAN DEFAULT TRUE,
	accepted BOOLEAN DEFAULT FALSE,
	CONSTRAINT PK_invitation PRIMARY KEY (invitation_id),
	CONSTRAINT FK_from FOREIGN KEY (from_id) REFERENCES users(user_id),
	CONSTRAINT FK_to FOREIGN KEY (to_id) REFERENCES users(user_id),
	CONSTRAINT FK_cookout FOREIGN KEY (cookout_id) REFERENCES cookouts(cookout_id)
);

COMMIT TRANSACTION;


